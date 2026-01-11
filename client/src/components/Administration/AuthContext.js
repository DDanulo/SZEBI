// src/context/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/api';
import {jwtDecode} from 'jwt-decode';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // sprawdzenie tokena
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            try {
                // decoding
                const decoded = jwtDecode(token);
                // sprawdzenie ważności tokenu
                if (decoded.exp * 1000 < Date.now()) {
                    logout();
                } else {
                    setUser({ userId: decoded.userId, role: decoded.role });
                }
            } catch (error) {
                console.error("Invalid token:", error);
                logout();
            }
        }
        setLoading(false);
    }, []);

    // funkcja wejscia
    const login = async (login, password) => {
        try {
            const response = await api.post('/auth/login', { login, password });
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

    // rejestracja
    const register = async (login, password) => {
        try {
            const response = await api.post('/auth/register', { login, password });

            return response.status === 201;

        } catch (error) {
            console.error('Błąd rejestracji:', error.response?.data?.message || 'Nieznany błąd.');

            throw new Error(error.response?.data?.message || 'Błąd serwera podczas rejestracji');
        }
    };

    // logout
    const logout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setUser(null);
    };

    // sprawdzenie roli
    const hasRole = (role) => {
        return user && user.role === role;
    }

    const value = {
        user,
        loading,
        login,
        logout,
        register,
        hasRole
    };

    if (loading) {
        return <div>Ładowanie danych użytkownika...</div>;
    }

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};