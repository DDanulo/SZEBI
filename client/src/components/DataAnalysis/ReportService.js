import axios from 'axios';

const API_URL = 'http://localhost:8080/api/reports';

export const getChartData = (from, to, type) => {
    return axios.get(`${API_URL}/chart-data`, {
        params: { from, to, type }
    });
};

export const downloadPdfReport = (from, to) => {
    return axios.get(`${API_URL}/download-pdf`, {
        params: { from, to },
        responseType: 'blob',
    });
};