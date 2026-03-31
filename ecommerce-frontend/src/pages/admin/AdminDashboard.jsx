import { useState, useEffect } from 'react';
import {
    getAllOrders, getAllVendors, deleteVendor,
    getAllCategories, createCategory, deleteCategory
} from '../../services/adminService';

const TAB = { ORDERS: 'orders', VENDORS: 'vendors', CATEGORIES: 'categories' };

export default function AdminDashboard() {
    const [tab, setTab]             = useState(TAB.ORDERS);
    const [orders, setOrders]       = useState([]);
    const [vendors, setVendors]     = useState([]);
    const [categories, setCategories] = useState([]);
    const [newCat, setNewCat]       = useState({ name: '', description: '' });
    const [toast, setToast]         = useState(null);
    const [loading, setLoading]     = useState(true);

    const showToast = (msg, type = 'success') => {
        setToast({ msg, type });
        setTimeout(() => setToast(null), 2500);
    };

    /* ── fetch all data ── */
    useEffect(() => {
        Promise.all([getAllOrders(), getAllVendors(), getAllCategories()])
            .then(([o, v, c]) => { setOrders(o.data); setVendors(v.data); setCategories(c.data); })
            .catch(() => showToast('Failed to load data', 'error'))
            .finally(() => setLoading(false));
    }, []);

    /* ── handlers ── */
    const handleDeleteVendor = async (id) => {
        if (!window.confirm('Delete this vendor?')) return;
        try {
            await deleteVendor(id);
            setVendors(v => v.filter(x => x.id !== id));
            showToast('Vendor deleted');
        } catch { showToast('Failed to delete vendor', 'error'); }
    };

    const handleCreateCategory = async (e) => {
        e.preventDefault();
        try {
            const res = await createCategory(newCat);
            setCategories(c => [...c, res.data]);
            setNewCat({ name: '', description: '' });
            showToast('Category created');
        } catch { showToast('Failed to create category', 'error'); }
    };

    const handleDeleteCategory = async (id) => {
        if (!window.confirm('Delete this category?')) return;
        try {
            await deleteCategory(id);
            setCategories(c => c.filter(x => x.id !== id));
            showToast('Category deleted');
        } catch { showToast('Failed to delete category', 'error'); }
    };

    const statusColor = { CONFIRMED: '#16a34a', PENDING: '#d97706', CANCELLED: '#dc2626' };

    /* ── render ── */
    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        .page { min-height:100vh; background:#f8fafc; padding:88px 48px 48px; font-family:'Inter',sans-serif; }
        .page-title { font-size:26px; font-weight:700; color:#1e293b; margin-bottom:4px; }
        .page-sub { font-size:14px; color:#64748b; margin-bottom:28px; }

        /* stats */
        .stats { display:grid; grid-template-columns:repeat(3,1fr); gap:16px; margin-bottom:28px; }
        .stat-card { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:20px 24px; }
        .stat-val { font-size:28px; font-weight:700; color:#1e293b; }
        .stat-lbl { font-size:13px; color:#64748b; margin-top:4px; }

        /* tabs */
        .tabs { display:flex; gap:4px; margin-bottom:24px; background:#fff; border:1px solid #e2e8f0; border-radius:10px; padding:6px; width:fit-content; }
        .tab { padding:8px 20px; border-radius:7px; font-size:14px; font-weight:500; color:#64748b; cursor:pointer; border:none; background:transparent; font-family:'Inter',sans-serif; transition:all 0.15s; }
        .tab.active { background:#2563eb; color:#fff; }
        .tab:hover:not(.active) { background:#f8fafc; color:#1e293b; }

        /* table */
        .tbl-wrap { background:#fff; border:1px solid #e2e8f0; border-radius:12px; overflow:hidden; }
        table { width:100%; border-collapse:collapse; }
        th { background:#f8fafc; padding:12px 16px; text-align:left; font-size:12px; font-weight:600; color:#64748b; text-transform:uppercase; letter-spacing:0.4px; border-bottom:1px solid #e2e8f0; }
        td { padding:14px 16px; font-size:14px; color:#1e293b; border-bottom:1px solid #f1f5f9; }
        tr:last-child td { border-bottom:none; }
        tr:hover td { background:#fafafe; }

        .badge { padding:3px 10px; border-radius:20px; font-size:12px; font-weight:600; }
        .btn-danger { background:#fef2f2; border:1px solid #fecaca; color:#dc2626; padding:6px 12px; border-radius:6px; font-size:12px; font-weight:500; cursor:pointer; font-family:'Inter',sans-serif; transition:background 0.15s; }
        .btn-danger:hover { background:#fee2e2; }

        /* category form */
        .cat-form { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:24px; margin-bottom:20px; display:flex; gap:12px; align-items:flex-end; flex-wrap:wrap; }
        .cat-form label { font-size:13px; font-weight:500; color:#374151; display:block; margin-bottom:6px; }
        .cat-form input { padding:9px 13px; border:1px solid #e2e8f0; border-radius:8px; font-size:14px; font-family:'Inter',sans-serif; color:#1e293b; outline:none; transition:border-color 0.15s; width:200px; }
        .cat-form input:focus { border-color:#2563eb; }
        .btn-add { background:#2563eb; color:#fff; border:none; border-radius:8px; padding:10px 20px; font-size:14px; font-weight:600; cursor:pointer; font-family:'Inter',sans-serif; white-space:nowrap; }
        .btn-add:hover { background:#1d4ed8; }

        /* toast */
        .toast-wrap { position:fixed; bottom:28px; right:28px; z-index:9999; }
        .toast { background:#fff; border:1px solid #e2e8f0; border-radius:10px; padding:13px 18px; font-size:14px; color:#1e293b; box-shadow:0 4px 20px rgba(0,0,0,.12); display:flex; align-items:center; gap:8px; animation:fadeUp .25s ease; font-family:'Inter',sans-serif; }
        .toast.success { border-left:3px solid #16a34a; }
        .toast.error   { border-left:3px solid #dc2626; }
        @keyframes fadeUp { from { opacity:0; transform:translateY(12px); } to { opacity:1; transform:translateY(0); } }
      `}</style>

            <div className="page">
                <h1 className="page-title">⚙️ Admin Dashboard</h1>
                <p className="page-sub">Manage orders, vendors, and categories</p>

                {/* Stats */}
                <div className="stats">
                    <div className="stat-card"><div className="stat-val">{orders.length}</div><div className="stat-lbl">Total Orders</div></div>
                    <div className="stat-card"><div className="stat-val">{vendors.length}</div><div className="stat-lbl">Registered Vendors</div></div>
                    <div className="stat-card"><div className="stat-val">{categories.length}</div><div className="stat-lbl">Categories</div></div>
                </div>

                {/* Tabs */}
                <div className="tabs">
                    {Object.values(TAB).map(t => (
                        <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
                            {t === TAB.ORDERS ? '📋 Orders' : t === TAB.VENDORS ? '🏪 Vendors' : '🏷️ Categories'}
                        </button>
                    ))}
                </div>

                {loading ? <p style={{ color:'#64748b' }}>Loading...</p> : (

                    /* ── ORDERS tab ── */
                    tab === TAB.ORDERS ? (
                        <div className="tbl-wrap">
                            <table>
                                <thead><tr><th>Order ID</th><th>Customer</th><th>Amount</th><th>Status</th><th>Date</th></tr></thead>
                                <tbody>
                                {orders.map(o => (
                                    <tr key={o.id}>
                                        <td><strong>#{o.id}</strong></td>
                                        <td>{o.userName}</td>
                                        <td>₹{Number(o.totalAmount).toLocaleString('en-IN')}</td>
                                        <td>
                                            <span className="badge" style={{ color: statusColor[o.status] || '#64748b', background: '#f8fafc' }}>
                                                {o.status}
                                            </span>
                                        </td>
                                        <td>{new Date(o.createdAt).toLocaleDateString('en-IN')}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>

                        /* ── VENDORS tab ── */
                    ) : tab === TAB.VENDORS ? (
                        <div className="tbl-wrap">
                            <table>
                                <thead><tr><th>ID</th><th>Store Name</th><th>Owner</th><th>Email</th><th>Action</th></tr></thead>
                                <tbody>
                                {vendors.map(v => (
                                    <tr key={v.id}>
                                        <td>{v.id}</td>
                                        <td><strong>{v.storeName}</strong></td>
                                        <td>{v.userName}</td>
                                        <td>{v.userEmail}</td>
                                        <td><button className="btn-danger" onClick={() => handleDeleteVendor(v.id)}>Delete</button></td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>

                        /* ── CATEGORIES tab ── */
                    ) : (
                        <>
                            <form className="cat-form" onSubmit={handleCreateCategory}>
                                <div>
                                    <label>Category Name</label>
                                    <input placeholder="e.g. Electronics" value={newCat.name}
                                           onChange={e => setNewCat({ ...newCat, name: e.target.value })} required />
                                </div>
                                <div>
                                    <label>Description</label>
                                    <input placeholder="Optional description" value={newCat.description}
                                           onChange={e => setNewCat({ ...newCat, description: e.target.value })} />
                                </div>
                                <button type="submit" className="btn-add">+ Add Category</button>
                            </form>

                            <div className="tbl-wrap">
                                <table>
                                    <thead><tr><th>ID</th><th>Name</th><th>Description</th><th>Action</th></tr></thead>
                                    <tbody>
                                    {categories.map(c => (
                                        <tr key={c.id}>
                                            <td>{c.id}</td>
                                            <td><strong>{c.name}</strong></td>
                                            <td>{c.description || '—'}</td>
                                            <td><button className="btn-danger" onClick={() => handleDeleteCategory(c.id)}>Delete</button></td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </>
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