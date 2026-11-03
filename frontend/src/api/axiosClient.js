import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // Always send JSESSIONID cookie
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

    if (status === 401 && currentPath !== '/login') {
      sessionStorage.setItem('auth_message', 'Your session expired. Please sign in again.');
      window.location.href = '/login';
    } else if (status === 403) {
      window.location.href = '/403';
    }
    return Promise.reject(error);
  }
);

export default axiosClient;