import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Products from './pages/Products';
import Cart from './pages/Cart';
import Orders from './pages/Orders';
import AdminDashboard from './pages/admin/AdminDashboard';
import VendorDashboard from './pages/vendor/VendorDashboard';

// Requires login
function PrivateRoute({ children }) {
    return localStorage.getItem('token') ? children : <Navigate to="/login" />;
}

// Requires a specific role
function RoleRoute({ children, role }) {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('role');
    if (!token) return <Navigate to="/login" />;
    if (userRole !== role) return <Navigate to="/products" />;
    return children;
}

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <Routes>
                <Route path="/" element={<Navigate to="/products" />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/products" element={<Products />} />

                {/* CUSTOMER routes */}
                <Route path="/cart" element={<PrivateRoute><Cart /></PrivateRoute>} />
                <Route path="/orders" element={<PrivateRoute><Orders /></PrivateRoute>} />

                {/* ADMIN routes */}
                <Route path="/admin" element={<RoleRoute role="ADMIN"><AdminDashboard /></RoleRoute>} />

                {/* VENDOR routes */}
                <Route path="/vendor" element={<RoleRoute role="VENDOR"><VendorDashboard /></RoleRoute>} />

                {/* Catch-all */}
                <Route path="*" element={<Navigate to="/products" />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;