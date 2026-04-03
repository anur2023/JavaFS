import { useEffect, useState } from "react";
import { getOrderHistory } from "../api/orders";
import OrderStatusBadge from "../components/OrderStatusBadge";
import ProtectedRoute from "../components/ProtectedRoute";

export default function OrderHistoryPage() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getOrderHistory()
            .then((r) => setOrders(r.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    return (
        <ProtectedRoute allowedRoles={["CUSTOMER"]}>
            <div className="page-container">
                <h1>My Orders</h1>
                {loading ? (
                    <div className="loading">Loading orders...</div>
                ) : orders.length === 0 ? (
                    <p>You haven't placed any orders yet.</p>
                ) : (
                    <div className="table-wrapper">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th>Payment</th>
                            </tr>
                            </thead>
                            <tbody>
                            {orders.map((o) => (
                                <tr key={o.orderId}>
                                    <td>#{o.orderId}</td>
                                    <td>{new Date(o.orderDate).toLocaleDateString()}</td>
                                    <td>₹{o.totalAmount}</td>
                                    <td><OrderStatusBadge status={o.orderStatus} /></td>
                                    <td>{o.paymentStatus}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </ProtectedRoute>
    );
}