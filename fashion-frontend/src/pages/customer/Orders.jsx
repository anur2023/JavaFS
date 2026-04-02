import { useState, useEffect } from 'react'
import { orderApi } from '../../api/modules'
import { productApi } from '../../api/product'
import { PageHeader, EmptyState, useToast, Spinner, StatusBadge, Modal, FormField } from '../../components/ui'
import { Link } from 'react-router-dom'

export default function Orders() {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [placeModal, setPlaceModal] = useState(false)
  const [detailModal, setDetailModal] = useState(null)
  const [products, setProducts] = useState([])
  const [cancellingId, setCancellingId] = useState(null)
  const { show, ToastEl } = useToast()

  const [orderForm, setOrderForm] = useState({
    shippingAddress: '',
    paymentMethod: 'CREDIT_CARD',
    items: [{ productId: '', quantity: 1 }],
  })
  const [placing, setPlacing] = useState(false)

  useEffect(() => { fetchOrders(); fetchProducts() }, [])

  const fetchOrders = async () => {
    setLoading(true)
    try {
      const { data } = await orderApi.getMyOrders()
      setOrders(Array.isArray(data) ? data : [])
    } catch { show('Failed to load orders', 'error') }
    finally { setLoading(false) }
  }

  const fetchProducts = async () => {
    try {
      const { data } = await productApi.getAll()
      setProducts(Array.isArray(data) ? data : [])
    } catch { }
  }

  const handleCancel = async (orderId) => {
    if (!confirm('Cancel this order?')) return
    setCancellingId(orderId)
    try {
      await orderApi.cancel(orderId)
      show('Order cancelled')
      fetchOrders()
    } catch (e) { show(e.response?.data?.message || 'Cannot cancel order', 'error') }
    finally { setCancellingId(null) }
  }

  const handleReorder = async (orderId) => {
    try {
      await orderApi.reorder(orderId)
      show('Reorder placed!')
      fetchOrders()
    } catch (e) { show(e.response?.data?.message || 'Reorder failed', 'error') }
  }

  const addItem = () => setOrderForm(f => ({ ...f, items: [...f.items, { productId: '', quantity: 1 }] }))
  const removeItem = (i) => setOrderForm(f => ({ ...f, items: f.items.filter((_, idx) => idx !== i) }))
  const updateItem = (i, field, val) => setOrderForm(f => ({
    ...f, items: f.items.map((it, idx) => idx === i ? { ...it, [field]: val } : it)
  }))

  const handlePlaceOrder = async (e) => {
    e.preventDefault()
    if (!orderForm.shippingAddress.trim()) return show('Shipping address required', 'error')
    const items = orderForm.items.filter(i => i.productId && i.quantity > 0)
    if (items.length === 0) return show('Add at least one product', 'error')
    setPlacing(true)
    try {
      await orderApi.place({ ...orderForm, items: items.map(i => ({ productId: Number(i.productId), quantity: Number(i.quantity) })) })
      show('Order placed successfully!')
      setPlaceModal(false)
      setOrderForm({ shippingAddress: '', paymentMethod: 'CREDIT_CARD', items: [{ productId: '', quantity: 1 }] })
      fetchOrders()
    } catch (e) { show(e.response?.data?.message || 'Order failed', 'error') }
    finally { setPlacing(false) }
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-10 page-enter">
      {ToastEl}
      <div className="flex items-center justify-between mb-8">
        <PageHeader label="Purchase History" title="My Orders" />
        <button onClick={() => setPlaceModal(true)} className="btn-primary">+ Place Order</button>
      </div>

      {loading
        ? <div className="flex justify-center py-20"><Spinner size="lg" /></div>
        : orders.length === 0
          ? <EmptyState icon="◻" title="No orders yet"
              subtitle="Place your first order to get started"
              action={<button onClick={() => setPlaceModal(true)} className="btn-primary">Place Order</button>} />
          : <div className="space-y-4">
              {orders.map(order => (
                <div key={order.orderId} className="card p-5">
                  <div className="flex items-start justify-between mb-3">
                    <div>
                      <p className="font-mono text-xs text-charcoal-700/50 mb-1">Order #{order.orderId}</p>
                      <StatusBadge status={order.status} />
                    </div>
                    <div className="text-right">
                      <p className="font-mono text-lg text-gold-600 font-medium">₹{order.totalAmount?.toLocaleString()}</p>
                      <p className="text-xs text-charcoal-700/40">{new Date(order.orderDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}</p>
                    </div>
                  </div>

                  <div className="text-sm text-charcoal-700/70 mb-3 space-y-1">
                    <p><span className="font-mono text-xs">Ship to:</span> {order.shippingAddress}</p>
                    <p><span className="font-mono text-xs">Payment:</span> {order.paymentMethod}</p>
                    {order.trackingNumber && <p><span className="font-mono text-xs">Tracking:</span> {order.trackingNumber}</p>}
                  </div>

                  <div className="border-t border-charcoal-900/8 pt-3 mb-3">
                    {order.items?.map(item => (
                      <div key={item.orderItemId} className="flex justify-between text-sm py-1">
                        <span className="text-charcoal-700/70">{item.productName} × {item.quantity}</span>
                        <span className="font-mono text-charcoal-900">₹{item.subtotal?.toLocaleString()}</span>
                      </div>
                    ))}
                  </div>

                  <div className="flex gap-3">
                    <button onClick={() => setDetailModal(order)} className="text-xs font-mono text-gold-600 hover:underline">View Details</button>
                    {order.status === 'PENDING' && (
                      <button onClick={() => handleCancel(order.orderId)} disabled={cancellingId === order.orderId}
                        className="text-xs font-mono text-red-400 hover:text-red-600 transition-colors disabled:opacity-40">
                        {cancellingId === order.orderId ? '…' : 'Cancel'}
                      </button>
                    )}
                    {order.status === 'DELIVERED' && (
                      <button onClick={() => handleReorder(order.orderId)} className="text-xs font-mono text-charcoal-700/60 hover:text-charcoal-900 transition-colors">
                        Reorder
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
      }

      {/* Place Order Modal */}
      {placeModal && (
        <Modal title="Place New Order" onClose={() => setPlaceModal(false)} wide>
          <form onSubmit={handlePlaceOrder} className="space-y-4">
            <FormField label="Shipping Address">
              <textarea className="input-field resize-none" rows={2} placeholder="123 Main St, Mumbai, Maharashtra 400001"
                value={orderForm.shippingAddress} onChange={e => setOrderForm(f => ({ ...f, shippingAddress: e.target.value }))} />
            </FormField>
            <FormField label="Payment Method">
              <select className="input-field" value={orderForm.paymentMethod} onChange={e => setOrderForm(f => ({ ...f, paymentMethod: e.target.value }))}>
                {['CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING', 'CASH_ON_DELIVERY'].map(m => (
                  <option key={m} value={m}>{m.replace('_', ' ')}</option>
                ))}
              </select>
            </FormField>
            <div>
              <label className="block text-xs font-mono tracking-wider uppercase text-charcoal-700/70 mb-2">Items</label>
              {orderForm.items.map((item, i) => (
                <div key={i} className="flex gap-2 mb-2">
                  <select className="input-field flex-1" value={item.productId} onChange={e => updateItem(i, 'productId', e.target.value)}>
                    <option value="">Select product…</option>
                    {products.map(p => <option key={p.productId} value={p.productId}>{p.name} — ₹{p.price?.toLocaleString()}</option>)}
                  </select>
                  <input type="number" min={1} className="input-field w-20" value={item.quantity}
                    onChange={e => updateItem(i, 'quantity', e.target.value)} />
                  {orderForm.items.length > 1 && (
                    <button type="button" onClick={() => removeItem(i)} className="text-red-400 px-2 hover:text-red-600">✕</button>
                  )}
                </div>
              ))}
              <button type="button" onClick={addItem} className="text-xs font-mono text-gold-600 hover:underline mt-1">+ Add item</button>
            </div>
            <button type="submit" disabled={placing} className="btn-gold w-full flex items-center justify-center gap-2">
              {placing ? <><Spinner size="sm" /> Placing…</> : 'Place Order'}
            </button>
          </form>
        </Modal>
      )}

      {/* Detail Modal */}
      {detailModal && (
        <Modal title={`Order #${detailModal.orderId}`} onClose={() => setDetailModal(null)}>
          <div className="space-y-3 text-sm">
            <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Status</span><StatusBadge status={detailModal.status} /></div>
            <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Total</span><span className="font-mono font-medium text-gold-600">₹{detailModal.totalAmount?.toLocaleString()}</span></div>
            <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Date</span><span>{new Date(detailModal.orderDate).toLocaleString('en-IN')}</span></div>
            <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Payment</span><span>{detailModal.paymentMethod}</span></div>
            <div><span className="font-mono text-xs text-charcoal-700/50">Address</span><p className="mt-1 text-charcoal-700/70">{detailModal.shippingAddress}</p></div>
            {detailModal.trackingNumber && <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Tracking</span><span className="font-mono">{detailModal.trackingNumber}</span></div>}
            {detailModal.deliveryEstimate && <div className="flex justify-between"><span className="font-mono text-xs text-charcoal-700/50">Est. Delivery</span><span>{new Date(detailModal.deliveryEstimate).toLocaleDateString('en-IN')}</span></div>}
            <div className="border-t border-charcoal-900/10 pt-3">
              {detailModal.items?.map(item => (
                <div key={item.orderItemId} className="flex justify-between py-1.5">
                  <span>{item.productName} × {item.quantity}</span>
                  <span className="font-mono">₹{item.subtotal?.toLocaleString()}</span>
                </div>
              ))}
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
