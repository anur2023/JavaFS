import api from './api';

export const getAllProducts = () => api.get('/products');
export const getProductById = (id) => api.get(`/products/${id}`);