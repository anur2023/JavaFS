import { useAuth } from "../context/AuthContext";

export default function Navbar() {
    const { user, logoutUser } = useAuth();

    return (
        <nav className="navbar">
            <a href="/" className="nav-brand">💊 PharmaCare</a>
            <div className="nav-links">
                <a href="/medicines">Medicines</a>
                {user && user.role === "CUSTOMER" && (
                    <>
                        <a href="/cart">Cart</a>
                        <a href="/orders">My Orders</a>
                        <a href="/profile">Profile</a>
                    </>
                )}
                {user && user.role === "PHARMACIST" && (
                    <a href="/pharmacist/prescriptions">Prescriptions</a>
                )}
                {user && user.role === "ADMIN" && (
                    <a href="/admin/medicines">Admin</a>
                )}
                {user ? (
                    <button onClick={logoutUser} className="btn btn-outline">Logout</button>
                ) : (
                    <>
                        <a href="/login">Login</a>
                        <a href="/register" className="btn btn-primary">Register</a>
                    </>
                )}
            </div>
        </nav>
    );
}