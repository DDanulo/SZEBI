import axios from 'axios';


const API_URL = "http://localhost:8080/users/residents";


export const getAllResidents = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};


export const createResident =  async (data) => {
    const response = await axios.post(API_URL, data);
    return response.data;
};
