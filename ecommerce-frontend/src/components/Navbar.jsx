import { Link, useNavigate, useLocation } from 'react-router-dom';

function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');   // "ADMIN" | "VENDOR" | "CUSTOMER"
    const email = localStorage.getItem('email');

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const isActive = (path) => location.pathname === path;

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        * { margin: 0; padding: 0; box-sizing: border-box; }
        :root {
          --blue: #2563eb; --blue-dark: #1d4ed8; --blue-light: #eff6ff;
          --text: #1e293b; --muted: #64748b; --border: #e2e8f0;
          --bg: #f8fafc; --white: #ffffff; --nav-h: 64px; --danger: #dc2626;
        }
        body { background: var(--bg); color: var(--text); font-family: 'Inter', sans-serif; min-height: 100vh; }
        .navbar { position: fixed; top: 0; left: 0; right: 0; height: var(--nav-h); background: var(--white); border-bottom: 1px solid var(--border); z-index: 1000; display: flex; align-items: center; padding: 0 48px; box-shadow: 0 1px 3px rgba(0,0,0,0.06); }
        .nav-logo { font-size: 16px; font-weight: 700; color: var(--blue); text-decoration: none; margin-right: 36px; display: flex; align-items: center; gap: 9px; white-space: nowrap; }
        .nav-logo-box { width: 32px; height: 32px; background: var(--blue); border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0; }
        .nav-links { display: flex; align-items: center; gap: 2px; flex: 1; }
        .nav-link { font-size: 14px; font-weight: 500; color: var(--muted); text-decoration: none; padding: 7px 13px; border-radius: 7px; transition: all 0.15s; }
        .nav-link:hover { color: var(--text); background: var(--bg); }
        .nav-link.active { color: var(--blue); background: var(--blue-light); font-weight: 600; }
        .nav-right { display: flex; align-items: center; gap: 10px; }
        .nav-user-pill { display: flex; align-items: center; gap: 8px; background: var(--bg); border: 1px solid var(--border); border-radius: 8px; padding: 6px 12px; font-size: 13px; color: var(--muted); }
        .role-badge { font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 20px; text-transform: uppercase; letter-spacing: 0.3px; }
        .role-badge.ADMIN    { background: #fef3c7; color: #d97706; }
        .role-badge.VENDOR   { background: #f0fdf4; color: #16a34a; }
        .role-badge.CUSTOMER { background: var(--blue-light); color: var(--blue); }
        .nbtn { font-family: 'Inter', sans-serif; font-size: 14px; font-weight: 500; padding: 8px 18px; border-radius: 7px; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; border: none; transition: all 0.15s; }
        .nbtn-ghost { background: transparent; border: 1px solid var(--border); color: var(--text); }
        .nbtn-ghost:hover { background: var(--bg); }
        .nbtn-primary { background: var(--blue); color: #fff; }
        .nbtn-primary:hover { background: var(--blue-dark); }
        .nbtn-logout { background: #fef2f2; border: 1px solid #fecaca; color: var(--danger); }
        .nbtn-logout:hover { background: #fee2e2; }
      `}</style>

            <nav className="navbar">
                <Link to="/products" className="nav-logo">
                    <div className="nav-logo-box">🛒</div>
                    Ecommerce Platform
                </Link>

                <div className="nav-links">
                    {/* ── PUBLIC ── */}
                    <Link to="/products" className={`nav-link ${isActive('/products') ? 'active' : ''}`}>Products</Link>

                    {/* ── CUSTOMER links ── */}
                    {token && role === 'CUSTOMER' && (
                        <>
                            <Link to="/cart"   className={`nav-link ${isActive('/cart')   ? 'active' : ''}`}>🛒 Cart</Link>
                            <Link to="/orders" className={`nav-link ${isActive('/orders') ? 'active' : ''}`}>📋 My Orders</Link>
                        </>
                    )}

                    {/* ── VENDOR links ── */}
                    {token && role === 'VENDOR' && (
                        <Link to="/vendor" className={`nav-link ${isActive('/vendor') ? 'active' : ''}`}>🏪 Vendor Dashboard</Link>
                    )}

                    {/* ── ADMIN links ── */}
                    {token && role === 'ADMIN' && (
                        <Link to="/admin" className={`nav-link ${isActive('/admin') ? 'active' : ''}`}>⚙️ Admin Dashboard</Link>
                    )}
                </div>

                <div className="nav-right">
                    {token ? (
                        <>
                            <div className="nav-user-pill">
                                <span>{email}</span>
                                <span className={`role-badge ${role}`}>{role}</span>
                            </div>
                            <button className="nbtn nbtn-logout" onClick={handleLogout}>Logout</button>
                        </>
                    ) : (
                        <>
                            <Link to="/login"    className="nbtn nbtn-ghost">Login</Link>
                            <Link to="/register" className="nbtn nbtn-primary">Register</Link>
                        </>
                    )}
                </div>
            </nav>
        </>
    );
}

export default Navbar;