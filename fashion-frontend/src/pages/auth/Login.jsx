import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../../api/auth'
import { useAuth } from '../../context/AuthContext'
import { Spinner, FormField } from '../../components/ui'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true); setError('')
    try {
      const { data } = await authApi.login(form)
      login({ token: data.token, email: data.email, role: data.role })
      navigate(data.role === 'ADMIN' ? '/admin' : '/customer')
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials')
    } finally { setLoading(false) }
  }

  return (
    <div className="min-h-screen flex bg-cream-50">
      {/* Left panel */}
      <div className="hidden lg:flex flex-col justify-between w-1/2 bg-charcoal-900 p-12">
        <div className="font-display text-3xl italic text-cream-50 tracking-tight">ÉLUME</div>
        <div>
          <p className="section-label text-gold-400">Fashion E-Commerce</p>
          <h2 className="font-display text-4xl text-cream-50 leading-tight mb-4">
            Style that<br /><em>knows you.</em>
          </h2>
          <p className="text-cream-200/60 text-sm font-body leading-relaxed max-w-xs">
            Virtual try-on, personalized recommendations, curated lookbooks — your fashion world, reinvented.
          </p>
        </div>
        <p className="font-mono text-xs text-cream-200/30">© 2025 ÉLUME Platform</p>
      </div>

      {/* Right panel */}
      <div className="flex-1 flex items-center justify-center p-8">
        <div className="w-full max-w-sm">
          <div className="lg:hidden font-display text-2xl italic text-charcoal-900 mb-8">ÉLUME</div>
          <p className="section-label">Welcome back</p>
          <h1 className="font-display text-3xl text-charcoal-900 mb-8">Sign In</h1>

          {error && (
            <div className="mb-5 px-4 py-3 bg-red-50 border border-red-200 text-red-600 text-sm font-body">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <FormField label="Email">
              <input type="email" className="input-field" placeholder="you@example.com"
                value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
            </FormField>
            <FormField label="Password">
              <input type="password" className="input-field" placeholder="••••••••"
                value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required />
            </FormField>
            <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2 mt-2">
              {loading ? <Spinner size="sm" /> : 'Sign In'}
            </button>
          </form>

          <p className="mt-6 text-sm text-charcoal-700/60 text-center font-body">
            New here?{' '}
            <Link to="/register" className="text-gold-600 hover:text-gold-500 font-medium">Create account</Link>
          </p>

          <div className="mt-8 pt-6 border-t border-charcoal-900/10">
            <p className="text-xs font-mono text-charcoal-700/40 text-center mb-3 uppercase tracking-wider">Demo Credentials</p>
            <div className="space-y-2 text-xs font-mono text-charcoal-700/50">
              <div className="flex justify-between"><span>Customer:</span><span>customer@test.com / pass123</span></div>
              <div className="flex justify-between"><span>Admin:</span><span>admin@test.com / pass123</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
