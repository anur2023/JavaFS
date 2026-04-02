import { useState, useEffect } from 'react'
import { inspirationApi } from '../../api/modules'
import { PageHeader, EmptyState, useToast, Spinner, Modal } from '../../components/ui'

export default function Inspiration() {
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [detail, setDetail] = useState(null)
  const { show, ToastEl } = useToast()

  useEffect(() => {
    inspirationApi.getAll()
      .then(({ data }) => setPosts(Array.isArray(data) ? data : []))
      .catch(() => show('Failed to load inspiration feed', 'error'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <PageHeader label="Fashion Feed" title="Inspiration" subtitle="Stories, trends, and style insights from our editors" />

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : posts.length === 0
          ? <EmptyState icon="✦" title="No posts yet" subtitle="Check back soon for fresh inspiration" />
          : <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {posts.map((post, i) => (
                <div key={post.feedId}
                  onClick={() => setDetail(post)}
                  className={`card overflow-hidden group cursor-pointer ${i === 0 ? 'md:col-span-2 md:row-span-2' : ''}`}>
                  <div className={`relative bg-charcoal-900 overflow-hidden ${i === 0 ? 'h-72 md:h-80' : 'h-48'}`}>
                    {post.imageUrl
                      ? <img src={post.imageUrl} alt={post.title} className="w-full h-full object-cover opacity-75 group-hover:opacity-90 group-hover:scale-105 transition-all duration-500" />
                      : <div className="w-full h-full flex items-center justify-center">
                          <span className="font-display text-6xl italic text-cream-200/10">É</span>
                        </div>
                    }
                    <div className="absolute inset-0 bg-gradient-to-t from-charcoal-900/80 via-charcoal-900/20 to-transparent" />
                    <div className="absolute bottom-0 left-0 right-0 p-5">
                      <h3 className={`font-display text-cream-50 leading-snug ${i === 0 ? 'text-2xl' : 'text-base'}`}>{post.title}</h3>
                      <p className="text-xs font-mono text-cream-200/50 mt-1">
                        {new Date(post.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' })}
                      </p>
                    </div>
                  </div>
                  {post.contentText && (
                    <div className="p-4">
                      <p className="text-sm text-charcoal-700/70 line-clamp-2">{post.contentText}</p>
                      <p className="text-xs font-mono text-gold-600 mt-2 group-hover:underline">Read more →</p>
                    </div>
                  )}
                </div>
              ))}
            </div>
      }

      {detail && (
        <Modal title={detail.title} onClose={() => setDetail(null)} wide>
          <div className="space-y-4">
            {detail.imageUrl && (
              <img src={detail.imageUrl} alt={detail.title} className="w-full h-56 object-cover" />
            )}
            <p className="text-xs font-mono text-charcoal-700/40">
              {new Date(detail.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' })}
            </p>
            <p className="text-sm text-charcoal-700/80 leading-relaxed whitespace-pre-wrap">{detail.contentText}</p>
          </div>
        </Modal>
      )}
    </div>
  )
}
