import { useState, useEffect } from 'react'
import { inspirationApi } from '../../api/modules'
import { PageHeader, useToast, Spinner, Modal, FormField, EmptyState } from '../../components/ui'

const EMPTY = { title: '', contentText: '', imageUrl: '' }

export default function AdminInspiration() {
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(EMPTY)
  const [saving, setSaving] = useState(false)
  const [deletingId, setDeletingId] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetch() }, [])

  const fetch = async () => {
    setLoading(true)
    try { const { data } = await inspirationApi.getAll(); setPosts(Array.isArray(data) ? data : []) }
    catch { show('Failed to load posts', 'error') }
    finally { setLoading(false) }
  }

  const openCreate = () => { setForm(EMPTY); setModal('create') }
  const openEdit = (p) => { setForm({ title: p.title, contentText: p.contentText || '', imageUrl: p.imageUrl || '' }); setModal(p) }

  const handleSave = async (e) => {
    e.preventDefault()
    if (!form.title) return show('Title required', 'error')
    setSaving(true)
    try {
      if (modal === 'create') { await inspirationApi.create(form); show('Post created!') }
      else { await inspirationApi.update(modal.feedId, form); show('Post updated!') }
      setModal(null); fetch()
    } catch (e) { show(e.response?.data?.message || 'Save failed', 'error') }
    finally { setSaving(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this post?')) return
    setDeletingId(id)
    try { await inspirationApi.delete(id); setPosts(prev => prev.filter(p => p.feedId !== id)); show('Deleted') }
    catch (e) { show(e.response?.data?.message || 'Delete failed', 'error') }
    finally { setDeletingId(null) }
  }

  const f = (k) => ({ value: form[k], onChange: e => setForm(p => ({ ...p, [k]: e.target.value })) })

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Content Management" title="Inspiration Feed" />
        <button onClick={openCreate} className="btn-primary">+ New Post</button>
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : posts.length === 0
          ? <EmptyState icon="✦" title="No posts" subtitle="Create your first inspiration post"
              action={<button onClick={openCreate} className="btn-primary">Create Post</button>} />
          : <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
              {posts.map(p => (
                <div key={p.feedId} className="card overflow-hidden">
                  {p.imageUrl && <img src={p.imageUrl} alt={p.title} className="w-full h-40 object-cover" />}
                  <div className="p-4">
                    <h3 className="font-display text-base text-charcoal-900 mb-1">{p.title}</h3>
                    {p.contentText && <p className="text-xs text-charcoal-700/60 line-clamp-3 mb-3">{p.contentText}</p>}
                    <p className="text-xs font-mono text-charcoal-700/40 mb-3">
                      {new Date(p.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                    </p>
                    <div className="flex gap-3">
                      <button onClick={() => openEdit(p)} className="text-xs font-mono text-gold-600 hover:underline">Edit</button>
                      <button onClick={() => handleDelete(p.feedId)} disabled={deletingId === p.feedId}
                        className="text-xs font-mono text-red-400 hover:text-red-600 disabled:opacity-40">
                        {deletingId === p.feedId ? '…' : 'Delete'}
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
      }

      {modal && (
        <Modal title={modal === 'create' ? 'New Post' : `Edit — ${modal.title}`} onClose={() => setModal(null)} wide>
          <form onSubmit={handleSave} className="space-y-4">
            <FormField label="Title *"><input className="input-field" placeholder="e.g. Spring Colour Trends 2025" {...f('title')} required /></FormField>
            <FormField label="Image URL"><input className="input-field" placeholder="https://…" {...f('imageUrl')} /></FormField>
            <FormField label="Content">
              <textarea className="input-field resize-none" rows={6} placeholder="Write your fashion story…" {...f('contentText')} />
            </FormField>
            <button type="submit" disabled={saving} className="btn-primary w-full flex items-center justify-center gap-2">
              {saving ? <><Spinner size="sm" /> Saving…</> : (modal === 'create' ? 'Publish Post' : 'Save Changes')}
            </button>
          </form>
        </Modal>
      )}
    </div>
  )
}
