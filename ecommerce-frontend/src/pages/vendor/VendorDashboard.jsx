import { useState, useEffect } from 'react';
import {
    getMyVendorProfile, registerVendorProfile, updateMyVendorProfile,
    getVendorProducts, createProduct, updateProduct, deleteProduct,
    getVendorOrders, getAllCategories
} from '../../services/vendorService';

const TAB = { PROFILE: 'profile', PRODUCTS: 'products', ORDERS: 'orders' };

const EMPTY_PRODUCT = { name: '', description: '', price: '', vendorId: '', categoryId: '' };

export default function VendorDashboard() {
    const [tab, setTab]           = useState(TAB.PROFILE);
    const [profile, setProfile]   = useState(null);
    const [products, setProducts] = useState([]);
    const [orders, setOrders]     = useState([]);
    const [categories, setCategories] = useState([]);
    const [profileForm, setProfileForm] = useState({ storeName: '', description: '' });
    const [productForm, setProductForm] = useState(EMPTY_PRODUCT);
    const [editingProduct, setEditingProduct] = useState(null); // product being edited
    const [toast, setToast]       = useState(null);
    const [loading, setLoading]   = useState(true);

    const showToast = (msg, type = 'success') => {
        setToast({ msg, type });
        setTimeout(() => setToast(null), 2500);
    };

    /* ── initial load ── */
    useEffect(() => {
        Promise.all([getMyVendorProfile(), getAllCategories()])
            .then(([prof, cats]) => {
                setProfile(prof.data);
                setProfileForm({ storeName: prof.data.storeName, description: prof.data.description || '' });
                setCategories(cats.data);
                // load products and orders using vendor id
                return Promise.all([
                    getVendorProducts(prof.data.id),
                    getVendorOrders(prof.data.id),
                ]);
            })
            .then(([prods, ords]) => {
                setProducts(prods.data);
                setOrders(ords.data);
            })
            .catch(() => {
                // Vendor profile may not exist yet — that's fine
                getAllCategories().then(r => setCategories(r.data)).catch(() => {});
            })
            .finally(() => setLoading(false));
    }, []);

    /* ── profile handlers ── */
    const handleSaveProfile = async (e) => {
        e.preventDefault();
        try {
            if (profile) {
                const res = await updateMyVendorProfile(profileForm);
                setProfile(res.data);
                showToast('Profile updated');
            } else {
                const res = await registerVendorProfile(profileForm);
                setProfile(res.data);
                showToast('Store created! Loading your data...');
                // now load products/orders
                const [prods, ords] = await Promise.all([
                    getVendorProducts(res.data.id),
                    getVendorOrders(res.data.id),
                ]);
                setProducts(prods.data);
                setOrders(ords.data);
            }
        } catch (err) {
            showToast(err.response?.data?.message || 'Failed to save profile', 'error');
        }
    };

    /* ── product handlers ── */
    const startEdit = (p) => {
        setEditingProduct(p.id);
        setProductForm({ name: p.name, description: p.description || '', price: p.price, vendorId: profile.id, categoryId: p.categoryId });
    };

    const cancelEdit = () => { setEditingProduct(null); setProductForm({ ...EMPTY_PRODUCT, vendorId: profile?.id || '' }); };

    const handleSaveProduct = async (e) => {
        e.preventDefault();
        const payload = { ...productForm, vendorId: profile.id, price: parseFloat(productForm.price) };
        try {
            if (editingProduct) {
                const res = await updateProduct(editingProduct, payload);
                setProducts(ps => ps.map(p => p.id === editingProduct ? res.data : p));
                showToast('Product updated');
            } else {
                const res = await createProduct(payload);
                setProducts(ps => [...ps, res.data]);
                showToast('Product created');
            }
            cancelEdit();
        } catch (err) {
            showToast(err.response?.data?.message || 'Failed to save product', 'error');
        }
    };

    const handleDeleteProduct = async (id) => {
        if (!window.confirm('Delete this product?')) return;
        try {
            await deleteProduct(id);
            setProducts(ps => ps.filter(p => p.id !== id));
            showToast('Product deleted');
        } catch { showToast('Failed to delete product', 'error'); }
    };

    /* ── render ── */
    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        .page { min-height:100vh; background:#f8fafc; padding:88px 48px 48px; font-family:'Inter',sans-serif; }
        .page-title { font-size:26px; font-weight:700; color:#1e293b; margin-bottom:4px; }
        .page-sub { font-size:14px; color:#64748b; margin-bottom:28px; }

        .stats { display:grid; grid-template-columns:repeat(3,1fr); gap:16px; margin-bottom:28px; }
        .stat-card { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:20px 24px; }
        .stat-val { font-size:28px; font-weight:700; color:#1e293b; }
        .stat-lbl { font-size:13px; color:#64748b; margin-top:4px; }

        .tabs { display:flex; gap:4px; margin-bottom:24px; background:#fff; border:1px solid #e2e8f0; border-radius:10px; padding:6px; width:fit-content; }
        .tab { padding:8px 20px; border-radius:7px; font-size:14px; font-weight:500; color:#64748b; cursor:pointer; border:none; background:transparent; font-family:'Inter',sans-serif; transition:all 0.15s; }
        .tab.active { background:#2563eb; color:#fff; }
        .tab:hover:not(.active) { background:#f8fafc; color:#1e293b; }

        /* form */
        .form-card { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:28px; max-width:560px; margin-bottom:24px; }
        .form-card h3 { font-size:17px; font-weight:700; color:#1e293b; margin-bottom:20px; }
        .fg { margin-bottom:16px; }
        .fg label { display:block; font-size:13px; font-weight:500; color:#374151; margin-bottom:6px; }
        .fg input, .fg textarea, .fg select { width:100%; padding:10px 13px; border:1px solid #e2e8f0; border-radius:8px; font-size:14px; font-family:'Inter',sans-serif; color:#1e293b; outline:none; transition:border-color 0.15s; }
        .fg input:focus, .fg textarea:focus, .fg select:focus { border-color:#2563eb; }
        .fg textarea { resize:vertical; min-height:80px; }
        .row2 { display:grid; grid-template-columns:1fr 1fr; gap:14px; }
        .btn-save { background:#2563eb; color:#fff; border:none; border-radius:8px; padding:11px 24px; font-size:14px; font-weight:600; cursor:pointer; font-family:'Inter',sans-serif; }
        .btn-save:hover { background:#1d4ed8; }
        .btn-cancel-edit { background:#f8fafc; border:1px solid #e2e8f0; color:#64748b; border-radius:8px; padding:11px 20px; font-size:14px; font-weight:500; cursor:pointer; font-family:'Inter',sans-serif; margin-left:10px; }

        /* table */
        .tbl-wrap { background:#fff; border:1px solid #e2e8f0; border-radius:12px; overflow:hidden; }
        table { width:100%; border-collapse:collapse; }
        th { background:#f8fafc; padding:12px 16px; text-align:left; font-size:12px; font-weight:600; color:#64748b; text-transform:uppercase; letter-spacing:0.4px; border-bottom:1px solid #e2e8f0; }
        td { padding:14px 16px; font-size:14px; color:#1e293b; border-bottom:1px solid #f1f5f9; }
        tr:last-child td { border-bottom:none; }
        .btn-edit { background:#eff6ff; border:1px solid #bfdbfe; color:#2563eb; padding:6px 12px; border-radius:6px; font-size:12px; font-weight:500; cursor:pointer; font-family:'Inter',sans-serif; margin-right:6px; }
        .btn-danger { background:#fef2f2; border:1px solid #fecaca; color:#dc2626; padding:6px 12px; border-radius:6px; font-size:12px; font-weight:500; cursor:pointer; font-family:'Inter',sans-serif; }
        .btn-danger:hover { background:#fee2e2; }

        .no-profile-banner { background:#fffbeb; border:1px solid #fde68a; border-radius:10px; padding:16px 20px; color:#92400e; font-size:14px; margin-bottom:24px; }

        /* toast */
        .toast-wrap { position:fixed; bottom:28px; right:28px; z-index:9999; }
        .toast { background:#fff; border:1px solid #e2e8f0; border-radius:10px; padding:13px 18px; font-size:14px; color:#1e293b; box-shadow:0 4px 20px rgba(0,0,0,.12); display:flex; align-items:center; gap:8px; animation:fadeUp .25s ease; font-family:'Inter',sans-serif; }
        .toast.success { border-left:3px solid #16a34a; }
        .toast.error   { border-left:3px solid #dc2626; }
        @keyframes fadeUp { from { opacity:0; transform:translateY(12px); } to { opacity:1; transform:translateY(0); } }
      `}</style>

            <div className="page">
                <h1 className="page-title">🏪 Vendor Dashboard</h1>
                <p className="page-sub">{profile ? profile.storeName : 'Set up your store to get started'}</p>

                {/* Stats */}
                {profile && (
                    <div className="stats">
                        <div className="stat-card"><div className="stat-val">{products.length}</div><div className="stat-lbl">Products Listed</div></div>
                        <div className="stat-card"><div className="stat-val">{orders.length}</div><div className="stat-lbl">Items Sold</div></div>
                        <div className="stat-card">
                            <div className="stat-val">
                                ₹{orders.reduce((s, o) => s + Number(o.vendorEarning || 0), 0).toLocaleString('en-IN')}
                            </div>
                            <div className="stat-lbl">Total Earnings</div>
                        </div>
                    </div>
                )}

                {/* Tabs */}
                <div className="tabs">
                    {Object.values(TAB).map(t => (
                        <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
                            {t === TAB.PROFILE ? '👤 My Store' : t === TAB.PRODUCTS ? '📦 Products' : '📋 Sales'}
                        </button>
                    ))}
                </div>

                {loading ? <p style={{ color:'#64748b' }}>Loading...</p> : (

                    /* ── PROFILE tab ── */
                    tab === TAB.PROFILE ? (
                        <div className="form-card">
                            <h3>{profile ? 'Update Store Profile' : 'Create Your Store'}</h3>
                            <form onSubmit={handleSaveProfile}>
                                <div className="fg">
                                    <label>Store Name</label>
                                    <input placeholder="Your store name" value={profileForm.storeName}
                                           onChange={e => setProfileForm({ ...profileForm, storeName: e.target.value })} required />
                                </div>
                                <div className="fg">
                                    <label>Description</label>
                                    <textarea placeholder="Tell customers about your store" value={profileForm.description}
                                              onChange={e => setProfileForm({ ...profileForm, description: e.target.value })} />
                                </div>
                                <button type="submit" className="btn-save">{profile ? 'Save Changes' : 'Create Store'}</button>
                            </form>
                        </div>

                        /* ── PRODUCTS tab ── */
                    ) : tab === TAB.PRODUCTS ? (
                        <>
                            {!profile && (
                                <div className="no-profile-banner">⚠️ Please create your store profile first before adding products.</div>
                            )}

                            {/* Product form */}
                            {profile && (
                                <div className="form-card">
                                    <h3>{editingProduct ? 'Edit Product' : 'Add New Product'}</h3>
                                    <form onSubmit={handleSaveProduct}>
                                        <div className="fg">
                                            <label>Product Name</label>
                                            <input placeholder="Product name" value={productForm.name}
                                                   onChange={e => setProductForm({ ...productForm, name: e.target.value })} required />
                                        </div>
                                        <div className="fg">
                                            <label>Description</label>
                                            <textarea placeholder="Product description" value={productForm.description}
                                                      onChange={e => setProductForm({ ...productForm, description: e.target.value })} />
                                        </div>
                                        <div className="row2">
                                            <div className="fg">
                                                <label>Price (₹)</label>
                                                <input type="number" min="0" step="0.01" placeholder="0.00" value={productForm.price}
                                                       onChange={e => setProductForm({ ...productForm, price: e.target.value })} required />
                                            </div>
                                            <div className="fg">
                                                <label>Category</label>
                                                <select value={productForm.categoryId}
                                                        onChange={e => setProductForm({ ...productForm, categoryId: e.target.value })} required>
                                                    <option value="">Select category</option>
                                                    {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                                </select>
                                            </div>
                                        </div>
                                        <button type="submit" className="btn-save">{editingProduct ? 'Update Product' : 'Add Product'}</button>
                                        {editingProduct && <button type="button" className="btn-cancel-edit" onClick={cancelEdit}>Cancel</button>}
                                    </form>
                                </div>
                            )}

                            {/* Product list */}
                            <div className="tbl-wrap">
                                <table>
                                    <thead><tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Actions</th></tr></thead>
                                    <tbody>
                                    {products.length === 0
                                        ? <tr><td colSpan={5} style={{ textAlign:'center', color:'#94a3b8' }}>No products yet</td></tr>
                                        : products.map(p => (
                                            <tr key={p.id}>
                                                <td>{p.id}</td>
                                                <td><strong>{p.name}</strong></td>
                                                <td>{p.categoryName}</td>
                                                <td>₹{Number(p.price).toLocaleString('en-IN')}</td>
                                                <td>
                                                    <button className="btn-edit" onClick={() => startEdit(p)}>Edit</button>
                                                    <button className="btn-danger" onClick={() => handleDeleteProduct(p.id)}>Delete</button>
                                                </td>
                                            </tr>
                                        ))
                                    }
                                    </tbody>
                                </table>
                            </div>
                        </>

                        /* ── SALES tab ── */
                    ) : (
                        <div className="tbl-wrap">
                            <table>
                                <thead><tr><th>Order Item ID</th><th>Product</th><th>Qty</th><th>Unit Price</th><th>Subtotal</th><th>Commission</th><th>Your Earning</th></tr></thead>
                                <tbody>
                                {orders.length === 0
                                    ? <tr><td colSpan={7} style={{ textAlign:'center', color:'#94a3b8' }}>No sales yet</td></tr>
                                    : orders.map(o => (
                                        <tr key={o.id}>
                                            <td>#{o.id}</td>
                                            <td><strong>{o.productName}</strong></td>
                                            <td>{o.quantity}</td>
                                            <td>₹{Number(o.unitPrice).toLocaleString('en-IN')}</td>
                                            <td>₹{Number(o.subtotal).toLocaleString('en-IN')}</td>
                                            <td style={{ color:'#dc2626' }}>-₹{Number(o.commissionAmount).toLocaleString('en-IN')}</td>
                                            <td style={{ color:'#16a34a', fontWeight:600 }}>₹{Number(o.vendorEarning).toLocaleString('en-IN')}</td>
                                        </tr>
                                    ))
                                }
                                </tbody>
                            </table>
                        </div>
                    )
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