import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useState } from 'react'

export default function Navbar() {
  const { user, logout, isAdmin, isCustomer } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [mobileOpen, setMobileOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const customerLinks = [
    { to: '/customer', label: 'Home' },
    { to: '/customer/products', label: 'Shop' },
    { to: '/customer/lookbook', label: 'Lookbook' },
    { to: '/customer/inspiration', label: 'Inspire' },
    { to: '/customer/quiz', label: 'Style Quiz' },
  ]

  const adminLinks = [
    { to: '/admin', label: 'Dashboard' },
    { to: '/admin/products', label: 'Products' },
    { to: '/admin/orders', label: 'Orders' },
    { to: '/admin/lookbooks', label: 'Lookbooks' },
    { to: '/admin/inspiration', label: 'Inspiration' },
    { to: '/admin/quiz', label: 'Quiz' },
  ]

  const links = isAdmin ? adminLinks : customerLinks

  if (!user) return null

  return (
    <nav className="sticky top-0 z-40 bg-cream-50/95 backdrop-blur-md border-b border-charcoal-900/8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to={isAdmin ? '/admin' : '/customer'} className="font-display text-2xl italic text-charcoal-900 tracking-tight">
            ÉLUME
          </Link>

          {/* Desktop links */}
          <div className="hidden md:flex items-center gap-8">
            {links.map(l => (
              <Link key={l.to} to={l.to}
                className={`nav-link ${location.pathname === l.to ? 'text-gold-500 font-semibold' : ''}`}>
                {l.label}
              </Link>
            ))}
          </div>

          {/* Right side */}
          <div className="hidden md:flex items-center gap-4">
            {isAdmin && (
              <span className="badge-gold text-xs font-mono px-2 py-1">ADMIN</span>
            )}
            {isCustomer && (
              <>
                <Link to="/customer/wishlist" className="nav-link">♡ Wishlist</Link>
                <Link to="/customer/orders" className="nav-link">Orders</Link>
                <Link to="/customer/tryon" className="nav-link text-gold-600">Try-On</Link>
              </>
            )}
            <div className="h-4 w-px bg-charcoal-900/20" />
            <span className="text-xs font-mono text-charcoal-700/50">{user.email}</span>
            <button onClick={handleLogout} className="text-xs font-mono text-charcoal-700/60 hover:text-red-500 transition-colors uppercase tracking-wider">
              Sign Out
            </button>
          </div>

          {/* Mobile toggle */}
          <button className="md:hidden text-charcoal-900 p-2" onClick={() => setMobileOpen(!mobileOpen)}>
            {mobileOpen ? '✕' : '☰'}
          </button>
        </div>

        {/* Mobile menu */}
        {mobileOpen && (
          <div className="md:hidden border-t border-charcoal-900/8 py-4 space-y-3 animate-fade-in">
            {links.map(l => (
              <Link key={l.to} to={l.to} onClick={() => setMobileOpen(false)}
                className="block nav-link py-1">{l.label}</Link>
            ))}
            {isCustomer && (
              <>
                <Link to="/customer/wishlist" onClick={() => setMobileOpen(false)} className="block nav-link py-1">Wishlist</Link>
                <Link to="/customer/orders" onClick={() => setMobileOpen(false)} className="block nav-link py-1">Orders</Link>
                <Link to="/customer/tryon" onClick={() => setMobileOpen(false)} className="block nav-link py-1 text-gold-600">Try-On</Link>
              </>
            )}
            <button onClick={handleLogout} className="block text-sm text-red-500 py-1">Sign Out</button>
          </div>
        )}
      </div>
    </nav>
  )
}
