function ProductCard({ product, onAddToCart }) {
    return (
        <div className="pcard">
            <div className="pcard-top">
                <div className="pcard-badges">
                    <span className="pcard-category">{product.categoryName}</span>
                </div>
                <h3 className="pcard-name">{product.name}</h3>
                <p className="pcard-desc">{product.description || 'No description available.'}</p>
            </div>

            <div className="pcard-bottom">
                <div className="pcard-store">🏪 {product.vendorStoreName}</div>
                <div className="pcard-footer">
                    <span className="pcard-price">₹{Number(product.price).toLocaleString('en-IN')}</span>
                    <button className="pcard-btn" onClick={() => onAddToCart(product)}>Add to Cart</button>
                </div>
            </div>

            <style>{`
        .pcard {
          background: #ffffff;
          border: 1px solid #e2e8f0;
          border-radius: 12px;
          padding: 20px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          gap: 16px;
          transition: box-shadow 0.2s, border-color 0.2s;
        }
        .pcard:hover {
          box-shadow: 0 4px 20px rgba(37,99,235,0.1);
          border-color: #bfdbfe;
        }
        .pcard-top { display: flex; flex-direction: column; gap: 10px; }
        .pcard-badges { display: flex; gap: 6px; flex-wrap: wrap; }
        .pcard-category {
          background: #eff6ff;
          color: #2563eb;
          font-size: 11px;
          font-weight: 600;
          padding: 3px 10px;
          border-radius: 20px;
          text-transform: uppercase;
          letter-spacing: 0.4px;
        }
        .pcard-name {
          font-size: 15px;
          font-weight: 700;
          color: #1e293b;
          line-height: 1.4;
        }
        .pcard-desc {
          font-size: 13px;
          color: #64748b;
          line-height: 1.6;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        .pcard-bottom { display: flex; flex-direction: column; gap: 12px; }
        .pcard-store { font-size: 12px; color: #94a3b8; }
        .pcard-footer { display: flex; align-items: center; justify-content: space-between; }
        .pcard-price { font-size: 20px; font-weight: 700; color: #1e293b; }
        .pcard-btn {
          background: #2563eb;
          color: #fff;
          border: none;
          padding: 8px 16px;
          border-radius: 7px;
          font-size: 13px;
          font-weight: 600;
          cursor: pointer;
          font-family: 'Inter', sans-serif;
          transition: background 0.15s;
          white-space: nowrap;
        }
        .pcard-btn:hover { background: #1d4ed8; }
      `}</style>
        </div>
    );
}

export default ProductCard;