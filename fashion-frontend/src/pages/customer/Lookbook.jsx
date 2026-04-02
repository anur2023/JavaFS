import { useState, useEffect } from 'react'
import { lookbookApi } from '../../api/modules'
import { PageHeader, EmptyState, useToast, Spinner, Modal } from '../../components/ui'

export default function Lookbook() {
  const [lookbooks, setLookbooks] = useState([])
  const [saved, setSaved] = useState([])
  const [loading, setLoading] = useState(true)
  const [tab, setTab] = useState('all') // 'all' | 'saved'
  const [detail, setDetail] = useState(null)
  const [savingId, setSavingId] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchAll() }, [])

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [pubRes, savedRes] = await Promise.all([
        lookbookApi.getPublished(),
        lookbookApi.getSaved(),
      ])
      setLookbooks(Array.isArray(pubRes.data) ? pubRes.data : [])
      setSaved(Array.isArray(savedRes.data) ? savedRes.data : [])
    } catch { show('Failed to load lookbooks', 'error') }
    finally { setLoading(false) }
  }

  const isSaved = (id) => saved.some(s => s.lookbookId === id)

  const handleToggleSave = async (lb) => {
    setSavingId(lb.lookbookId)
    try {
      if (isSaved(lb.lookbookId)) {
        await lookbookApi.unsave(lb.lookbookId)
        setSaved(prev => prev.filter(s => s.lookbookId !== lb.lookbookId))
        show('Removed from saved')
      } else {
        await lookbookApi.save(lb.lookbookId)
        setSaved(prev => [...prev, lb])
        show('Lookbook saved!')
      }
    } catch (e) { show(e.response?.data?.message || 'Action failed', 'error') }
    finally { setSavingId(null) }
  }

  const displayed = tab === 'saved' ? saved : lookbooks

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <PageHeader label="Seasonal Curation" title="Lookbook" subtitle="Editorial style inspiration, curated each season" />

      {/* Tabs */}
      <div className="flex gap-1 mb-8 border-b border-charcoal-900/10">
        {[['all', 'All Lookbooks'], ['saved', 'My Saved']].map(([key, label]) => (
          <button key={key} onClick={() => setTab(key)}
            className={`px-5 py-2.5 text-sm font-body font-medium transition-all duration-150 border-b-2 -mb-px ${
              tab === key ? 'border-gold-500 text-charcoal-900' : 'border-transparent text-charcoal-700/50 hover:text-charcoal-900'
            }`}>{label}
            {key === 'saved' && saved.length > 0 && <span className="ml-1.5 font-mono text-xs text-gold-500">({saved.length})</span>}
          </button>
        ))}
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : displayed.length === 0
          ? <EmptyState icon="◎"
              title={tab === 'saved' ? 'No saved lookbooks' : 'No lookbooks available'}
              subtitle={tab === 'saved' ? 'Browse lookbooks and bookmark your favourites' : 'Check back soon for seasonal collections'} />
          : <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {displayed.map(lb => (
                <div key={lb.lookbookId} className="card overflow-hidden group cursor-pointer" onClick={() => setDetail(lb)}>
                  {/* Cover */}
                  <div className="relative h-52 bg-charcoal-900 overflow-hidden">
                    {lb.coverImageUrl
                      ? <img src={lb.coverImageUrl} alt={lb.title} className="w-full h-full object-cover opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-500" />
                      : <div className="w-full h-full flex items-center justify-center">
                          <span className="font-display text-5xl italic text-cream-200/20">É</span>
                        </div>
                    }
                    <div className="absolute inset-0 bg-gradient-to-t from-charcoal-900/60 to-transparent" />
                    <div className="absolute bottom-4 left-4 right-4">
                      <p className="section-label text-gold-400">{lb.season}</p>
                      <h3 className="font-display text-lg text-cream-50 leading-snug">{lb.title}</h3>
                    </div>
                    <button
                      onClick={e => { e.stopPropagation(); handleToggleSave(lb) }}
                      disabled={savingId === lb.lookbookId}
                      className={`absolute top-3 right-3 w-8 h-8 flex items-center justify-center text-sm transition-colors ${
                        isSaved(lb.lookbookId) ? 'bg-gold-500 text-white' : 'bg-white/90 text-charcoal-900 hover:bg-gold-400 hover:text-white'
                      }`}
                    >
                      {savingId === lb.lookbookId ? '…' : isSaved(lb.lookbookId) ? '♥' : '♡'}
                    </button>
                  </div>
                  <div className="p-4">
                    {lb.style && <span className="badge-gold mr-2">{lb.style}</span>}
                    {lb.description && <p className="text-xs text-charcoal-700/60 mt-2 line-clamp-2">{lb.description}</p>}
                    <p className="text-xs font-mono text-charcoal-700/40 mt-2">
                      {lb.items?.length || 0} pieces · {lb.savedCount || 0} saves
                    </p>
                  </div>
                </div>
              ))}
            </div>
      }

      {/* Detail Modal */}
      {detail && (
        <Modal title={detail.title} onClose={() => setDetail(null)} wide>
          <div className="space-y-4">
            {detail.coverImageUrl && (
              <img src={detail.coverImageUrl} alt={detail.title} className="w-full h-48 object-cover" />
            )}
            <div className="flex gap-2 flex-wrap">
              {detail.season && <span className="badge-gold">{detail.season}</span>}
              {detail.style && <span className="badge bg-blush-200 text-charcoal-700">{detail.style}</span>}
            </div>
            {detail.description && <p className="text-sm text-charcoal-700/70">{detail.description}</p>}

            {detail.items?.length > 0 && (
              <div>
                <p className="section-label mb-3">Featured Pieces ({detail.items.length})</p>
                <div className="grid grid-cols-2 gap-3">
                  {detail.items.map(item => (
                    <div key={item.lookbookItemId} className="flex gap-3 bg-cream-50 p-3 border border-charcoal-900/5">
                      <div className="w-12 h-12 bg-cream-100 flex-shrink-0 overflow-hidden">
                        {item.productImageUrl
                          ? <img src={item.productImageUrl} alt={item.productName} className="w-full h-full object-cover" />
                          : <span className="w-full h-full flex items-center justify-center font-display italic text-charcoal-700/20">É</span>
                        }
                      </div>
                      <div className="min-w-0">
                        <p className="text-xs font-medium text-charcoal-900 truncate">{item.productName}</p>
                        <p className="text-xs text-charcoal-700/50">{item.productCategory}</p>
                        <p className="text-xs font-mono text-gold-600">₹{item.productPrice?.toLocaleString()}</p>
                        {item.stylingNote && <p className="text-xs text-charcoal-700/40 italic mt-0.5">{item.stylingNote}</p>}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            <button onClick={() => handleToggleSave(detail)}
              disabled={savingId === detail.lookbookId}
              className={isSaved(detail.lookbookId) ? 'btn-outline w-full' : 'btn-primary w-full'}>
              {savingId === detail.lookbookId ? '…' : isSaved(detail.lookbookId) ? '♥ Saved' : '♡ Save Lookbook'}
            </button>
          </div>
        </Modal>
      )}
    </div>
  )
}
