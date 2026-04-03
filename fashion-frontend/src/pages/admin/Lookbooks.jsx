import { useState, useEffect } from 'react'
import { lookbookApi } from '../../api/modules'
import { productApi } from '../../api/product'
import { PageHeader, useToast, Spinner, StatusBadge, Modal, FormField, EmptyState } from '../../components/ui'

const EMPTY_FORM = { title: '', description: '', season: '', style: '', coverImageUrl: '' }

export default function AdminLookbooks() {
  const [lookbooks, setLookbooks] = useState([])
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [saving, setSaving] = useState(false)
  const [itemModal, setItemModal] = useState(null)
  const [itemForm, setItemForm] = useState({ productId: '', stylingNote: '', displayOrder: 0 })
  const [addingItem, setAddingItem] = useState(false)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchAll() }, [])

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [lRes, pRes] = await Promise.all([lookbookApi.getAll(), productApi.getAll()])
      setLookbooks(Array.isArray(lRes.data) ? lRes.data : [])
      setProducts(Array.isArray(pRes.data) ? pRes.data : [])
    } catch { show('Failed to load data', 'error') }
    finally { setLoading(false) }
  }

  const openCreate = () => { setForm(EMPTY_FORM); setModal('create') }
  const openEdit = (lb) => {
    setForm({ title: lb.title, description: lb.description || '', season: lb.season || '', style: lb.style || '', coverImageUrl: lb.coverImageUrl || '' })
    setModal(lb)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      if (modal === 'create') { await lookbookApi.create(form); show('Lookbook created!') }
      else { await lookbookApi.update(modal.lookbookId, form); show('Lookbook updated!') }
      setModal(null); fetchAll()
    } catch (e) { show(e.response?.data?.message || 'Save failed', 'error') }
    finally { setSaving(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this lookbook?')) return
    try { await lookbookApi.delete(id); show('Deleted'); fetchAll() }
    catch (e) { show(e.response?.data?.message || 'Delete failed', 'error') }
  }

  const handlePublish = async (id) => {
    try { await lookbookApi.publish(id); show('Published!'); fetchAll() }
    catch (e) { show(e.response?.data?.message || 'Failed', 'error') }
  }

  const handleArchive = async (id) => {
    try { await lookbookApi.archive(id); show('Archived'); fetchAll() }
    catch (e) { show(e.response?.data?.message || 'Failed', 'error') }
  }

  const handleAddItem = async (e) => {
    e.preventDefault()
    if (!itemForm.productId) return show('Select a product', 'error')
    setAddingItem(true)
    try {
      await lookbookApi.addItem(itemModal.lookbookId, { ...itemForm, productId: Number(itemForm.productId), displayOrder: Number(itemForm.displayOrder) })
      show('Product added to lookbook')
      setItemModal(null); fetchAll()
    } catch (e) { show(e.response?.data?.message || 'Failed', 'error') }
    finally { setAddingItem(false) }
  }

  const handleRemoveItem = async (lookbookId, productId) => {
    try { await lookbookApi.removeItem(lookbookId, productId); show('Removed'); fetchAll() }
    catch (e) { show(e.response?.data?.message || 'Failed', 'error') }
  }

  const f = (k) => ({ value: form[k], onChange: e => setForm(p => ({ ...p, [k]: e.target.value })) })

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Content" title="Lookbooks" />
        <button onClick={openCreate} className="btn-primary">+ New Lookbook</button>
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : lookbooks.length === 0
          ? <EmptyState icon="◎" title="No lookbooks" subtitle="Create your first seasonal lookbook"
              action={<button onClick={openCreate} className="btn-primary">Create Lookbook</button>} />
          : <div className="grid gap-4">
              {lookbooks.map(lb => (
                <div key={lb.lookbookId} className="card p-5">
                  <div className="flex items-start gap-4">
                    {lb.coverImageUrl && (
                      <img src={lb.coverImageUrl} alt={lb.title} className="w-20 h-20 object-cover flex-shrink-0" />
                    )}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-3 mb-1 flex-wrap">
                        <h3 className="font-display text-lg text-charcoal-900">{lb.title}</h3>
                        <StatusBadge status={lb.status} />
                      </div>
                      <div className="flex gap-3 text-xs font-mono text-charcoal-700/50 mb-2 flex-wrap">
                        {lb.season && <span>Season: {lb.season}</span>}
                        {lb.style && <span>Style: {lb.style}</span>}
                        <span>{lb.items?.length || 0} items</span>
                        <span>{lb.savedCount || 0} saves</span>
                      </div>
                      {lb.description && <p className="text-sm text-charcoal-700/60 line-clamp-1">{lb.description}</p>}
                    </div>
                    <div className="flex flex-col gap-2 flex-shrink-0">
                      <div className="flex gap-2">
                        <button onClick={() => openEdit(lb)} className="text-xs font-mono text-gold-600 hover:underline">Edit</button>
                        <button onClick={() => setItemModal(lb)} className="text-xs font-mono text-blue-500 hover:underline">Items</button>
                        <button onClick={() => handleDelete(lb.lookbookId)} className="text-xs font-mono text-red-400 hover:text-red-600">Delete</button>
                      </div>
                      <div className="flex gap-2">
                        {lb.status !== 'PUBLISHED' && (
                          <button onClick={() => handlePublish(lb.lookbookId)} className="text-xs font-mono text-green-600 hover:underline">Publish</button>
                        )}
                        {lb.status === 'PUBLISHED' && (
                          <button onClick={() => handleArchive(lb.lookbookId)} className="text-xs font-mono text-charcoal-700/50 hover:underline">Archive</button>
                        )}
                      </div>
                    </div>
                  </div>

                  {/* Items preview */}
                  {lb.items?.length > 0 && (
                    <div className="mt-3 pt-3 border-t border-charcoal-900/8 flex gap-2 flex-wrap">
                      {lb.items.slice(0, 5).map(item => (
                        <div key={item.lookbookItemId} className="flex items-center gap-2 bg-cream-100 px-2 py-1">
                          <span className="text-xs text-charcoal-700/70">{item.productName}</span>
                          <button onClick={() => handleRemoveItem(lb.lookbookId, item.productId)}
                            className="text-red-400 hover:text-red-600 text-xs">✕</button>
                        </div>
                      ))}
                      {lb.items.length > 5 && <span className="text-xs text-charcoal-700/40">+{lb.items.length - 5} more</span>}
                    </div>
                  )}
                </div>
              ))}
            </div>
      }

      {/* Create/Edit Modal */}
      {modal && (
        <Modal title={modal === 'create' ? 'New Lookbook' : `Edit — ${modal.title}`} onClose={() => setModal(null)} wide>
          <form onSubmit={handleSave} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <FormField label="Title *"><input className="input-field" placeholder="Summer Breeze 2025" {...f('title')} required /></FormField>
              <FormField label="Season"><input className="input-field" placeholder="Summer 2025" {...f('season')} /></FormField>
              <FormField label="Style"><input className="input-field" placeholder="Casual, Boho, Formal…" {...f('style')} /></FormField>
              <FormField label="Cover Image URL"><input className="input-field" placeholder="https://…" {...f('coverImageUrl')} /></FormField>
            </div>
            <FormField label="Description">
              <textarea className="input-field resize-none" rows={3} placeholder="About this lookbook…" {...f('description')} />
            </FormField>
            <button type="submit" disabled={saving} className="btn-primary w-full flex items-center justify-center gap-2">
              {saving ? <><Spinner size="sm" /> Saving…</> : (modal === 'create' ? 'Create Lookbook' : 'Save Changes')}
            </button>
          </form>
        </Modal>
      )}

      {/* Add Item Modal */}
      {itemModal && (
        <Modal title={`Add Product to "${itemModal.title}"`} onClose={() => setItemModal(null)}>
          <form onSubmit={handleAddItem} className="space-y-4">
            <FormField label="Product">
              <select className="input-field" value={itemForm.productId} onChange={e => setItemForm(f => ({ ...f, productId: e.target.value }))}>
                <option value="">Select product…</option>
                {products.map(p => <option key={p.productId} value={p.productId}>{p.name} — ₹{p.price?.toLocaleString()}</option>)}
              </select>
            </FormField>
            <FormField label="Styling Note">
              <input className="input-field" placeholder="e.g. Pair with white sneakers" value={itemForm.stylingNote}
                onChange={e => setItemForm(f => ({ ...f, stylingNote: e.target.value }))} />
            </FormField>
            <FormField label="Display Order">
              <input type="number" className="input-field" value={itemForm.displayOrder}
                onChange={e => setItemForm(f => ({ ...f, displayOrder: e.target.value }))} />
            </FormField>
            <button type="submit" disabled={addingItem} className="btn-primary w-full flex items-center justify-center gap-2">
              {addingItem ? <><Spinner size="sm" /> Adding…</> : 'Add to Lookbook'}
            </button>
          </form>
        </Modal>
      )}
    </div>
  )
}
