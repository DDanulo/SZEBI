import axios from 'axios';


//toDo: zmienic url w kontrolerze
const url = 'http://localhost:8080/api/simulation';


export const getSimulationParameters = async () => {
    try {
        const res = await axios.get(`${url}/parameters`);
        return res.data;
    }catch (error) {
        alert(error);
    }
}

export const changeSeason = async(season) => {
    try {
        const res = await axios.put(`${url}/season`,{season: season} );
        return res.data;
    } catch (error) {
        if(error.status == 400) {
            alert("Wybierz porę roku!");
        }
    }
}

export const changeDaytime = async(daytime) => {
    try {
        const res = await axios.put(`${url}/daytime`,{timeOfDay: daytime} );
        return res.data;
    } catch (error) {
        if(error.status == 400) {
            alert("Wybierz porę dnia!");
        }
    }
}