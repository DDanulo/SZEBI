import axios from 'axios';

const API_URL = 'http://localhost:8080/api/reports';

export const getChartData = (from, to, type) => {
    return axios.get(`${API_URL}/chart-data`, {
        params: { from, to, type }
    });
};

export const getDataTotal = (from, to, type) => {
    return axios.get(`${API_URL}/total`, {
        params: { from, to, type }
    });
};

export const getDataAverages = (from, to, type) => {
    return axios.get(`${API_URL}/average`, {
        params: { from, to, type }
    });
};

export const getAggregatedData = (from, to, type) => {
    return axios.get(`${API_URL}/aggregated`, {
        params: { from, to, type }
    });
};

export const downloadPdfReport = (from, to) => {
    return axios.get(`${API_URL}/download-pdf`, {
        params: { from, to },
        responseType: 'blob',
    });
};