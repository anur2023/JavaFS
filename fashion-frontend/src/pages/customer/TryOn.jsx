import { useState, useEffect } from 'react'
import { tryOnApi } from '../../api/modules'
import { PageHeader, EmptyState, useToast, Spinner, StatusBadge } from '../../components/ui'
import { Link } from 'react-router-dom'

export default function TryOn() {
  const [sessions, setSessions] = useState([])
  const [loading, setLoading] = useState(true)
  const [deletingId, setDeletingId] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchHistory() }, [])

  const fetchHistory = async () => {
    setLoading(true)
    try {
      const { data } = await tryOnApi.getHistory()
      setSessions(Array.isArray(data) ? data : [])
    } catch { show('Failed to load try-on history', 'error') }
    finally { setLoading(false) }
  }

  const handleDelete = async (id) => {
    if (!confirm('Remove this session?')) return
    setDeletingId(id)
    try {
      await tryOnApi.deleteSession(id)
      setSessions(prev => prev.filter(s => s.sessionId !== id))
      show('Session removed')
    } catch { show('Failed to remove session', 'error') }
    finally { setDeletingId(null) }
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="AR Feature" title="Virtual Try-On History"
          subtitle="All your try-on sessions are saved here" />
        <Link to="/customer/products" className="btn-outline text-sm">Try on a product →</Link>
      </div>

      {/* Info banner */}
      <div className="bg-gold-400/10 border border-gold-400/30 px-5 py-4 mb-8 flex gap-3">
        <span className="text-gold-500 text-xl">◈</span>
        <div>
          <p className="text-sm font-medium text-charcoal-900 mb-0.5">How it works</p>
          <p className="text-xs text-charcoal-700/70">Go to any product, click the <strong>AR</strong> button, enter your photo URL, and we'll overlay the outfit virtually. Results appear here.</p>
        </div>
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : sessions.length === 0
          ? <EmptyState icon="◈" title="No try-on sessions yet"
              subtitle="Visit the shop and click the AR button on any product"
              action={<Link to="/customer/products" className="btn-primary">Go to Shop</Link>} />
          : <div className="space-y-4">
              {sessions.map(s => (
                <div key={s.sessionId} className="card p-5 flex gap-4">
                  {/* User image preview */}
                  <div className="w-16 h-16 bg-cream-100 flex-shrink-0 overflow-hidden">
                    <img src={s.userImageUrl} alt="User" className="w-full h-full object-cover"
                      onError={e => { e.target.style.display = 'none' }} />
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <h4 className="font-display text-base text-charcoal-900">{s.productName}</h4>
                      <StatusBadge status={s.status} />
                    </div>
                    <p className="section-label">{s.productCategory}</p>
                    {s.resultImageUrl && s.status === 'COMPLETED' && (
                      <p className="text-xs font-mono text-gold-600 mt-1 truncate">
                        Result: <a href={s.resultImageUrl} target="_blank" rel="noreferrer" className="underline hover:text-gold-500">View result image</a>
                      </p>
                    )}
                    {s.apiMessage && <p className="text-xs text-charcoal-700/50 mt-1">{s.apiMessage}</p>}
                    <p className="text-xs text-charcoal-700/40 mt-1">
                      {new Date(s.createdAt).toLocaleString('en-IN', { day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })}
                    </p>
                  </div>

                  <button onClick={() => handleDelete(s.sessionId)} disabled={deletingId === s.sessionId}
                    className="text-xs font-mono text-red-400 hover:text-red-600 transition-colors self-start flex-shrink-0 disabled:opacity-40">
                    {deletingId === s.sessionId ? '…' : 'Remove'}
                  </button>
                </div>
              ))}
            </div>
      }
    </div>
  )
}
