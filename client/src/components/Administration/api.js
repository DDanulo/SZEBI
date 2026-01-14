import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            console.warn("Unauthorized request (401). Performing fallback to login.");

            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');

            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;