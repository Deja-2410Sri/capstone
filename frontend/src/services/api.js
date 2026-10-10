import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  verifyOtp: (data) => api.post('/auth/verify-otp', data),
  getMe: () => api.get('/auth/me'),
};

export const profileAPI = {
  get: () => api.get('/profile'),
  update: (data) => api.put('/profile', data),
};

export const farmAPI = {
  getAll: (params) => api.get('/farms', { params }),
  getById: (id) => api.get(`/farms/${id}`),
  create: (data) => api.post('/farms', data),
  update: (id, data) => api.put(`/farms/${id}`, data),
  delete: (id) => api.delete(`/farms/${id}`),
};

export const cropAPI = {
  search: (params) => api.get('/crops', { params }),
  getById: (id) => api.get(`/crops/${id}`),
  create: (data) => api.post('/crops', data),
  update: (id, data) => api.put(`/crops/${id}`, data),
  delete: (id) => api.delete(`/crops/${id}`),
};

export const recommendationAPI = {
  generate: () => api.post('/recommendations'),
  getAll: () => api.get('/recommendations'),
  getById: (id) => api.get(`/recommendations/${id}`),
};

export const guidelineAPI = {
  getAll: () => api.get('/guidelines'),
  getByCropId: (cropId) => api.get(`/guidelines/${cropId}`),
  create: (data) => api.post('/guidelines', data),
  update: (id, data) => api.put(`/guidelines/${id}`, data),
  delete: (id) => api.delete(`/guidelines/${id}`),
};

export const diseaseAPI = {
  getAll: () => api.get('/diseases'),
  getByCropId: (cropId) => api.get(`/diseases/${cropId}`),
  create: (data) => api.post('/diseases', data),
  update: (id, data) => api.put(`/diseases/${id}`, data),
  delete: (id) => api.delete(`/diseases/${id}`),
};

export const adviceAPI = {
  ask: (data) => api.post('/advice', data),
  getAll: (params) => api.get('/advice', { params }),
  answer: (id, data) => api.put(`/advice/${id}/answer`, data),
};

export const notificationAPI = {
  getAll: () => api.get('/notifications'),
  getUnreadCount: () => api.get('/notifications/unread'),
  markAsRead: (id) => api.put(`/notifications/${id}/read`),
  markAllAsRead: () => api.put('/notifications/read-all'),
};

export const weatherAPI = {
  getByLocation: (location) => api.get(`/weather/${location}`),
};

export const marketPriceAPI = {
  getAll: () => api.get('/market-prices'),
  getByCropId: (cropId) => api.get(`/market-prices/${cropId}`),
};

export const adminAPI = {
  getDashboard: () => api.get('/admin/dashboard'),
  getUsers: (params) => api.get('/admin/users', { params }),
  getCrops: (params) => api.get('/admin/crops', { params }),
  getAdvice: (params) => api.get('/admin/advice', { params }),
  getAuditLogs: (params) => api.get('/admin/audit-logs', { params }),
};

export default api;

