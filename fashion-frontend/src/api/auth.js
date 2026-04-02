import api from './client'

export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  registerAdmin: (data) => api.post('/auth/admin/register', data),
}
