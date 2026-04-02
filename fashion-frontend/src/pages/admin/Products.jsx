import { useState, useEffect } from 'react'
import { productApi } from '../../api/product'
import { PageHeader, useToast, Spinner, Modal, FormField, EmptyState } from '../../components/ui'

const EMPTY_FORM = { name: '', description: '', price: '', stockQuantity: '', category: '', brand: '', style: '', occasion: '', materialDetails: '', careInstructions: '', sku: '' }

export default function AdminProducts() {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [modal, setModal] = useState(null) // null | 'create' | product-obj
  const [form, setForm] = useState(EMPTY_FORM)
  const [saving, setSaving] = useState(false)
  const [deletingId, setDeletingId] = useState(null)
  const [search, setSearch] = useState('')
  const { show, ToastEl } = useToast()

  useEffect(() => { fetch() }, [])

  const fetch = async () => {
    setLoading(true)
    try {
      const { data } = await productApi.getAll()
      setProducts(Array.isArray(data) ? data : [])
    } catch { show('Failed to load products', 'error') }
    finally { setLoading(false) }
  }

  const openCreate = () => { setForm(EMPTY_FORM); setModal('create') }
  const openEdit = (p) => {
    setForm({ name: p.name || '', description: p.description || '', price: p.price || '', stockQuantity: p.stockQuantity || '', category: p.category || '', brand: p.brand || '', style: p.style || '', occasion: p.occasion || '', materialDetails: p.materialDetails || '', careInstructions: p.careInstructions || '', sku: p.sku || '' })
    setModal(p)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    if (!form.name || !form.price) return show('Name and price are required', 'error')
    setSaving(true)
    try {
      const payload = { ...form, price: Number(form.price), stockQuantity: Number(form.stockQuantity) || 0 }
      if (modal === 'create') {
        await productApi.create(payload)
        show('Product created!')
      } else {
        await productApi.update(modal.productId, payload)
        show('Product updated!')
      }
      setModal(null)
      fetch()
    } catch (e) { show(e.response?.data?.message || 'Save failed', 'error') }
    finally { setSaving(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this product? This cannot be undone.')) return
    setDeletingId(id)
    try {
      await productApi.delete(id)
      setProducts(prev => prev.filter(p => p.productId !== id))
      show('Product deleted')
    } catch (e) { show(e.response?.data?.message || 'Delete failed', 'error') }
    finally { setDeletingId(null) }
  }

  const filtered = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.category?.toLowerCase().includes(search.toLowerCase())
  )

  const f = (k) => ({ value: form[k], onChange: e => setForm(prev => ({ ...prev, [k]: e.target.value })) })

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Inventory" title="Products" />
        <button onClick={openCreate} className="btn-primary">+ Add Product</button>
      </div>

      <input className="input-field max-w-sm mb-6" placeholder="Search products…" value={search} onChange={e => setSearch(e.target.value)} />

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : filtered.length === 0
          ? <EmptyState icon="◻" title="No products" subtitle="Add your first product to get started"
              action={<button onClick={openCreate} className="btn-primary">Add Product</button>} />
          : <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-charcoal-900/10">
                    {['Name', 'Category', 'Price', 'Stock', 'Brand', ''].map(h => (
                      <th key={h} className="text-left py-3 px-3 text-xs font-mono uppercase tracking-wider text-charcoal-700/50">{h}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {filtered.map(p => (
                    <tr key={p.productId} className="border-b border-charcoal-900/5 hover:bg-cream-100/50 transition-colors">
                      <td className="py-3 px-3">
                        <p className="font-medium text-charcoal-900">{p.name}</p>
                        {p.sku && <p className="text-xs font-mono text-charcoal-700/40">SKU: {p.sku}</p>}
                      </td>
                      <td className="py-3 px-3 text-charcoal-700/70">{p.category || '—'}</td>
                      <td className="py-3 px-3 font-mono text-gold-600">₹{p.price?.toLocaleString()}</td>
                      <td className="py-3 px-3">
                        <span className={`font-mono text-xs ${p.stockQuantity > 0 ? 'text-green-600' : 'text-red-500'}`}>
                          {p.stockQuantity}
                        </span>
                      </td>
                      <td className="py-3 px-3 text-charcoal-700/60">{p.brand || '—'}</td>
                      <td className="py-3 px-3">
                        <div className="flex gap-3">
                          <button onClick={() => openEdit(p)} className="text-xs font-mono text-gold-600 hover:underline">Edit</button>
                          <button onClick={() => handleDelete(p.productId)} disabled={deletingId === p.productId}
                            className="text-xs font-mono text-red-400 hover:text-red-600 disabled:opacity-40">
                            {deletingId === p.productId ? '…' : 'Delete'}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
      }

      {modal && (
        <Modal title={modal === 'create' ? 'Add New Product' : `Edit — ${modal.name}`} onClose={() => setModal(null)} wide>
          <form onSubmit={handleSave} className="space-y-4 max-h-[70vh] overflow-y-auto pr-1">
            <div className="grid grid-cols-2 gap-4">
              <FormField label="Product Name *">
                <input className="input-field" placeholder="e.g. Silk Wrap Dress" {...f('name')} required />
              </FormField>
              <FormField label="SKU">
                <input className="input-field" placeholder="e.g. SWD-001" {...f('sku')} />
              </FormField>
              <FormField label="Price (₹) *">
                <input type="number" className="input-field" placeholder="2999" {...f('price')} required />
              </FormField>
              <FormField label="Stock Quantity">
                <input type="number" className="input-field" placeholder="50" {...f('stockQuantity')} />
              </FormField>
              <FormField label="Category">
                <input className="input-field" placeholder="e.g. Dresses" {...f('category')} />
              </FormField>
              <FormField label="Brand">
                <input className="input-field" placeholder="e.g. ÉLUME" {...f('brand')} />
              </FormField>
              <FormField label="Style">
                <input className="input-field" placeholder="e.g. Casual, Formal" {...f('style')} />
              </FormField>
              <FormField label="Occasion">
                <input className="input-field" placeholder="e.g. Party, Work" {...f('occasion')} />
              </FormField>
            </div>
            <FormField label="Description">
              <textarea className="input-field resize-none" rows={3} placeholder="Product description…" {...f('description')} />
            </FormField>
            <FormField label="Material Details">
              <input className="input-field" placeholder="e.g. 100% Silk" {...f('materialDetails')} />
            </FormField>
            <FormField label="Care Instructions">
              <input className="input-field" placeholder="e.g. Dry clean only" {...f('careInstructions')} />
            </FormField>
            <button type="submit" disabled={saving} className="btn-primary w-full flex items-center justify-center gap-2">
              {saving ? <><Spinner size="sm" /> Saving…</> : (modal === 'create' ? 'Create Product' : 'Save Changes')}
            </button>
          </form>
        </Modal>
      )}
    </div>
  )
}
