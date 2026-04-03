import { useEffect, useState } from "react";
import { getAllMedicines } from "../api/medicines";
import MedicineCard from "../components/MedicineCard";

export default function MedicineCatalogPage() {
    const [medicines, setMedicines] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);
    const [cart, setCart] = useState(() => {
        const saved = localStorage.getItem("cart");
        return saved ? JSON.parse(saved) : [];
    });

    useEffect(() => {
        getAllMedicines()
            .then((r) => setMedicines(r.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    const addToCart = (medicine) => {
        const existing = cart.find((i) => i.id === medicine.medicineId);
        let updated;
        if (existing) {
            updated = cart.map((i) =>
                i.id === medicine.medicineId ? { ...i, quantity: i.quantity + 1 } : i
            );
        } else {
            updated = [...cart, {
                id: medicine.medicineId,
                name: medicine.name,
                price: medicine.price,
                requiresPrescription: medicine.requiresPrescription,
                quantity: 1,
            }];
        }
        setCart(updated);
        localStorage.setItem("cart", JSON.stringify(updated));
        alert(`${medicine.name} added to cart!`);
    };

    const filtered = medicines.filter((m) =>
        m.name.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <div className="page-container">
            <div className="page-header">
                <h1>Medicine Catalog</h1>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Search medicines..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
            </div>
            {loading ? (
                <div className="loading">Loading medicines...</div>
            ) : filtered.length === 0 ? (
                <p>No medicines found.</p>
            ) : (
                <div className="card-grid">
                    {filtered.map((m) => (
                        <MedicineCard
                            key={m.medicineId}
                            medicine={m}
                            onAddToCart={addToCart}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}