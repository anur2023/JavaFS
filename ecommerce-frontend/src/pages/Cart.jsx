import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { getCart, removeFromCart, clearCart } from '../services/cartService';
import { placeOrder } from '../services/orderService';
import { makePayment } from '../services/paymentService';

function Cart() {
    const [cart, setCart] = useState({ items: [], totalItems: 0, grandTotal: 0 });
    const [loading, setLoading] = useState(true);
    const [placing, setPlacing] = useState(false);
    const [toast, setToast] = useState(null);
    const navigate = useNavigate();

    const showToast = (msg, type = 'success') => {
        setToast({ msg, type });
        setTimeout(() => setToast(null), 2500);
    };

    const fetchCart = async () => {
        try {
            const res = await getCart();
            setCart(res.data);
        } catch { showToast('Failed to load cart', 'error'); }
        finally { setLoading(false); }
    };

    useEffect(() => { fetchCart(); }, []);

    const handleRemove = async (id) => {
        try { await removeFromCart(id); fetchCart(); showToast('Item removed'); }
        catch { showToast('Failed to remove', 'error'); }
    };

    const handleClear = async () => {
        try { await clearCart(); fetchCart(); showToast('Cart cleared'); }
        catch { showToast('Failed to clear', 'error'); }
    };

    const handlePlaceOrder = async () => {
        if (!cart.items?.length) { showToast('Cart is empty', 'error'); return; }
        setPlacing(true);
        try {
            const orderRes = await placeOrder({ items: cart.items.map(i => ({ productId: i.productId, quantity: i.quantity })) });
            await makePayment({ orderId: orderRes.data.id, paymentMethod: 'COD' });
            await clearCart();
            showToast('Order placed successfully!');
            setTimeout(() => navigate('/orders'), 1500);
        } catch (err) {
            showToast(err.response?.data?.message || 'Order failed', 'error');
        } finally { setPlacing(false); }
    };

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        .page { min-height: 100vh; background: #f8fafc; padding: 88px 48px 48px; font-family: 'Inter', sans-serif; }
        .page h1 { font-size: 26px; font-weight: 700; color: #1e293b; margin-bottom: 4px; }
        .page-sub { font-size: 14px; color: #64748b; margin-bottom: 28px; }
        .cart-layout { display: grid; grid-template-columns: 1fr 340px; gap: 24px; align-items: start; }
        .cart-items {}
        .cart-row {
          background: #fff; border: 1px solid #e2e8f0; border-radius: 10px;
          padding: 16px 20px; display: flex; align-items: center; gap: 16px;
          margin-bottom: 10px; transition: border-color 0.15s;
        }
        .cart-row:hover { border-color: #bfdbfe; }
        .cart-row-avatar {
          width: 46px; height: 46px; background: #eff6ff; border-radius: 9px;
          display: flex; align-items: center; justify-content: center;
          font-size: 14px; font-weight: 700; color: #2563eb; flex-shrink: 0;
        }
        .cart-row-info { flex: 1; }
        .cart-row-name { font-size: 14px; font-weight: 600; color: #1e293b; margin-bottom: 3px; }
        .cart-row-meta { font-size: 12px; color: #64748b; }
        .cart-row-price { font-size: 16px; font-weight: 700; color: #1e293b; text-align: right; white-space: nowrap; }
        .cart-row-price span { display: block; font-size: 11px; font-weight: 400; color: #94a3b8; }
        .btn-rm { background: #fef2f2; border: 1px solid #fecaca; color: #dc2626; padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: 500; cursor: pointer; font-family: 'Inter', sans-serif; transition: background 0.15s; white-space: nowrap; }
        .btn-rm:hover { background: #fee2e2; }
        .summary-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 24px; position: sticky; top: 80px; }
        .summary-card h3 { font-size: 16px; font-weight: 700; color: #1e293b; margin-bottom: 20px; }
        .sum-row { display: flex; justify-content: space-between; font-size: 14px; color: #64748b; margin-bottom: 12px; }
        .sum-row.total { font-size: 17px; font-weight: 700; color: #1e293b; border-top: 1px solid #e2e8f0; padding-top: 14px; margin-top: 6px; }
        .sum-free { color: #16a34a; font-weight: 500; }
        .btn-order { width: 100%; background: #2563eb; color: #fff; border: none; border-radius: 8px; padding: 13px; font-size: 15px; font-weight: 600; cursor: pointer; font-family: 'Inter', sans-serif; transition: background 0.15s; margin-top: 16px; }
        .btn-order:hover:not(:disabled) { background: #1d4ed8; }
        .btn-order:disabled { opacity: 0.6; cursor: not-allowed; }
        .btn-clear-all { width: 100%; background: transparent; border: 1px solid #e2e8f0; color: #64748b; border-radius: 8px; padding: 10px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Inter', sans-serif; transition: all 0.15s; margin-top: 8px; }
        .btn-clear-all:hover { background: #f8fafc; color: #1e293b; }
        .empty-cart { text-align: center; padding: 80px 20px; }
        .empty-cart p { font-size: 20px; font-weight: 600; color: #475569; margin: 12px 0 6px; }
        .empty-cart span { font-size: 14px; color: #94a3b8; display: block; margin-bottom: 24px; }
        .btn-browse { display: inline-block; background: #2563eb; color: #fff; text-decoration: none; padding: 11px 24px; border-radius: 8px; font-size: 14px; font-weight: 600; font-family: 'Inter', sans-serif; transition: background 0.15s; }
        .btn-browse:hover { background: #1d4ed8; }
        .toast-wrap { position: fixed; bottom: 28px; right: 28px; z-index: 9999; }
        .toast { background: #fff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 13px 18px; font-size: 14px; color: #1e293b; box-shadow: 0 4px 20px rgba(0,0,0,0.12); display: flex; align-items: center; gap: 8px; animation: fadeUp 0.25s ease; font-family: 'Inter', sans-serif; }
        .toast.success { border-left: 3px solid #16a34a; }
        .toast.error { border-left: 3px solid #dc2626; }
        @keyframes fadeUp { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
      `}</style>

            <div className="page">
                <h1>Shopping Cart</h1>
                <p className="page-sub">{cart.totalItems || 0} item(s) in your cart</p>

                {loading ? (
                    <p style={{ color: '#64748b' }}>Loading cart...</p>
                ) : !cart.items?.length ? (
                    <div className="empty-cart">
                        <div style={{ fontSize: '48px' }}>🛒</div>
                        <p>Your cart is empty</p>
                        <span>Add some products to get started</span>
                        <Link to="/products" className="btn-browse">Browse Products</Link>
                    </div>
                ) : (
                    <div className="cart-layout">
                        <div className="cart-items">
                            {cart.items.map(item => (
                                <div key={item.id} className="cart-row">
                                    <div className="cart-row-avatar">{item.productName?.slice(0,2).toUpperCase()}</div>
                                    <div className="cart-row-info">
                                        <div className="cart-row-name">{item.productName}</div>
                                        <div className="cart-row-meta">Qty: {item.quantity} × ₹{Number(item.productPrice).toLocaleString('en-IN')}</div>
                                    </div>
                                    <div className="cart-row-price">
                                        <span>Subtotal</span>
                                        ₹{Number(item.totalPrice).toLocaleString('en-IN')}
                                    </div>
                                    <button className="btn-rm" onClick={() => handleRemove(item.id)}>Remove</button>
                                </div>
                            ))}
                        </div>

                        <div className="summary-card">
                            <h3>Order Summary</h3>
                            <div className="sum-row"><span>Items ({cart.totalItems})</span><span>₹{Number(cart.grandTotal).toLocaleString('en-IN')}</span></div>
                            <div className="sum-row"><span>Delivery</span><span className="sum-free">Free</span></div>
                            <div className="sum-row total"><span>Total</span><span>₹{Number(cart.grandTotal).toLocaleString('en-IN')}</span></div>
                            <button className="btn-order" onClick={handlePlaceOrder} disabled={placing}>
                                {placing ? 'Placing Order...' : 'Place Order'}
                            </button>
                            <button className="btn-clear-all" onClick={handleClear}>Clear Cart</button>
                        </div>
                    </div>
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

export default Cart;