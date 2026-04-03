import { useState } from "react";
import { register } from "../api/auth";

export default function RegisterPage() {
    const [form, setForm] = useState({
        fullName: "", email: "", password: "", address: "",
    });
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) =>
        setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(""); setSuccess("");
        setLoading(true);
        try {
            await register({ ...form, role: "CUSTOMER" });
            setSuccess("Account created! Redirecting to login...");
            setTimeout(() => (window.location.href = "/login"), 1500);
        } catch (err) {
            setError(err.response?.data?.message || "Registration failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Create Account</h2>
                <p className="auth-sub">Join PharmaCare today</p>
                {error && <div className="alert alert-error">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}
                <form onSubmit={handleSubmit}>
                    {[
                        { label: "Full Name", name: "fullName", type: "text", placeholder: "John Doe" },
                        { label: "Email", name: "email", type: "email", placeholder: "you@example.com" },
                        { label: "Password", name: "password", type: "password", placeholder: "••••••••" },
                        { label: "Delivery Address", name: "address", type: "text", placeholder: "123 Main St" },
                    ].map(({ label, name, type, placeholder }) => (
                        <div className="form-group" key={name}>
                            <label>{label}</label>
                            <input
                                type={type} name={name} value={form[name]}
                                onChange={handleChange} required placeholder={placeholder}
                            />
                        </div>
                    ))}
                    <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
                        {loading ? "Creating account..." : "Create Account"}
                    </button>
                </form>
                <p className="auth-link">Already have an account? <a href="/login">Sign in</a></p>
            </div>
        </div>
    );
}