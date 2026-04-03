import { useEffect, useState } from "react";
import { getAllMedicines, addMedicine, updateMedicine, deleteMedicine } from "../api/medicines";
import ProtectedRoute from "../components/ProtectedRoute";

const EMPTY_FORM = { name: "", dosage: "", packaging: "", price: "", stockQuantity: "", description: "", requiresPrescription: false, categoryId: "" };

export default function AdminDashboard() {
    const [medicines, setMedicines] = useState([]);
    const [form, setForm] = useState(EMPTY_FORM);
    const [editId, setEditId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    const load = () => {
        getAllMedicines()
            .then((r) => setMedicines(r.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(load, []);

    const handleChange = (e) => {
        const val = e.target.type === "checkbox" ? e.target.checked : e.target.value;
        setForm({ ...form, [e.target.name]: val });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editId) {
                await updateMedicine(editId, form);
                setMessage("Medicine updated.");
            } else {
                await addMedicine(form);
                setMessage("Medicine added.");
            }
            setForm(EMPTY_FORM);
            setEditId(null);
            load();
        } catch (err) {
            setMessage(err.response?.data?.message || "Operation failed.");
        }
    };

    const startEdit = (m) => {
        setEditId(m.medicineId);
        setForm({
            name: m.name, dosage: m.dosage, packaging: m.packaging,
            price: m.price, stockQuantity: m.stockQuantity,
            description: m.description, requiresPrescription: m.requiresPrescription,
            categoryId: m.category?.categoryId || "",
        });
    };

    const handleDelete = async (id) => {
        if (!confirm("Delete this medicine?")) return;
        await deleteMedicine(id);
        load();
    };

    return (
        <ProtectedRoute allowedRoles={["ADMIN"]}>
            <div className="page-container">
                <h1>Admin — Medicine Management</h1>
                {message && <div className="alert alert-info">{message}</div>}

                <div className="card" style={{ marginBottom: "2rem" }}>
                    <h3>{editId ? "Edit Medicine" : "Add New Medicine"}</h3>
                    <form onSubmit={handleSubmit} className="admin-form">
                        {[
                            { name: "name", label: "Name", type: "text" },
                            { name: "dosage", label: "Dosage", type: "text" },
                            { name: "packaging", label: "Packaging", type: "text" },
                            { name: "price", label: "Price", type: "number" },
                            { name: "stockQuantity", label: "Stock", type: "number" },
                            { name: "categoryId", label: "Category ID", type: "number" },
                            { name: "description", label: "Description", type: "text" },
                        ].map(({ name, label, type }) => (
                            <div className="form-group" key={name}>
                                <label>{label}</label>
                                <input type={type} name={name} value={form[name]} onChange={handleChange} required />
                            </div>
                        ))}
                        <div className="form-group">
                            <label>
                                <input type="checkbox" name="requiresPrescription" checked={form.requiresPrescription} onChange={handleChange} />
                                {" "}Requires Prescription
                            </label>
                        </div>
                        <div style={{ display: "flex", gap: "0.5rem" }}>
                            <button type="submit" className="btn btn-primary">{editId ? "Update" : "Add"}</button>
                            {editId && (
                                <button type="button" className="btn btn-outline" onClick={() => { setEditId(null); setForm(EMPTY_FORM); }}>
                                    Cancel
                                </button>
                            )}
                        </div>
                    </form>
                </div>

                {loading ? <div className="loading">Loading...</div> : (
                    <div className="table-wrapper">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>Name</th><th>Dosage</th><th>Price</th><th>Stock</th><th>Rx</th><th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {medicines.map((m) => (
                                <tr key={m.medicineId}>
                                    <td>{m.name}</td>
                                    <td>{m.dosage}</td>
                                    <td>₹{m.price}</td>
                                    <td>{m.stockQuantity}</td>
                                    <td>{m.requiresPrescription ? "Yes" : "No"}</td>
                                    <td>
                                        <button className="btn btn-outline" onClick={() => startEdit(m)}>Edit</button>{" "}
                                        <button className="btn btn-danger" onClick={() => handleDelete(m.medicineId)}>Delete</button>
                                    </td>
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