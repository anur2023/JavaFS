export default function MedicineCard({ medicine, onAddToCart }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{medicine.name}</h3>
                {medicine.requiresPrescription && (
                    <span className="badge badge-warning">Rx Required</span>
                )}
            </div>
            <p className="card-category">{medicine.category?.categoryName}</p>
            <p className="card-dosage">{medicine.dosage} · {medicine.packaging}</p>
            <p className="card-description">{medicine.description}</p>
            <div className="card-footer">
                <span className="card-price">₹{medicine.price}</span>
                <span className="card-stock">Stock: {medicine.stockQuantity}</span>
                <button
                    className="btn btn-primary"
                    onClick={() => onAddToCart(medicine)}
                    disabled={medicine.stockQuantity === 0}
                >
                    {medicine.stockQuantity === 0 ? "Out of Stock" : "Add to Cart"}
                </button>
            </div>
        </div>
    );
}