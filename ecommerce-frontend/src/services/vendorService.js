import api from './api';

// ── Vendor profile ───────────────────────────────────────
export const registerVendorProfile = (data) => api.post('/vendors/register', data);
export const getMyVendorProfile    = ()      => api.get('/vendors/me');
export const updateMyVendorProfile = (data) => api.put('/vendors/me', data);

// ── Products ─────────────────────────────────────────────
export const createProduct = (data)       => api.post('/products', data);
export const updateProduct = (id, data)   => api.put(`/products/${id}`, data);
export const deleteProduct = (id)         => api.delete(`/products/${id}`);
export const getVendorProducts = (vendorId) => api.get(`/products/vendor/${vendorId}`);

// ── Orders sold ──────────────────────────────────────────
export const getVendorOrders = (vendorId) => api.get(`/orders/vendor/${vendorId}/items`);

// ── Categories (read-only for vendor) ───────────────────
export const getAllCategories = () => api.get('/categories');