import axios from "axios";

const API = "http://localhost:8080/prediction"

export const getLatestPrediction = function () {
    return axios.get(`${API}/latest/one`, {
        responseType: "json"
    })
}

export const getLatestPredictions = function () {
    return axios.get(`${API}/latest/batch`)
}

export const generatePrediction = function () {
    return axios.put(`${API}/generate`)
}
