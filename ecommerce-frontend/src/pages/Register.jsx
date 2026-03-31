import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register } from '../services/authService';

function Register() {
    const [form, setForm] = useState({ name: '', email: '', password: '', role: 'CUSTOMER' });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await register(form);
            setSuccess('Account created successfully! Redirecting...');
            setTimeout(() => navigate('/login'), 1500);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed');
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
        .auth-wrap { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: #f8fafc; padding: 24px; }
        .auth-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 14px; padding: 40px; width: 100%; max-width: 440px; box-shadow: 0 4px 24px rgba(0,0,0,0.06); }
        .auth-logo { display: flex; align-items: center; gap: 8px; margin-bottom: 28px; }
        .auth-logo-box { width: 34px; height: 34px; background: #2563eb; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 17px; }
        .auth-logo-name { font-size: 15px; font-weight: 700; color: #2563eb; }
        .auth-title { font-size: 22px; font-weight: 700; color: #1e293b; margin-bottom: 4px; }
        .auth-sub { font-size: 14px; color: #64748b; margin-bottom: 28px; }
        .form-group { margin-bottom: 16px; }
        .form-label { display: block; font-size: 13px; font-weight: 500; color: #374151; margin-bottom: 6px; }
        .form-input, .form-select {
          width: 100%; padding: 11px 14px;
          border: 1px solid #e2e8f0; border-radius: 8px;
          font-size: 14px; font-family: 'Inter', sans-serif;
          color: #1e293b; outline: none; transition: all 0.15s;
          background: #fff;
        }
        .form-input::placeholder { color: #94a3b8; }
        .form-input:focus, .form-select:focus { border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
        .role-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
        .role-card {
          border: 1px solid #e2e8f0; border-radius: 9px; padding: 14px 12px;
          cursor: pointer; transition: all 0.15s; text-align: center; background: #fff;
        }
        .role-card:hover { border-color: #93c5fd; background: #f0f9ff; }
        .role-card.selected { border-color: #2563eb; background: #eff6ff; }
        .role-card-icon { font-size: 22px; margin-bottom: 5px; }
        .role-card-label { font-size: 13px; font-weight: 600; color: #1e293b; }
        .role-card-sub { font-size: 11px; color: #94a3b8; margin-top: 2px; }
        .error-msg { background: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; padding: 11px 14px; color: #dc2626; font-size: 13px; margin-bottom: 16px; }
        .success-msg { background: #f0fdf4; border: 1px solid #bbf7d0; border-radius: 8px; padding: 11px 14px; color: #16a34a; font-size: 13px; margin-bottom: 16px; }
        .btn-submit { width: 100%; background: #2563eb; color: #fff; border: none; border-radius: 8px; padding: 12px; font-size: 15px; font-weight: 600; cursor: pointer; font-family: 'Inter', sans-serif; transition: background 0.15s; margin-top: 6px; }
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
                    <h2 className="auth-title">Create an account</h2>
                    <p className="auth-sub">Fill in your details to get started</p>

                    {error && <div className="error-msg">⚠️ {error}</div>}
                    {success && <div className="success-msg">✅ {success}</div>}

                    <form onSubmit={handleRegister}>
                        <div className="form-group">
                            <label className="form-label">Full Name</label>
                            <input name="name" className="form-input" placeholder="Your full name"
                                   value={form.name} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label className="form-label">Email Address</label>
                            <input type="email" name="email" className="form-input" placeholder="you@example.com"
                                   value={form.email} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label className="form-label">Password</label>
                            <input type="password" name="password" className="form-input" placeholder="Create a password"
                                   value={form.password} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label className="form-label">I want to</label>
                            <div className="role-grid">
                                <div className={`role-card ${form.role === 'CUSTOMER' ? 'selected' : ''}`}
                                     onClick={() => setForm({ ...form, role: 'CUSTOMER' })}>
                                    <div className="role-card-icon">🛒</div>
                                    <div className="role-card-label">Shop</div>
                                    <div className="role-card-sub">Buy products</div>
                                </div>
                                <div className={`role-card ${form.role === 'VENDOR' ? 'selected' : ''}`}
                                     onClick={() => setForm({ ...form, role: 'VENDOR' })}>
                                    <div className="role-card-icon">🏪</div>
                                    <div className="role-card-label">Sell</div>
                                    <div className="role-card-sub">List products</div>
                                </div>
                            </div>
                        </div>
                        <button type="submit" className="btn-submit" disabled={loading}>
                            {loading ? 'Creating account...' : 'Create Account'}
                        </button>
                    </form>

                    <p className="auth-footer">
                        Already have an account? <Link to="/login">Sign in</Link>
                    </p>
                </div>
            </div>
        </>
    );
}

export default Register;