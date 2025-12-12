import axios from 'axios';


const API_URL = "http://localhost:8080/register";

export const registerUser = async (data) => {
    const response = await axios.post(API_URL,data);
    return response.data;
};