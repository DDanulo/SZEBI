import api from './api.js';
import {jwtDecode} from 'jwt-decode';




export const loginUser = async (data) => {
    try {
        const response = await api.post("login", data);
        const { accessToken, refreshToken } = response.data;

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        const decoded = jwtDecode(accessToken);
        setUser({ userId: decoded.userId, role: decoded.role });

        return true;

    } catch (error) {
        console.error('Błąd logowania:', error.response?.data?.message || 'Nieznany błąd.');
        return false;
    }
};
