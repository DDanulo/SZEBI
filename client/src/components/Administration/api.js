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
    (response) => response, // sukces – zwracamy bez zmian
    (error) => {
        if (error.response) {
            const { status, data } = error.response;


            if (status === 401) {
                console.warn('Unauthorized (401) – przekierowanie na login');
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                return Promise.reject(error);
            }


            if (status === 403) {
                console.warn('Forbidden (403) – brak uprawnień');

                return Promise.reject({
                    status,
                    message: data?.message || 'Brak uprawnień do wykonania tej operacji',
                    code: data?.code || 'FORBIDDEN',
                });
            }


            if (status === 400) {
                return Promise.reject({
                    status,
                    message: data?.message || 'Niepoprawne dane',
                    code: data?.code || 'BAD_REQUEST',
                    errors: data?.errors || [],
                });
            }


            if (status === 409) {
                return Promise.reject({
                    status,
                    message: data?.message || 'Konflikt – np. login już istnieje',
                    code: data?.code || 'CONFLICT',
                });
            }


            if (status >= 500) {
                console.error('Błąd serwera:', error);
                return Promise.reject({
                    status,
                    message: 'Wystąpił błąd po stronie serwera. Spróbuj ponownie później.',
                    code: 'SERVER_ERROR',
                });
            }


            return Promise.reject({
                status,
                message: data?.message || 'Wystąpił nieznany błąd',
                code: data?.code || status.toString(),
            });
        }


        if (error.request) {
            console.error('Błąd sieciowy:', error.request);
            return Promise.reject({
                message: 'Brak odpowiedzi od serwera. Sprawdź połączenie internetowe.',
                code: 'NETWORK_ERROR',
            });
        }


        return Promise.reject({
            message: error.message || 'Wystąpił nieznany błąd',
            code: 'UNKNOWN_ERROR',
        });
    }
);
export default api;