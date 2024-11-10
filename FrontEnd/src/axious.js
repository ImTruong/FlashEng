// src/axios.js
import axios from 'axios';

// Tạo instance Axios
const instance = axios.create({
  baseURL: 'http://localhost:8080/', // URL cơ bản cho tất cả các yêu cầu
  timeout: 10000, // Thời gian timeout
  headers: {
    'Content-Type': 'application/json', // Định dạng dữ liệu
  },
});

// Thiết lập header Authorization
// axios.defaults.headers.common['Authorization'] = Bearer ${localStorage.getItem('token')};

instance.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});


// Có thể thêm interceptor nếu cần
instance.interceptors.response.use(
  response => response,
  error => {
    // Xử lý lỗi toàn cục tại đây
    return Promise.reject(error);
  }
);

export default instance;