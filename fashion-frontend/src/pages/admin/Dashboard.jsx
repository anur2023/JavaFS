import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { productApi } from '../../api/product'
import { orderApi } from '../../api/modules'
import { Spinner } from '../../components/ui'

export default function AdminDashboard() {
  const [stats, setStats] = useState(null)
  const [recentOrders, setRecentOrders] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([productApi.getAll(), orderApi.getAll()])
      .then(([pRes, oRes]) => {
        const products = Array.isArray(pRes.data) ? pRes.data : []
        const orders = Array.isArray(oRes.data) ? oRes.data : []
        setStats({
          products: products.length,
          orders: orders.length,
          revenue: orders.reduce((sum, o) => sum + (o.totalAmount || 0), 0),
          pending: orders.filter(o => o.status === 'PENDING').length,
        })
        setRecentOrders(orders.slice(0, 5))
      })
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [])

  const modules = [
    { label: 'Products', to: '/admin/products', icon: '◻', desc: 'Add, edit, delete products' },
    { label: 'Orders', to: '/admin/orders', icon: '◈', desc: 'Manage & update order status' },
    { label: 'Lookbooks', to: '/admin/lookbooks', icon: '◎', desc: 'Create seasonal lookbooks' },
    { label: 'Inspiration', to: '/admin/inspiration', icon: '✦', desc: 'Post fashion content' },
    { label: 'Style Quiz', to: '/admin/quiz', icon: '◈', desc: 'Manage quiz questions' },
  ]

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      <div className="mb-8">
        <p className="section-label">Control Panel</p>
        <h1 className="font-display text-4xl text-charcoal-900">Admin Dashboard</h1>
      </div>

      {loading
        ? <div className="flex justify-center py-10"><Spinner size="lg" /></div>
        : <>
            {/* Stats */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-10">
              {[
                { label: 'Products', value: stats?.products ?? 0, color: 'text-charcoal-900' },
                { label: 'Total Orders', value: stats?.orders ?? 0, color: 'text-charcoal-900' },
                { label: 'Revenue', value: `₹${(stats?.revenue || 0).toLocaleString()}`, color: 'text-gold-600' },
                { label: 'Pending Orders', value: stats?.pending ?? 0, color: 'text-orange-600' },
              ].map(s => (
                <div key={s.label} className="card p-5">
                  <p className="text-xs font-mono text-charcoal-700/50 uppercase tracking-wider mb-2">{s.label}</p>
                  <p className={`font-display text-3xl ${s.color}`}>{s.value}</p>
                </div>
              ))}
            </div>

            <div className="grid md:grid-cols-2 gap-8">
              {/* Module shortcuts */}
              <div>
                <p className="section-label mb-4">Modules</p>
                <div className="space-y-2">
                  {modules.map(m => (
                    <Link key={m.to} to={m.to} className="card flex items-center gap-4 p-4 group hover:border-gold-400/40 transition-colors">
                      <span className="text-xl text-gold-400">{m.icon}</span>
                      <div className="flex-1">
                        <p className="font-medium text-charcoal-900 text-sm">{m.label}</p>
                        <p className="text-xs text-charcoal-700/50">{m.desc}</p>
                      </div>
                      <span className="text-charcoal-700/30 group-hover:text-gold-500 transition-colors">→</span>
                    </Link>
                  ))}
                </div>
              </div>

              {/* Recent orders */}
              <div>
                <div className="flex items-center justify-between mb-4">
                  <p className="section-label">Recent Orders</p>
                  <Link to="/admin/orders" className="text-xs font-mono text-gold-600 hover:underline">View all →</Link>
                </div>
                <div className="space-y-2">
                  {recentOrders.length === 0
                    ? <p className="text-sm text-charcoal-700/50 py-4 text-center">No orders yet</p>
                    : recentOrders.map(o => (
                        <div key={o.orderId} className="card p-3 flex items-center gap-3">
                          <div className="flex-1 min-w-0">
                            <p className="text-sm font-medium text-charcoal-900">Order #{o.orderId}</p>
                            <p className="text-xs text-charcoal-700/50 truncate">{o.shippingAddress}</p>
                          </div>
                          <div className="text-right flex-shrink-0">
                            <p className="font-mono text-sm text-gold-600">₹{o.totalAmount?.toLocaleString()}</p>
                            <span className={`text-xs font-mono ${
                              o.status === 'PENDING' ? 'text-orange-500' :
                              o.status === 'DELIVERED' ? 'text-green-600' :
                              o.status === 'CANCELLED' ? 'text-red-500' : 'text-blue-500'
                            }`}>{o.status}</span>
                          </div>
                        </div>
                      ))
                  }
                </div>
              </div>
            </div>
          </>
      }
    </div>
  )
}
