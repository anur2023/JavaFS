import api from './api';

export const makePayment = (data) => api.post('/payments', data);
export const getPaymentByOrder = (orderId) => api.get(`/payments/order/${orderId}`);