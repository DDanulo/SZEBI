/*
import api from '../Administration/api.js';



export const getSchedulesByDevice = (deviceId) => {
    return api.get(`api/schedules/device/${deviceId}`);
};

export const addSchedule = (schedule) => {
    return api.post(`api/schedules`, schedule);
};

export const deleteSchedule = (id) => {
    return api.delete(`api/schedules/${id}`);
};


export const getAllDevices = () => {
    return api.get(`api/devices`);
};

export const turnDeviceOn = (id) => {
    return api.post(`api/devices/${id}/turn-on`);
};

export const turnDeviceOff = (id) => {
    return api.post(`api/devices/${id}/turn-off`);
};
export const addDevice = (name, type, params = {}) => {
    return api.post(`api/devices`, null, {
        params: {
            name,
            type,
            ...params
        }
    });
};

export const removeDevice = (id) => {
    return api.delete(`api/devices/${id}`);
};*/
import api from '../Administration/api.js'; // Importujemy Twoją instancję axios z tokenem

const API_URL = "/api"; // api.js ma już baseURL, więc tu wystarczy ścieżka względna

// --- URZĄDZENIA (ADMIN/ENGINEER) ---

export const getAllDevices = () => {
    return api.get(`${API_URL}/devices`);
};

export const addDevice = (name, type, extraParams) => {
    const params = new URLSearchParams();
    params.append('name', name);
    params.append('type', type);
    params.append('area', extraParams.area);
    if (extraParams.maxPower) params.append('maxPower', extraParams.maxPower);
    if (extraParams.minWind) params.append('minWind', extraParams.minWind);

    return api.post(`${API_URL}/devices`, null, { params });
};

export const removeDevice = (id) => {
    return api.delete(`${API_URL}/devices/${id}`);
};

export const turnDeviceOn = (id) => {
    return api.post(`${API_URL}/devices/${id}/turn-on`);
};

export const turnDeviceOff = (id) => {
    return api.post(`${API_URL}/devices/${id}/turn-off`);
};

// --- HARMONOGRAMY ---

export const getSchedulesByDevice = (deviceId) => {
    return api.get(`${API_URL}/schedules/device/${deviceId}`);
};

export const addSchedule = (schedule) => {
    return api.post(`${API_URL}/schedules`, schedule);
};

export const deleteSchedule = (id) => {
    return api.delete(`${API_URL}/schedules/${id}`);
};

// --- WNIOSKI (REQUESTS) ---

// 1. Resident wysyła wniosek o dodanie
export const requestAddDevice = (name, type, extraParams) => {
    const params = new URLSearchParams();
    params.append('name', name);
    params.append('type', type);
    params.append('area', extraParams.area);
    if (extraParams.maxPower) params.append('maxPower', extraParams.maxPower);
    if (extraParams.minWind) params.append('minWind', extraParams.minWind);

    return api.post(`${API_URL}/requests/add`, null, { params });
};

// 2. Resident wysyła wniosek o usunięcie
export const requestRemoveDevice = (deviceId) => {
    return api.post(`${API_URL}/requests/remove/${deviceId}`);
};

// 3. Admin pobiera listę oczekujących wniosków
export const getPendingRequests = () => {
    return api.get(`${API_URL}/requests/pending`);
};

// 4. Admin zatwierdza
export const approveRequest = (id) => {
    return api.post(`${API_URL}/requests/${id}/approve`);
};

// 5. Admin odrzuca
export const rejectRequest = (id) => {
    return api.post(`${API_URL}/requests/${id}/reject`);
};
