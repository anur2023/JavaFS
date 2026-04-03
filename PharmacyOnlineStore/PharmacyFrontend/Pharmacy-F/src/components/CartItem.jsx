export default function CartItem({ item, onRemove, onChangeQty }) {
    return (
        <div className="cart-item">
            <div className="cart-item-info">
                <span className="cart-item-name">{item.name}</span>
                <span className="cart-item-price">₹{item.price} each</span>
            </div>
            <div className="cart-item-controls">
                <button onClick={() => onChangeQty(item.id, item.quantity - 1)}>−</button>
                <span>{item.quantity}</span>
                <button onClick={() => onChangeQty(item.id, item.quantity + 1)}>+</button>
                <button className="btn-danger" onClick={() => onRemove(item.id)}>Remove</button>
            </div>
            <span className="cart-item-subtotal">₹{item.price * item.quantity}</span>
        </div>
    );
}