import api from '../Administration/api.js';

const API_URL = "/api";

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


export const requestAddDevice = (name, type, extraParams) => {
    const params = new URLSearchParams();
    params.append('name', name);
    params.append('type', type);
    params.append('area', extraParams.area);
    if (extraParams.maxPower) params.append('maxPower', extraParams.maxPower);
    if (extraParams.minWind) params.append('minWind', extraParams.minWind);

    return api.post(`${API_URL}/requests/add`, null, { params });
};


export const requestRemoveDevice = (deviceId) => {
    return api.post(`${API_URL}/requests/remove/${deviceId}`);
};

export const getPendingRequests = () => {
    return api.get(`${API_URL}/requests/pending`);
};

export const approveRequest = (id) => {
    return api.post(`${API_URL}/requests/${id}/approve`);
};

export const rejectRequest = (id) => {
    return api.post(`${API_URL}/requests/${id}/reject`);
};
