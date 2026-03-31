import { useState, useEffect } from 'react';
import { getMyOrders, cancelOrder } from '../services/orderService';

function Orders() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [toast, setToast] = useState(null);
    const [expanded, setExpanded] = useState(null);

    const showToast = (msg, type = 'success') => {
        setToast({ msg, type });
        setTimeout(() => setToast(null), 2500);
    };

    const fetchOrders = async () => {
        try {
            const res = await getMyOrders();
            setOrders([...res.data].reverse());
        } catch { showToast('Failed to load orders', 'error'); }
        finally { setLoading(false); }
    };

    useEffect(() => { fetchOrders(); }, []);

    const handleCancel = async (orderId, e) => {
        e.stopPropagation();
        try {
            await cancelOrder(orderId);
            showToast('Order cancelled');
            fetchOrders();
        } catch (err) {
            showToast(err.response?.data?.message || 'Cannot cancel', 'error');
        }
    };

    const statusStyle = {
        CONFIRMED: { color: '#16a34a', bg: '#f0fdf4', border: '#bbf7d0', label: 'Confirmed' },
        PENDING:   { color: '#d97706', bg: '#fffbeb', border: '#fde68a', label: 'Pending'   },
        CANCELLED: { color: '#dc2626', bg: '#fef2f2', border: '#fecaca', label: 'Cancelled' },
    };

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        .page { min-height: 100vh; background: #f8fafc; padding: 88px 48px 48px; font-family: 'Inter', sans-serif; }
        .page-hdr { display: flex; justify-content: space-between; align-items: center; margin-bottom: 28px; }
        .page-hdr h1 { font-size: 26px; font-weight: 700; color: #1e293b; }
        .page-hdr p { font-size: 14px; color: #64748b; margin-top: 2px; }
        .orders-badge { background: #eff6ff; color: #2563eb; border: 1px solid #bfdbfe; border-radius: 8px; padding: 6px 14px; font-size: 13px; font-weight: 600; }
        .order-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; margin-bottom: 14px; overflow: hidden; transition: border-color 0.15s; }
        .order-card:hover { border-color: #bfdbfe; }
        .order-head { padding: 18px 22px; display: flex; align-items: center; gap: 16px; cursor: pointer; }
        .order-id { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 7px; padding: 5px 11px; font-size: 13px; font-weight: 600; color: #64748b; white-space: nowrap; }
        .order-meta { flex: 1; }
        .order-date { font-size: 14px; font-weight: 500; color: #1e293b; }
        .order-items-count { font-size: 12px; color: #64748b; margin-top: 2px; }
        .order-total { text-align: right; }
        .order-total-label { font-size: 11px; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.3px; }
        .order-total-val { font-size: 18px; font-weight: 700; color: #1e293b; }
        .status-pill { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; white-space: nowrap; }
        .btn-cancel { background: #fef2f2; border: 1px solid #fecaca; color: #dc2626; padding: 6px 13px; border-radius: 7px; font-size: 12px; font-weight: 500; cursor: pointer; font-family: 'Inter', sans-serif; transition: background 0.15s; white-space: nowrap; }
        .btn-cancel:hover { background: #fee2e2; }
        .expand-icon { width: 28px; height: 28px; background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 11px; color: #94a3b8; cursor: pointer; flex-shrink: 0; }
        .order-body { border-top: 1px solid #f1f5f9; padding: 16px 22px; background: #fafafa; }
        .order-body-title { font-size: 12px; font-weight: 600; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.4px; margin-bottom: 12px; }
        .item-row { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #f1f5f9; }
        .item-row:last-child { border-bottom: none; }
        .item-name { font-size: 14px; font-weight: 500; color: #1e293b; }
        .item-vendor { font-size: 12px; color: #94a3b8; margin-top: 2px; }
        .item-right { text-align: right; }
        .item-qty { font-size: 12px; color: #94a3b8; }
        .item-sub { font-size: 14px; font-weight: 600; color: #1e293b; }
        .empty-state { text-align: center; padding: 80px 20px; }
        .empty-state p { font-size: 20px; font-weight: 600; color: #475569; margin: 12px 0 6px; }
        .empty-state span { font-size: 14px; color: #94a3b8; }
        .toast-wrap { position: fixed; bottom: 28px; right: 28px; z-index: 9999; }
        .toast { background: #fff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 13px 18px; font-size: 14px; color: #1e293b; box-shadow: 0 4px 20px rgba(0,0,0,0.12); display: flex; align-items: center; gap: 8px; animation: fadeUp 0.25s ease; font-family: 'Inter', sans-serif; }
        .toast.success { border-left: 3px solid #16a34a; }
        .toast.error { border-left: 3px solid #dc2626; }
        @keyframes fadeUp { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
      `}</style>

            <div className="page">
                <div className="page-hdr">
                    <div>
                        <h1>My Orders</h1>
                        <p>View and manage your orders</p>
                    </div>
                    {!loading && <div className="orders-badge">{orders.length} orders</div>}
                </div>

                {loading ? (
                    <p style={{ color: '#64748b' }}>Loading orders...</p>
                ) : orders.length === 0 ? (
                    <div className="empty-state">
                        <div style={{ fontSize: '48px' }}>📋</div>
                        <p>No orders yet</p>
                        <span>Place your first order from the products page</span>
                    </div>
                ) : (
                    orders.map(order => {
                        const s = statusStyle[order.status] || statusStyle.PENDING;
                        const open = expanded === order.id;
                        return (
                            <div key={order.id} className="order-card">
                                <div className="order-head" onClick={() => setExpanded(open ? null : order.id)}>
                                    <div className="order-id">Order #{order.id}</div>
                                    <div className="order-meta">
                                        <div className="order-date">
                                            {new Date(order.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                                        </div>
                                        <div className="order-items-count">{order.items?.length || 0} item(s)</div>
                                    </div>
                                    <div className="order-total">
                                        <div className="order-total-label">Total</div>
                                        <div className="order-total-val">₹{Number(order.totalAmount).toLocaleString('en-IN')}</div>
                                    </div>
                                    <span className="status-pill" style={{ color: s.color, background: s.bg, border: `1px solid ${s.border}` }}>
                    {s.label}
                  </span>
                                    {order.status !== 'CANCELLED' && (
                                        <button className="btn-cancel" onClick={(e) => handleCancel(order.id, e)}>Cancel</button>
                                    )}
                                    <div className="expand-icon">{open ? '▲' : '▼'}</div>
                                </div>

                                {open && (
                                    <div className="order-body">
                                        <div className="order-body-title">Items in this order</div>
                                        {order.items?.map(item => (
                                            <div key={item.id} className="item-row">
                                                <div>
                                                    <div className="item-name">{item.productName}</div>
                                                    <div className="item-vendor">🏪 {item.vendorStoreName}</div>
                                                </div>
                                                <div className="item-right">
                                                    <div className="item-qty">Qty: {item.quantity}</div>
                                                    <div className="item-sub">₹{Number(item.subtotal).toLocaleString('en-IN')}</div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        );
                    })
                )}
            </div>

            {toast && (
                <div className="toast-wrap">
                    <div className={`toast ${toast.type}`}>{toast.type === 'success' ? '✅' : '❌'} {toast.msg}</div>
                </div>
            )}
        </>
    );
}

export default Orders;