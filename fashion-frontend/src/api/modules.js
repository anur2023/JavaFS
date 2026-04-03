import api from './client'

export const wishlistApi = {
  get: () => api.get('/user/wishlist'),
  add: (productId) => api.post('/user/wishlist/add', { productId }),
  remove: (productId) => api.delete(`/user/wishlist/remove/${productId}`),
  clear: () => api.delete('/user/wishlist/clear'),
}

export const orderApi = {
  place: (data) => api.post('/user/orders/place', data),
  getMyOrders: () => api.get('/user/orders'),
  getById: (id) => api.get(`/user/orders/${id}`),
  cancel: (id) => api.patch(`/user/orders/${id}/cancel`),
  reorder: (id) => api.post(`/user/orders/${id}/reorder`),
  // Admin
  getAll: (status) => api.get(`/admin/orders${status ? `?status=${status}` : ''}`),
  updateStatus: (id, data) => api.patch(`/admin/orders/${id}/status`, data),
}

export const tryOnApi = {
  start: (data) => api.post('/user/tryon', data),
  getHistory: () => api.get('/user/tryon'),
  getSession: (id) => api.get(`/user/tryon/${id}`),
  deleteSession: (id) => api.delete(`/user/tryon/${id}`),
  // Admin
  getAll: () => api.get('/admin/tryon'),
  getByProduct: (id) => api.get(`/admin/tryon/product/${id}`),
}

export const lookbookApi = {
  // Customer
  getPublished: (params) => api.get('/user/lookbooks', { params }),
  getById: (id) => api.get(`/user/lookbooks/${id}`),
  save: (id) => api.post(`/user/lookbooks/${id}/save`),
  unsave: (id) => api.delete(`/user/lookbooks/${id}/save`),
  getSaved: () => api.get('/user/lookbooks/saved'),
  // Admin
  getAll: () => api.get('/admin/lookbooks'),
  create: (data) => api.post('/admin/lookbooks', data),
  update: (id, data) => api.put(`/admin/lookbooks/${id}`, data),
  delete: (id) => api.delete(`/admin/lookbooks/${id}`),
  publish: (id) => api.patch(`/admin/lookbooks/${id}/publish`),
  archive: (id) => api.patch(`/admin/lookbooks/${id}/archive`),
  addItem: (id, data) => api.post(`/admin/lookbooks/${id}/items`, data),
  removeItem: (lookbookId, productId) => api.delete(`/admin/lookbooks/${lookbookId}/items/${productId}`),
}

export const quizApi = {
  getAll: () => api.get('/user/quiz'),
  submit: (data) => api.post('/user/quiz/submit', data),
  getMyResponses: () => api.get('/user/quiz/my-responses'),
  // Admin
  create: (data) => api.post('/admin/quiz', data),
  delete: (id) => api.delete(`/admin/quiz/${id}`),
}

export const inspirationApi = {
  getAll: () => api.get('/inspiration'),
  getById: (id) => api.get(`/inspiration/${id}`),
  // Admin
  create: (data) => api.post('/admin/inspiration', data),
  update: (id, data) => api.put(`/admin/inspiration/${id}`, data),
  delete: (id) => api.delete(`/admin/inspiration/${id}`),
}

export const recommendationApi = {
  byCategory: (cat) => api.get(`/recommendations/category/${cat}`),
  byBrand: (brand) => api.get(`/recommendations/brand/${brand}`),
  similar: (id) => api.get(`/recommendations/similar/${id}`),
}
