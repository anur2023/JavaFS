import api from './api';

// ── Orders ──────────────────────────────────────────────
export const getAllOrders = () => api.get('/orders/admin/all');

// ── Categories ──────────────────────────────────────────
export const getAllCategories  = ()           => api.get('/categories');
export const createCategory    = (data)       => api.post('/categories/admin/create', data);
export const updateCategory    = (id, data)   => api.put(`/categories/admin/update/${id}`, data);
export const deleteCategory    = (id)         => api.delete(`/categories/admin/delete/${id}`);

// ── Vendors ─────────────────────────────────────────────
export const getAllVendors  = ()   => api.get('/vendors');
export const deleteVendor  = (id) => api.delete(`/vendors/admin/${id}`);