import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const currentPath = window.location.pathname;
    const publicPaths = ['/', '/login', '/hospital/activate', '/forgot-password'];

    if (status === 401 && !publicPaths.includes(currentPath) && !currentPath.startsWith('/activate')) {
      sessionStorage.setItem('auth_message', 'Your session expired. Please sign in again.');
      window.location.href = '/login';
    } else if (status === 403 && currentPath !== '/login') {
      window.location.href = '/403';
    }
    return Promise.reject(error);
  }
);

export default axiosClient;