import { useState, useEffect } from 'react'
import { orderApi } from '../../api/modules'
import { PageHeader, useToast, Spinner, StatusBadge, Modal, FormField, EmptyState } from '../../components/ui'

const STATUSES = ['ALL', 'PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED']

export default function AdminOrders() {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState('ALL')
  const [updateModal, setUpdateModal] = useState(null)
  const [updateForm, setUpdateForm] = useState({ status: '', trackingNumber: '' })
  const [updating, setUpdating] = useState(false)
  const { show, ToastEl } = useToast()

  useEffect(() => { fetchOrders() }, [filter])

  const fetchOrders = async () => {
    setLoading(true)
    try {
      const { data } = await orderApi.getAll(filter === 'ALL' ? null : filter)
      setOrders(Array.isArray(data) ? data : [])
    } catch { show('Failed to load orders', 'error') }
    finally { setLoading(false) }
  }

  const openUpdate = (order) => {
    setUpdateForm({ status: order.status, trackingNumber: order.trackingNumber || '' })
    setUpdateModal(order)
  }

  const handleUpdate = async (e) => {
    e.preventDefault()
    setUpdating(true)
    try {
      await orderApi.updateStatus(updateModal.orderId, updateForm)
      show('Order updated!')
      setUpdateModal(null)
      fetchOrders()
    } catch (e) { show(e.response?.data?.message || 'Update failed', 'error') }
    finally { setUpdating(false) }
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <PageHeader label="Fulfilment" title="All Orders" />

      {/* Status filter */}
      <div className="flex gap-2 flex-wrap mb-6">
        {STATUSES.map(s => (
          <button key={s} onClick={() => setFilter(s)}
            className={`px-4 py-1.5 text-xs font-mono uppercase tracking-wider transition-all duration-150 border ${
              filter === s ? 'bg-charcoal-900 text-cream-50 border-charcoal-900' : 'border-charcoal-900/20 text-charcoal-700/60 hover:border-charcoal-900/40'
            }`}>{s}</button>
        ))}
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : orders.length === 0
          ? <EmptyState icon="◈" title="No orders" subtitle={`No ${filter === 'ALL' ? '' : filter.toLowerCase() + ' '}orders found`} />
          : <div className="space-y-3">
              {orders.map(o => (
                <div key={o.orderId} className="card p-5">
                  <div className="flex items-start gap-4 flex-wrap">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-3 mb-2 flex-wrap">
                        <p className="font-mono text-xs text-charcoal-700/50">Order #{o.orderId}</p>
                        <StatusBadge status={o.status} />
                        {o.trackingNumber && (
                          <span className="text-xs font-mono text-blue-500">📦 {o.trackingNumber}</span>
                        )}
                      </div>
                      <p className="text-sm text-charcoal-700/70 mb-1">
                        <span className="font-mono text-xs">Ship to:</span> {o.shippingAddress}
                      </p>
                      <p className="text-sm text-charcoal-700/70">
                        <span className="font-mono text-xs">Payment:</span> {o.paymentMethod}
                      </p>
                      {o.items?.length > 0 && (
                        <p className="text-xs text-charcoal-700/50 mt-1">
                          {o.items.map(i => `${i.productName} ×${i.quantity}`).join(', ')}
                        </p>
                      )}
                    </div>
                    <div className="text-right flex-shrink-0">
                      <p className="font-mono text-lg text-gold-600 font-medium">₹{o.totalAmount?.toLocaleString()}</p>
                      <p className="text-xs text-charcoal-700/40 mb-3">
                        {new Date(o.orderDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                      </p>
                      <button onClick={() => openUpdate(o)} className="btn-outline text-xs px-3 py-1.5">Update Status</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
      }

      {updateModal && (
        <Modal title={`Update Order #${updateModal.orderId}`} onClose={() => setUpdateModal(null)}>
          <form onSubmit={handleUpdate} className="space-y-4">
            <FormField label="New Status">
              <select className="input-field" value={updateForm.status} onChange={e => setUpdateForm(f => ({ ...f, status: e.target.value }))}>
                {['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map(s => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>
            </FormField>
            <FormField label="Tracking Number (optional)">
              <input className="input-field" placeholder="e.g. IN123456789" value={updateForm.trackingNumber}
                onChange={e => setUpdateForm(f => ({ ...f, trackingNumber: e.target.value }))} />
            </FormField>
            <button type="submit" disabled={updating} className="btn-primary w-full flex items-center justify-center gap-2">
              {updating ? <><Spinner size="sm" /> Updating…</> : 'Update Order'}
            </button>
          </form>
        </Modal>
      )}
    </div>
  )
}
