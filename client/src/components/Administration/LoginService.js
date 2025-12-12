import axios from 'axios';


const API_URL = "http://localhost:8080/login";

export const loginUser = async (data) => {
    const response = await axios.post(API_URL,data);
    return response.data;
};
