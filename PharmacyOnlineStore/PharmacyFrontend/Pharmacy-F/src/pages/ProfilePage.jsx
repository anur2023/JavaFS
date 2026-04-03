import { useAuth } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";

export default function ProfilePage() {
    const { user } = useAuth();
    return (
        <ProtectedRoute>
            <div className="page-container">
                <h1>My Profile</h1>
                <div className="card" style={{ maxWidth: "500px" }}>
                    <div className="profile-row"><span>Name</span><span>{user?.fullName}</span></div>
                    <div className="profile-row"><span>Email</span><span>{user?.email}</span></div>
                    <div className="profile-row"><span>Role</span><span>{user?.role}</span></div>
                </div>
            </div>
        </ProtectedRoute>
    );
}