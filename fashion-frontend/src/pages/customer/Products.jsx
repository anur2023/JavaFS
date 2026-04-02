import { useState, useEffect } from 'react'
import { productApi } from '../../api/product'
import { wishlistApi, tryOnApi } from '../../api/modules'
import { ProductCard, SkeletonCard, PageHeader, EmptyState, Modal, useToast, Spinner, FormField } from '../../components/ui'

export default function Products() {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [tryOnModal, setTryOnModal] = useState(null)
  const [tryOnUrl, setTryOnUrl] = useState('')
  const [tryOnLoading, setTryOnLoading] = useState(false)
  const [tryOnResult, setTryOnResult] = useState(null)
  const [detailModal, setDetailModal] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchProducts() }, [])

  const fetchProducts = async () => {
    setLoading(true)
    try {
      const { data } = await productApi.getAll()
      setProducts(Array.isArray(data) ? data : [])
    } catch { show('Failed to load products', 'error') }
    finally { setLoading(false) }
  }

  const handleAddToWishlist = async (product) => {
    try {
      await wishlistApi.add(product.productId)
      show(`${product.name} added to wishlist`)
    } catch (e) { show(e.response?.data?.message || 'Error adding to wishlist', 'error') }
  }

  const handleTryOn = (product) => {
    setTryOnModal(product)
    setTryOnUrl('')
    setTryOnResult(null)
  }

  const submitTryOn = async () => {
    if (!tryOnUrl.trim()) return show('Please enter your photo URL', 'error')
    setTryOnLoading(true)
    try {
      const { data } = await tryOnApi.start({ productId: tryOnModal.productId, userImageUrl: tryOnUrl })
      setTryOnResult(data)
      show('Virtual try-on complete!')
    } catch (e) { show(e.response?.data?.message || 'Try-on failed', 'error') }
    finally { setTryOnLoading(false) }
  }

  const filtered = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.category?.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <PageHeader label="Collection" title="Shop All" subtitle="Discover our curated fashion collection" />

      {/* Search */}
      <div className="flex gap-3 mb-8">
        <input className="input-field max-w-sm" placeholder="Search by name or category…"
          value={search} onChange={e => setSearch(e.target.value)} />
        {search && <button onClick={() => setSearch('')} className="btn-outline px-4 py-2 text-xs">Clear</button>}
      </div>

      {loading
        ? <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
            {[...Array(8)].map((_, i) => <SkeletonCard key={i} />)}
          </div>
        : filtered.length === 0
          ? <EmptyState icon="✦" title="No products found" subtitle="Try a different search term" />
          : <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5">
              {filtered.map(p => (
                <ProductCard key={p.productId} product={p}
                  onAddToWishlist={handleAddToWishlist}
                  onTryOn={handleTryOn}
                  onClick={() => setDetailModal(p)} />
              ))}
            </div>
      }

      {/* Product Detail Modal */}
      {detailModal && (
        <Modal title={detailModal.name} onClose={() => setDetailModal(null)} wide>
          <div className="flex gap-6">
            <div className="w-40 h-40 bg-cream-100 flex-shrink-0 flex items-center justify-center overflow-hidden">
              {detailModal.images?.[0]?.imageUrl
                ? <img src={detailModal.images[0].imageUrl} alt={detailModal.name} className="w-full h-full object-cover" />
                : <span className="font-display text-4xl italic text-charcoal-700/20">É</span>
              }
            </div>
            <div className="flex-1 space-y-3">
              <p className="section-label">{detailModal.category}</p>
              <p className="font-mono text-xl text-gold-600 font-medium">₹{detailModal.price?.toLocaleString()}</p>
              <p className="text-sm text-charcoal-700/70">{detailModal.description || 'No description available.'}</p>
              {detailModal.brand && <p className="text-xs font-mono text-charcoal-700/50">Brand: {detailModal.brand}</p>}
              {detailModal.materialDetails && <p className="text-xs font-mono text-charcoal-700/50">Material: {detailModal.materialDetails}</p>}
              <p className="text-xs font-mono text-charcoal-700/50">
                Stock: <span className={detailModal.stockQuantity > 0 ? 'text-green-600' : 'text-red-500'}>{detailModal.stockQuantity > 0 ? `${detailModal.stockQuantity} available` : 'Out of stock'}</span>
              </p>
            </div>
          </div>
          <div className="flex gap-3 mt-5">
            <button className="btn-primary flex-1" onClick={() => { handleAddToWishlist(detailModal); setDetailModal(null) }}>Add to Wishlist</button>
            <button className="btn-outline" onClick={() => { setDetailModal(null); handleTryOn(detailModal) }}>Try On</button>
          </div>
        </Modal>
      )}

      {/* Try-On Modal */}
      {tryOnModal && (
        <Modal title={`Virtual Try-On — ${tryOnModal.name}`} onClose={() => { setTryOnModal(null); setTryOnResult(null) }} wide>
          {!tryOnResult
            ? <div className="space-y-4">
                <p className="text-sm text-charcoal-700/70">Enter the URL of your photo to see how this item looks on you.</p>
                <FormField label="Your Photo URL">
                  <input className="input-field" placeholder="https://example.com/your-photo.jpg"
                    value={tryOnUrl} onChange={e => setTryOnUrl(e.target.value)} />
                </FormField>
                <button onClick={submitTryOn} disabled={tryOnLoading} className="btn-gold w-full flex items-center justify-center gap-2">
                  {tryOnLoading ? <><Spinner size="sm" /> Processing…</> : '✦ Start Virtual Try-On'}
                </button>
              </div>
            : <div className="space-y-4 text-center">
                <p className="section-label">Try-On Result</p>
                <div className="flex gap-4 justify-center">
                  <div>
                    <p className="text-xs font-mono text-charcoal-700/50 mb-2">Your Photo</p>
                    <img src={tryOnResult.userImageUrl} alt="User" className="w-32 h-40 object-cover bg-cream-100" onError={e => e.target.style.display='none'} />
                  </div>
                  <div className="flex items-center text-2xl text-gold-400">→</div>
                  <div>
                    <p className="text-xs font-mono text-charcoal-700/50 mb-2">AR Result</p>
                    <div className="w-32 h-40 bg-cream-100 flex items-center justify-center text-xs text-charcoal-700/50 font-mono p-2 text-center">
                      {tryOnResult.status === 'COMPLETED' ? 'Result ready (integrate real AR API)' : tryOnResult.apiMessage}
                    </div>
                  </div>
                </div>
                <p className="text-xs font-mono text-charcoal-700/40">Status: {tryOnResult.status}</p>
              </div>
          }
        </Modal>
      )}
    </div>
  )
}
