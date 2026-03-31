import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../services/authService';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const res = await login({ email, password });
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('role', res.data.role);
            localStorage.setItem('email', res.data.email);
            navigate('/products');
        } catch (err) {
            setError(err.response?.data?.message || 'Invalid email or password');
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f8fafc; }
        .auth-wrap {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #f8fafc;
          padding: 24px;
        }
        .auth-card {
          background: #fff;
          border: 1px solid #e2e8f0;
          border-radius: 14px;
          padding: 40px;
          width: 100%;
          max-width: 420px;
          box-shadow: 0 4px 24px rgba(0,0,0,0.06);
        }
        .auth-logo {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 28px;
        }
        .auth-logo-box {
          width: 34px; height: 34px;
          background: #2563eb;
          border-radius: 8px;
          display: flex; align-items: center; justify-content: center;
          font-size: 17px;
        }
        .auth-logo-name { font-size: 15px; font-weight: 700; color: #2563eb; }
        .auth-title { font-size: 22px; font-weight: 700; color: #1e293b; margin-bottom: 4px; }
        .auth-sub { font-size: 14px; color: #64748b; margin-bottom: 28px; }
        .form-group { margin-bottom: 18px; }
        .form-label { display: block; font-size: 13px; font-weight: 500; color: #374151; margin-bottom: 6px; }
        .form-input {
          width: 100%; padding: 11px 14px;
          border: 1px solid #e2e8f0; border-radius: 8px;
          font-size: 14px; font-family: 'Inter', sans-serif;
          color: #1e293b; outline: none; transition: all 0.15s;
        }
        .form-input::placeholder { color: #94a3b8; }
        .form-input:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
        .error-msg {
          background: #fef2f2; border: 1px solid #fecaca;
          border-radius: 8px; padding: 11px 14px;
          color: #dc2626; font-size: 13px; margin-bottom: 18px;
        }
        .btn-submit {
          width: 100%; background: #2563eb; color: #fff;
          border: none; border-radius: 8px; padding: 12px;
          font-size: 15px; font-weight: 600; cursor: pointer;
          font-family: 'Inter', sans-serif; transition: background 0.15s;
          margin-top: 4px;
        }
        .btn-submit:hover:not(:disabled) { background: #1d4ed8; }
        .btn-submit:disabled { opacity: 0.6; cursor: not-allowed; }
        .auth-footer { text-align: center; margin-top: 20px; font-size: 14px; color: #64748b; }
        .auth-footer a { color: #2563eb; text-decoration: none; font-weight: 500; }
        .auth-footer a:hover { text-decoration: underline; }
      `}</style>

            <div className="auth-wrap">
                <div className="auth-card">
                    <div className="auth-logo">
                        <div className="auth-logo-box">🛒</div>
                        <span className="auth-logo-name">Ecommerce Platform</span>
                    </div>
                    <h2 className="auth-title">Welcome back</h2>
                    <p className="auth-sub">Sign in to your account</p>

                    {error && <div className="error-msg">⚠️ {error}</div>}

                    <form onSubmit={handleLogin}>
                        <div className="form-group">
                            <label className="form-label">Email Address</label>
                            <input type="email" className="form-input" placeholder="you@example.com"
                                   value={email} onChange={e => setEmail(e.target.value)} required />
                        </div>
                        <div className="form-group">
                            <label className="form-label">Password</label>
                            <input type="password" className="form-input" placeholder="Enter your password"
                                   value={password} onChange={e => setPassword(e.target.value)} required />
                        </div>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Signing in...' : 'Sign In'}
                        </button>
                    </form>

                    <p className="auth-footer">
                        Don't have an account? <Link to="/register">Register here</Link>
                    </p>
                </div>
            </div>
        </>
    );
}

export default Login;