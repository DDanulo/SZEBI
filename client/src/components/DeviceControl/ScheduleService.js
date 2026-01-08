import axios from 'axios';


const API_URL = 'http://localhost:8080/api';

export const getSchedulesByDevice = (deviceId) => {
    return axios.get(`${API_URL}/schedules/device/${deviceId}`);
};

export const addSchedule = (schedule) => {
    return axios.post(`${API_URL}/schedules`, schedule);
};

export const deleteSchedule = (id) => {
    return axios.delete(`${API_URL}/schedules/${id}`);
};


export const getAllDevices = () => {
    return axios.get(`${API_URL}/devices`);
};

export const turnDeviceOn = (id) => {
    return axios.post(`${API_URL}/devices/${id}/turn-on`);
};

export const turnDeviceOff = (id) => {
    return axios.post(`${API_URL}/devices/${id}/turn-off`);
};
export const addDevice = (name, type, params = {}) => {
    return axios.post(`${API_URL}/devices`, null, {
        params: {
            name,
            type,
            ...params
        }
    });
};

export const removeDevice = (id) => {
    return axios.delete(`${API_URL}/devices/${id}`);
};