import axios from 'axios';

const API_URL = 'http://localhost:8080/api/reports';

export const getDevices = () => {
    return axios.get(`${API_URL}/devices`);
};

const toDeviceIdsParam = (deviceIds) => {
    if (!deviceIds || deviceIds.length === 0) return undefined;
    return deviceIds.join(',');
};

export const getChartData = (from, to, type, deviceIds = []) => {
    return axios.get(`${API_URL}/chart-data`, {
        params: { from, to, type, deviceIds: toDeviceIdsParam(deviceIds) }
    });
};

export const getDataTotal = (from, to, type, deviceIds = []) => {
    return axios.get(`${API_URL}/total`, {
        params: { from, to, type, deviceIds: toDeviceIdsParam(deviceIds) }
    });
};

export const getDataAverages = (from, to, type, deviceIds = []) => {
    return axios.get(`${API_URL}/average`, {
        params: { from, to, type, deviceIds: toDeviceIdsParam(deviceIds) }
    });
};

export const getAggregatedData = (from, to, type, deviceIds = []) => {
    return axios.get(`${API_URL}/aggregated`, {
        params: { from, to, type, deviceIds: toDeviceIdsParam(deviceIds) }
    });
};

export const downloadPdfReport = (from, to, deviceIds = [], granularity = 'daily') => {
    return axios.get(`${API_URL}/download-pdf`, {
        params: { from, to, deviceIds: toDeviceIdsParam(deviceIds), granularity },
        responseType: 'blob',
    });
};