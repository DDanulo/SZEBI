import api from "../Administration/api.js";

const api_pred = "/api/predictions"

export const getLatestPrediction = async function () {
    return await api.get(`${api_pred}/latest/one`)
}

export const getLatestPredictions = async function () {
    return await api.get(`${api_pred}/latest/batch`)
}

export const generatePrediction = async function () {
    return await api.put(`${api_pred}/generate`)
}

export const getPredictionsByDateRange = function (fromDate, toDate) {
    return api.get(`${api_pred}/forecasts/${new Date(fromDate).getTime()}/${new Date(toDate).getTime()}`);
};
