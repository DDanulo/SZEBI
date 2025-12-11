import axios from "axios";

const API = "http://localhost:8080/prediction"

export const getLatestPrediction = function () {
    return axios.get(`${API}/latest`, {
        responseType: "json"
    })
}

export const generatePrediction = function () {
    return axios.put(`${API}/generate`)
}
