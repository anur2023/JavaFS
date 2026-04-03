import { Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function CustomerHome() {
  const { user } = useAuth()
  const firstName = user?.email?.split('@')[0] || 'there'

  const features = [
    { icon: '✦', label: 'Shop', to: '/customer/products', desc: 'Browse our curated collection', color: 'bg-blush-100' },
    { icon: '◈', label: 'Virtual Try-On', to: '/customer/tryon', desc: 'See how it looks on you', color: 'bg-cream-200' },
    { icon: '◎', label: 'Lookbook', to: '/customer/lookbook', desc: 'Seasonal style inspiration', color: 'bg-gold-400/10' },
    { icon: '♡', label: 'Wishlist', to: '/customer/wishlist', desc: 'Your saved favourites', color: 'bg-blush-100' },
    { icon: '◻', label: 'My Orders', to: '/customer/orders', desc: 'Track & manage orders', color: 'bg-cream-200' },
    { icon: '◈', label: 'Style Quiz', to: '/customer/quiz', desc: 'Find your personal style', color: 'bg-gold-400/10' },
  ]

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {/* Hero */}
      <div className="relative bg-charcoal-900 text-cream-50 px-8 py-16 mb-12 overflow-hidden">
        <div className="absolute top-0 right-0 w-64 h-64 bg-gold-500/10 rounded-full -translate-y-1/2 translate-x-1/4" />
        <div className="absolute bottom-0 left-1/2 w-32 h-32 bg-blush-200/5 rounded-full translate-y-1/2" />
        <div className="relative z-10">
          <p className="section-label text-gold-400">Welcome back</p>
          <h1 className="font-display text-4xl md:text-5xl italic mb-4">
            Hello, {firstName}
          </h1>
          <p className="text-cream-200/60 text-sm max-w-md mb-8">
            Your personal fashion platform — shop, try-on virtually, save favourites, and discover your style.
          </p>
          <Link to="/customer/products" className="btn-gold inline-block">Explore Collection →</Link>
        </div>
      </div>

      {/* Feature grid */}
      <p className="section-label mb-4">Quick Access</p>
      <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
        {features.map(f => (
          <Link key={f.to} to={f.to}
            className={`${f.color} p-6 group hover:shadow-md transition-all duration-200 border border-charcoal-900/5`}>
            <div className="text-2xl text-gold-500 mb-3">{f.icon}</div>
            <h3 className="font-display text-lg text-charcoal-900 mb-1">{f.label}</h3>
            <p className="text-xs text-charcoal-700/60">{f.desc}</p>
            <div className="mt-4 text-xs font-mono text-gold-600 group-hover:underline">Explore →</div>
          </Link>
        ))}
      </div>
    </div>
  )
}
