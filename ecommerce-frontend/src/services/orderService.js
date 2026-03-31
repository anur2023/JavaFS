import api from './api';

export const placeOrder = (data) => api.post('/orders', data);
export const getMyOrders = () => api.get('/orders/my');
export const cancelOrder = (orderId) => api.put(`/orders/${orderId}/cancel`);