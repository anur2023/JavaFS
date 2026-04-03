import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import { PageLoader } from './components/ui'
import Navbar from './components/Navbar'

// Auth pages
import Login from './pages/auth/Login'
import Register from './pages/auth/Register'

// Customer pages
import CustomerHome from './pages/customer/Home'
import Products from './pages/customer/Products'
import Wishlist from './pages/customer/Wishlist'
import Orders from './pages/customer/Orders'
import TryOn from './pages/customer/TryOn'
import Lookbook from './pages/customer/Lookbook'
import Quiz from './pages/customer/Quiz'
import Inspiration from './pages/customer/Inspiration'

// Admin pages
import AdminDashboard from './pages/admin/Dashboard'
import AdminProducts from './pages/admin/Products'
import AdminOrders from './pages/admin/Orders'
import AdminLookbooks from './pages/admin/Lookbooks'
import AdminInspiration from './pages/admin/Inspiration'
import AdminQuiz from './pages/admin/Quiz'

// ── Route guards ──────────────────────────────────────────────────────────────

function RequireAuth({ role }) {
  const { user, loading } = useAuth()
  if (loading) return <PageLoader />
  if (!user) return <Navigate to="/login" replace />
  if (role && user.role !== role) return <Navigate to={user.role === 'ADMIN' ? '/admin' : '/customer'} replace />
  return <Outlet />
}

function Layout() {
  return (
    <div className="min-h-screen bg-cream-50">
      <Navbar />
      <main>
        <Outlet />
      </main>
    </div>
  )
}

function PublicOnly() {
  const { user, loading } = useAuth()
  if (loading) return <PageLoader />
  if (user) return <Navigate to={user.role === 'ADMIN' ? '/admin' : '/customer'} replace />
  return <Outlet />
}

// ── App ───────────────────────────────────────────────────────────────────────

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route element={<PublicOnly />}>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>

          {/* Customer routes */}
          <Route element={<RequireAuth role="CUSTOMER" />}>
            <Route element={<Layout />}>
              <Route path="/customer" element={<CustomerHome />} />
              <Route path="/customer/products" element={<Products />} />
              <Route path="/customer/wishlist" element={<Wishlist />} />
              <Route path="/customer/orders" element={<Orders />} />
              <Route path="/customer/tryon" element={<TryOn />} />
              <Route path="/customer/lookbook" element={<Lookbook />} />
              <Route path="/customer/quiz" element={<Quiz />} />
              <Route path="/customer/inspiration" element={<Inspiration />} />
            </Route>
          </Route>

          {/* Admin routes */}
          <Route element={<RequireAuth role="ADMIN" />}>
            <Route element={<Layout />}>
              <Route path="/admin" element={<AdminDashboard />} />
              <Route path="/admin/products" element={<AdminProducts />} />
              <Route path="/admin/orders" element={<AdminOrders />} />
              <Route path="/admin/lookbooks" element={<AdminLookbooks />} />
              <Route path="/admin/inspiration" element={<AdminInspiration />} />
              <Route path="/admin/quiz" element={<AdminQuiz />} />
            </Route>
          </Route>

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
