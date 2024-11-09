// src/axios.js
import axios from 'axios';

// Tạo instance axios với cấu hình chung
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // Đặt base URL cho các request
  timeout: 10000, // Thời gian timeout (10 giây)
  headers: {
    'Content-Type': 'application/json',
  },
});

// Thêm interceptor để xử lý request và thêm token Authorization vào header nếu có
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token'); // Lấy token từ localStorage
    if (token) {
      // Nếu có token, thêm vào header Authorization
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Bạn có thể xử lý lỗi tại đây nếu cần
    if (error.response) {
      console.error('API Error:', error.response.status, error.response.data);
    } else {
      console.error('Network or Axios error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
