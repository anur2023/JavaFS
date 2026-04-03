import { useState } from "react";
import { placeOrder } from "../api/orders";
import { useAuth } from "../context/AuthContext";
import CartItem from "../components/CartItem";

export default function CartPage() {
    const { user } = useAuth();
    const [cart, setCart] = useState(() => {
        const saved = localStorage.getItem("cart");
        return saved ? JSON.parse(saved) : [];
    });
    const [prescription, setPrescription] = useState(null);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const needsPrescription = cart.some((i) => i.requiresPrescription);
    const total = cart.reduce((s, i) => s + i.price * i.quantity, 0);

    const updateCart = (updated) => {
        setCart(updated);
        localStorage.setItem("cart", JSON.stringify(updated));
    };

    const removeItem = (id) => updateCart(cart.filter((i) => i.id !== id));

    const changeQty = (id, qty) => {
        if (qty < 1) return removeItem(id);
        updateCart(cart.map((i) => (i.id === id ? { ...i, quantity: qty } : i)));
    };

    const handleOrder = async () => {
        if (!user) { window.location.href = "/login"; return; }
        if (needsPrescription && !prescription) {
            setMessage("Please upload a prescription for Rx medicines.");
            return;
        }
        setLoading(true);
        try {
            const formData = new FormData();
            const orderData = {
                deliveryAddress: user.address || "Default Address",
                items: cart.map((i) => ({ medicineId: i.id, quantity: i.quantity })),
            };
            formData.append("order", new Blob([JSON.stringify(orderData)], { type: "application/json" }));
            if (prescription) formData.append("prescription", prescription);
            await placeOrder(formData);
            setMessage("Order placed successfully!");
            updateCart([]);
        } catch (err) {
            setMessage(err.response?.data?.message || "Failed to place order.");
        } finally {
            setLoading(false);
        }
    };

    if (cart.length === 0) {
        return (
            <div className="page-container" style={{ textAlign: "center" }}>
                <h2>Your cart is empty</h2>
                <a href="/medicines" className="btn btn-primary">Browse Medicines</a>
            </div>
        );
    }

    return (
        <div className="page-container">
            <h1>Your Cart</h1>
            {message && <div className="alert alert-info">{message}</div>}
            <div className="cart-list">
                {cart.map((item) => (
                    <CartItem key={item.id} item={item} onRemove={removeItem} onChangeQty={changeQty} />
                ))}
            </div>
            <div className="cart-summary">
                <span className="cart-total">Total: ₹{total.toFixed(2)}</span>
                {needsPrescription && (
                    <div className="form-group">
                        <label>Upload Prescription (required)</label>
                        <input
                            type="file" accept="image/*,.pdf"
                            onChange={(e) => setPrescription(e.target.files[0])}
                        />
                    </div>
                )}
                <button className="btn btn-primary" onClick={handleOrder} disabled={loading}>
                    {loading ? "Placing order..." : "Place Order"}
                </button>
            </div>
        </div>
    );
}