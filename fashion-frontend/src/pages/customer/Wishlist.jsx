import { useState, useEffect } from 'react'
import { wishlistApi } from '../../api/modules'
import { PageHeader, EmptyState, useToast, Spinner } from '../../components/ui'
import { Link } from 'react-router-dom'

export default function Wishlist() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [removing, setRemoving] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchWishlist() }, [])

  const fetchWishlist = async () => {
    setLoading(true)
    try {
      const { data } = await wishlistApi.get()
      // Response is a WishlistResponse with wishlistId + items list
      // Backend returns single object with nested items
      if (Array.isArray(data)) setItems(data)
      else if (data?.items) setItems(data.items)
      else setItems([])
    } catch { show('Failed to load wishlist', 'error') }
    finally { setLoading(false) }
  }

  const handleRemove = async (productId) => {
    setRemoving(productId)
    try {
      await wishlistApi.remove(productId)
      setItems(prev => prev.filter(i => i.productId !== productId))
      show('Removed from wishlist')
    } catch { show('Failed to remove item', 'error') }
    finally { setRemoving(null) }
  }

  const handleClear = async () => {
    if (!confirm('Clear your entire wishlist?')) return
    try {
      await wishlistApi.clear()
      setItems([])
      show('Wishlist cleared')
    } catch { show('Failed to clear wishlist', 'error') }
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Saved Items" title="My Wishlist" />
        {items.length > 0 && (
          <button onClick={handleClear} className="text-xs font-mono text-red-400 hover:text-red-600 uppercase tracking-wider transition-colors">
            Clear All
          </button>
        )}
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : items.length === 0
          ? <EmptyState icon="♡" title="Your wishlist is empty"
              subtitle="Browse our collection and save items you love"
              action={<Link to="/customer/products" className="btn-primary">Shop Now</Link>} />
          : <div className="space-y-3">
              {items.map(item => (
                <div key={item.wishlistItemId} className="card flex items-center gap-4 p-4">
                  <div className="w-16 h-16 bg-cream-100 flex-shrink-0 flex items-center justify-center overflow-hidden">
                    <span className="font-display text-2xl italic text-charcoal-700/20">É</span>
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="section-label">{item.category}</p>
                    <h4 className="font-display text-base text-charcoal-900 truncate">{item.productName}</h4>
                    <p className="font-mono text-sm text-gold-600 font-medium">₹{item.price?.toLocaleString()}</p>
                  </div>
                  <div className="flex items-center gap-3 flex-shrink-0">
                    <span className="text-xs font-mono text-charcoal-700/40">
                      {new Date(item.addedAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}
                    </span>
                    <button
                      onClick={() => handleRemove(item.productId)}
                      disabled={removing === item.productId}
                      className="text-xs font-mono text-red-400 hover:text-red-600 uppercase tracking-wider transition-colors disabled:opacity-40"
                    >
                      {removing === item.productId ? '…' : 'Remove'}
                    </button>
                  </div>
                </div>
              ))}
            </div>
      }
    </div>
  )
}
