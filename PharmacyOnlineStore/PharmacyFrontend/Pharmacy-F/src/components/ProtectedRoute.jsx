import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({ children, allowedRoles }) {
    const { user } = useAuth();

    if (!user) {
        return (
            <div style={{ padding: "2rem", textAlign: "center" }}>
                <p>Please <a href="/login">log in</a> to access this page.</p>
            </div>
        );
    }

    if (allowedRoles && !allowedRoles.includes(user.role)) {
        return (
            <div style={{ padding: "2rem", textAlign: "center" }}>
                <p>You do not have permission to view this page.</p>
            </div>
        );
    }

    return children;
}