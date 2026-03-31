import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Products from './pages/Products';
import Cart from './pages/Cart';
import Orders from './pages/Orders';

function PrivateRoute({ children }) {
    return localStorage.getItem('token') ? children : <Navigate to="/login" />;
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
                <Route path="/cart" element={<PrivateRoute><Cart /></PrivateRoute>} />
                <Route path="/orders" element={<PrivateRoute><Orders /></PrivateRoute>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;