// ── Spinner ──────────────────────────────────────────────────────────────────
export function Spinner({ size = 'md' }) {
  const s = size === 'sm' ? 'h-4 w-4' : size === 'lg' ? 'h-10 w-10' : 'h-7 w-7'
  return (
    <div className={`${s} border-2 border-gold-400/30 border-t-gold-500 rounded-full animate-spin`} />
  )
}

// ── Full page loader ──────────────────────────────────────────────────────────
export function PageLoader() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-cream-50 gap-4">
      <div className="font-display text-2xl text-charcoal-900 italic">ÉLUME</div>
      <Spinner size="lg" />
    </div>
  )
}

// ── Toast ─────────────────────────────────────────────────────────────────────
import { useState, useEffect } from 'react'

export function Toast({ message, type = 'success', onClose }) {
  useEffect(() => {
    const t = setTimeout(onClose, 3500)
    return () => clearTimeout(t)
  }, [onClose])

  const colors = {
    success: 'bg-charcoal-900 text-cream-50',
    error: 'bg-red-600 text-white',
    info: 'bg-gold-500 text-white',
  }

  return (
    <div className={`fixed bottom-6 right-6 z-50 px-5 py-3 text-sm font-body font-medium shadow-xl animate-fade-up ${colors[type]}`}>
      {message}
    </div>
  )
}

// ── useToast hook ─────────────────────────────────────────────────────────────
export function useToast() {
  const [toast, setToast] = useState(null)
  const show = (message, type = 'success') => setToast({ message, type })
  const hide = () => setToast(null)
  const ToastEl = toast ? <Toast message={toast.message} type={toast.type} onClose={hide} /> : null
  return { show, ToastEl }
}

// ── EmptyState ────────────────────────────────────────────────────────────────
export function EmptyState({ icon = '✦', title, subtitle, action }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 text-center">
      <div className="text-4xl mb-4 text-gold-400">{icon}</div>
      <h3 className="font-display text-xl text-charcoal-900 mb-2">{title}</h3>
      {subtitle && <p className="text-sm text-charcoal-700/60 mb-6 max-w-xs">{subtitle}</p>}
      {action}
    </div>
  )
}

// ── Modal ─────────────────────────────────────────────────────────────────────
export function Modal({ title, children, onClose, wide }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-charcoal-900/60 backdrop-blur-sm animate-fade-in">
      <div className={`bg-cream-50 shadow-2xl w-full ${wide ? 'max-w-2xl' : 'max-w-md'} animate-fade-up`}>
        <div className="flex items-center justify-between px-6 py-4 border-b border-charcoal-900/10">
          <h3 className="font-display text-lg text-charcoal-900">{title}</h3>
          <button onClick={onClose} className="text-charcoal-700/50 hover:text-charcoal-900 text-xl leading-none">&times;</button>
        </div>
        <div className="px-6 py-5">{children}</div>
      </div>
    </div>
  )
}

// ── SkeletonCard ──────────────────────────────────────────────────────────────
export function SkeletonCard() {
  return (
    <div className="card overflow-hidden">
      <div className="skeleton h-56 w-full" />
      <div className="p-4 space-y-2">
        <div className="skeleton h-4 w-3/4 rounded" />
        <div className="skeleton h-3 w-1/2 rounded" />
      </div>
    </div>
  )
}

// ── ProductCard ───────────────────────────────────────────────────────────────
export function ProductCard({ product, onAddToWishlist, onTryOn, onClick }) {
  const img = product.images?.[0]?.imageUrl || product.productImageUrl
  return (
    <div className="card group cursor-pointer overflow-hidden" onClick={onClick}>
      <div className="relative overflow-hidden bg-cream-100 h-56">
        {img
          ? <img src={img} alt={product.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
          : <div className="w-full h-full flex items-center justify-center text-charcoal-700/20 font-display text-5xl italic">É</div>
        }
        {product.stockQuantity === 0 && (
          <div className="absolute inset-0 bg-charcoal-900/40 flex items-center justify-center">
            <span className="text-cream-50 text-xs font-mono tracking-widest uppercase">Sold Out</span>
          </div>
        )}
        <div className="absolute top-3 right-3 flex flex-col gap-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
          {onAddToWishlist && (
            <button onClick={e => { e.stopPropagation(); onAddToWishlist(product) }}
              className="w-8 h-8 bg-white/90 flex items-center justify-center text-charcoal-900 hover:bg-gold-400 hover:text-white transition-colors text-sm shadow">♡</button>
          )}
          {onTryOn && (
            <button onClick={e => { e.stopPropagation(); onTryOn(product) }}
              className="w-8 h-8 bg-white/90 flex items-center justify-center text-charcoal-900 hover:bg-charcoal-900 hover:text-cream-50 transition-colors text-xs shadow">AR</button>
          )}
        </div>
      </div>
      <div className="p-4">
        <p className="section-label">{product.category || product.productCategory}</p>
        <h4 className="font-display text-base text-charcoal-900 mb-1 leading-snug">{product.name || product.productName}</h4>
        <p className="font-mono text-sm text-gold-600 font-medium">₹{(product.price || product.productPrice)?.toLocaleString()}</p>
      </div>
    </div>
  )
}

// ── StatusBadge ───────────────────────────────────────────────────────────────
export function StatusBadge({ status }) {
  const map = {
    PENDING: 'badge-gold', CONFIRMED: 'badge-blue', SHIPPED: 'badge-blue',
    DELIVERED: 'badge-green', CANCELLED: 'badge-red',
    COMPLETED: 'badge-green', FAILED: 'badge-red',
    PUBLISHED: 'badge-green', DRAFT: 'badge-gold', ARCHIVED: 'badge-red',
  }
  return <span className={map[status] || 'badge bg-gray-100 text-gray-600'}>{status}</span>
}

// ── PageHeader ────────────────────────────────────────────────────────────────
export function PageHeader({ label, title, subtitle }) {
  return (
    <div className="mb-8">
      {label && <p className="section-label">{label}</p>}
      <h1 className="font-display text-3xl md:text-4xl text-charcoal-900">{title}</h1>
      {subtitle && <p className="text-charcoal-700/60 mt-2 text-sm">{subtitle}</p>}
    </div>
  )
}

// ── FormField ─────────────────────────────────────────────────────────────────
export function FormField({ label, error, children }) {
  return (
    <div>
      {label && <label className="block text-xs font-mono tracking-wider uppercase text-charcoal-700/70 mb-1.5">{label}</label>}
      {children}
      {error && <p className="mt-1 text-xs text-red-500">{error}</p>}
    </div>
  )
}
