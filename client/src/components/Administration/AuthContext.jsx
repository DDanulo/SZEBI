// src/context/AuthContext.jsx
import React, {createContext, useContext, useState, useEffect} from 'react';
import api from './api.js';
import {jwtDecode} from 'jwt-decode';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({children}) => {

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
                    setUser({userId: decoded.userId, role: decoded.role, login: decoded.login});
                }
            } catch (error) {
                console.error("Invalid token:", error);
                logout();
            }
        }
        setLoading(false);
    }, []);


    const login = async (login, password) => {

        const response = await api.post('/login', {login, password});
        const {accessToken, refreshToken} = response.data;
        console.log(response.status)
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        const decoded = jwtDecode(accessToken);
        console.log(decoded)
        setUser({userId: decoded.sub, role: decoded.role, login: decoded.login});

        return response;


    };


    const register = async (login, password, firstName, lastName, email, room) => {
        try {
            const response = await api.post('/register', {login, password, firstName, lastName, email, room});

            return response.status === 201;

        } catch (error) {
            throw error
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