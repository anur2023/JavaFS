import api from './client'

export const productApi = {
  // Customer
  getAll: () => api.get('/products'),
  getById: (id) => api.get(`/products/${id}`),
  search: (q) => api.get(`/products/search?q=${q}`),
  getByCategory: (cat) => api.get(`/products/category/${cat}`),

  // Admin
  create: (data) => api.post('/admin/products', data),
  update: (id, data) => api.put(`/admin/products/${id}`, data),
  delete: (id) => api.delete(`/admin/products/${id}`),
  addImage: (id, data) => api.post(`/admin/products/${id}/images`, data),
}
