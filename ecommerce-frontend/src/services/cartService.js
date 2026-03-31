import api from './api';

export const getCart = () => api.get('/cart');
export const addToCart = (data) => api.post('/cart/add', data);
export const removeFromCart = (cartItemId) => api.delete(`/cart/remove/${cartItemId}`);
export const clearCart = () => api.delete('/cart/clear');