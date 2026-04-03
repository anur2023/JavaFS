import { useEffect, useState } from "react";
import { getPendingPrescriptions, validatePrescription } from "../api/prescriptions";
import ProtectedRoute from "../components/ProtectedRoute";

export default function PharmacistDashboard() {
    const [prescriptions, setPrescriptions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [reason, setReason] = useState({});

    useEffect(() => {
        getPendingPrescriptions()
            .then((r) => setPrescriptions(r.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    const handleValidate = async (id, status) => {
        try {
            await validatePrescription(id, status, reason[id] || "");
            setPrescriptions((prev) => prev.filter((p) => p.prescriptionId !== id));
        } catch (err) {
            alert(err.response?.data?.message || "Failed to validate");
        }
    };

    return (
        <ProtectedRoute allowedRoles={["PHARMACIST"]}>
            <div className="page-container">
                <h1>Pending Prescriptions</h1>
                {loading ? (
                    <div className="loading">Loading...</div>
                ) : prescriptions.length === 0 ? (
                    <p>No pending prescriptions.</p>
                ) : (
                    <div className="prescription-list">
                        {prescriptions.map((p) => (
                            <div className="card" key={p.prescriptionId}>
                                <p><strong>Prescription #{p.prescriptionId}</strong> — Order #{p.orderId}</p>
                                <p>Uploaded: {new Date(p.uploadedAt).toLocaleString()}</p>
                                {p.imageUrl && (
                                    <a href={p.imageUrl} target="_blank" rel="noreferrer" className="btn btn-outline">
                                        View Image
                                    </a>
                                )}
                                <div className="form-group" style={{ marginTop: "1rem" }}>
                                    <label>Rejection Reason (optional)</label>
                                    <input
                                        type="text"
                                        placeholder="Reason if rejecting..."
                                        value={reason[p.prescriptionId] || ""}
                                        onChange={(e) =>
                                            setReason({ ...reason, [p.prescriptionId]: e.target.value })
                                        }
                                    />
                                </div>
                                <div style={{ display: "flex", gap: "0.5rem", marginTop: "0.5rem" }}>
                                    <button className="btn btn-primary" onClick={() => handleValidate(p.prescriptionId, "approved")}>
                                        Approve
                                    </button>
                                    <button className="btn btn-danger" onClick={() => handleValidate(p.prescriptionId, "rejected")}>
                                        Reject
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </ProtectedRoute>
    );
}