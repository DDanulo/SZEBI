import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const ReportService = {
    getDevices: () => api.get('/reports/devices'),

    getChartData: (from, to, type, deviceIds = []) => {
        const params = new URLSearchParams({ from, to, type });
        deviceIds.forEach(id => params.append('deviceIds', id));
        return api.get('/reports/chart-data', { params });
    },

    getAggregated: (from, to, type) =>
        api.get('/reports/aggregated', { params: { from, to, type } }),

    getTotal: (from, to, type) =>
        api.get('/reports/total', { params: { from, to, type } }),

    downloadPdf: async (from, to, granularity = 'daily') => {
        const response = await api.get('/reports/download-pdf', {
            params: { from, to, granularity },
            responseType: 'blob', // Kluczowe dla plików binarnych
        });
        return response.data;
    }
};

export const AlertService = {
    getLogs: () => api.get('/alerts/logs'),

    triggerSos: (message) => api.post('/alerts/sos', { message }),
};