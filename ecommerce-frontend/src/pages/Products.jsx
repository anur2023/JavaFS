import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllProducts } from '../services/productService';
import { addToCart } from '../services/cartService';
import ProductCard from '../components/ProductCard';

function Products() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');
    const [toast, setToast] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        getAllProducts()
            .then(res => { setProducts(res.data); setLoading(false); })
            .catch(() => { setProducts([]); setLoading(false); });
    }, []);

    const showToast = (msg, type = 'success') => {
        setToast({ msg, type });
        setTimeout(() => setToast(null), 2500);
    };

    const handleAddToCart = async (product) => {
        if (!localStorage.getItem('token')) { navigate('/login'); return; }
        try {
            await addToCart({ productId: product.id, productName: product.name, productPrice: product.price, quantity: 1 });
            showToast(`"${product.name}" added to cart!`);
        } catch {
            showToast('Failed to add to cart', 'error');
        }
    };

    const filtered = products.filter(p =>
        p.name.toLowerCase().includes(search.toLowerCase()) ||
        p.categoryName?.toLowerCase().includes(search.toLowerCase()) ||
        p.vendorStoreName?.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        .page { min-height: 100vh; background: #f8fafc; padding: 88px 48px 48px; font-family: 'Inter', sans-serif; }
        .page-header { margin-bottom: 28px; }
        .page-header h1 { font-size: 26px; font-weight: 700; color: #1e293b; margin-bottom: 4px; }
        .page-header p { font-size: 14px; color: #64748b; }
        .toolbar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; gap: 16px; }
        .search-box { position: relative; flex: 1; max-width: 400px; }
        .search-box input {
          width: 100%; padding: 10px 14px 10px 38px;
          border: 1px solid #e2e8f0; border-radius: 8px;
          font-size: 14px; font-family: 'Inter', sans-serif;
          color: #1e293b; background: #fff; outline: none;
          transition: border-color 0.15s;
        }
        .search-box input:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
        .search-box input::placeholder { color: #94a3b8; }
        .search-icon { position: absolute; left: 12px; top: 50%; transform: translateY(-50%); font-size: 14px; pointer-events: none; }
        .results-count { font-size: 13px; color: #64748b; white-space: nowrap; }
        .results-count strong { color: #1e293b; }
        .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 18px; }
        .empty { text-align: center; padding: 80px 20px; color: #94a3b8; }
        .empty p { font-size: 22px; font-weight: 600; color: #475569; margin: 12px 0 6px; }
        .loading-row { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 18px; }
        .skel { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px; height: 210px; }
        .skel-line { background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%); background-size: 200% 100%; animation: shimmer 1.4s infinite; border-radius: 5px; margin-bottom: 10px; }
        @keyframes shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }
        .toast-wrap { position: fixed; bottom: 28px; right: 28px; z-index: 9999; }
        .toast { background: #fff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 13px 18px; font-size: 14px; color: #1e293b; box-shadow: 0 4px 20px rgba(0,0,0,0.12); display: flex; align-items: center; gap: 8px; animation: fadeUp 0.25s ease; font-family: 'Inter', sans-serif; }
        .toast.success { border-left: 3px solid #16a34a; }
        .toast.error { border-left: 3px solid #dc2626; }
        @keyframes fadeUp { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
      `}</style>

            <div className="page">
                <div className="page-header">
                    <h1>All Products</h1>
                    <p>Browse products from our vendors</p>
                </div>

                <div className="toolbar">
                    <div className="search-box">
                        <span className="search-icon">🔍</span>
                        <input placeholder="Search products, categories, stores..." value={search} onChange={e => setSearch(e.target.value)} />
                    </div>
                    <span className="results-count">Showing <strong>{filtered.length}</strong> of <strong>{products.length}</strong> products</span>
                </div>

                {loading ? (
                    <div className="loading-row">
                        {[1,2,3,4,5,6].map(i => (
                            <div key={i} className="skel">
                                <div className="skel-line" style={{ width: '60px', height: '22px' }}></div>
                                <div className="skel-line" style={{ width: '80%', height: '18px', marginTop: '14px' }}></div>
                                <div className="skel-line" style={{ width: '95%', height: '13px' }}></div>
                                <div className="skel-line" style={{ width: '65%', height: '13px' }}></div>
                            </div>
                        ))}
                    </div>
                ) : filtered.length === 0 ? (
                    <div className="empty">
                        <div style={{ fontSize: '44px' }}>📦</div>
                        <p>No products found</p>
                        <span>Try a different search term</span>
                    </div>
                ) : (
                    <div className="grid">
                        {filtered.map(p => <ProductCard key={p.id} product={p} onAddToCart={handleAddToCart} />)}
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

export default Products;