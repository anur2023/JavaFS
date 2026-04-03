import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../../api/auth'
import { Spinner, FormField } from '../../components/ui'

export default function Register() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '', fullName: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true); setError('')
    try {
      await authApi.register(form)
      setSuccess('Account created! Redirecting to login…')
      setTimeout(() => navigate('/login'), 1500)
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed')
    } finally { setLoading(false) }
  }

  return (
    <div className="min-h-screen flex bg-cream-50">
      <div className="hidden lg:flex flex-col justify-between w-1/2 bg-blush-200 p-12">
        <div className="font-display text-3xl italic text-charcoal-900 tracking-tight">ÉLUME</div>
        <div>
          <p className="section-label">Join Us</p>
          <h2 className="font-display text-4xl text-charcoal-900 leading-tight mb-4">
            Your style<br /><em>journey begins.</em>
          </h2>
          <p className="text-charcoal-700/60 text-sm font-body leading-relaxed max-w-xs">
            Create your account and unlock virtual try-on, personalized style recommendations, and exclusive lookbooks.
          </p>
        </div>
        <p className="font-mono text-xs text-charcoal-700/30">© 2025 ÉLUME Platform</p>
      </div>

      <div className="flex-1 flex items-center justify-center p-8">
        <div className="w-full max-w-sm">
          <div className="lg:hidden font-display text-2xl italic text-charcoal-900 mb-8">ÉLUME</div>
          <p className="section-label">Get started</p>
          <h1 className="font-display text-3xl text-charcoal-900 mb-8">Create Account</h1>

          {error && <div className="mb-5 px-4 py-3 bg-red-50 border border-red-200 text-red-600 text-sm">{error}</div>}
          {success && <div className="mb-5 px-4 py-3 bg-green-50 border border-green-200 text-green-700 text-sm">{success}</div>}

          <form onSubmit={handleSubmit} className="space-y-5">
            <FormField label="Full Name">
              <input type="text" className="input-field" placeholder="Jane Doe"
                value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} required />
            </FormField>
            <FormField label="Email">
              <input type="email" className="input-field" placeholder="you@example.com"
                value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
            </FormField>
            <FormField label="Password">
              <input type="password" className="input-field" placeholder="••••••••"
                value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required minLength={6} />
            </FormField>
            <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2 mt-2">
              {loading ? <Spinner size="sm" /> : 'Create Account'}
            </button>
          </form>

          <p className="mt-6 text-sm text-charcoal-700/60 text-center">
            Already have an account?{' '}
            <Link to="/login" className="text-gold-600 hover:text-gold-500 font-medium">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  )
}
