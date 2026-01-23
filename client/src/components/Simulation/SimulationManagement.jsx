import React, {useState, useEffect} from 'react';
import {changeDaytime, changeSeason} from "./SimulationService.js";
import {getSimulationParameters} from "./SimulationService.js";
import {seasonMappingProps, daytimeMappingProps} from "./responseMappingProps.js";

function Simulation() {

    const [dayTime, setDayTime] = useState("");
    const [season, setSeason] = useState("");
    const [temperature, setTemperature] = useState("");
    const [windSpeed, setWindSpeed] = useState("");
    const [insolation, setInsolation] = useState("");
    const [seasonToChange, setSeasonToChange] = useState("");
    const [daytimeToChange, setDaytimeToChange] = useState("");

    const seasons = seasonMappingProps;
    const daytimes = daytimeMappingProps;

    useEffect(() => {
        getSimulationParameters().then(e =>
            updateSimulationParameters(e)
        )
    }, []);


    function updateSimulationParameters(paramsFromApi) {
        setSeason(paramsFromApi.season);
        setDayTime(paramsFromApi.time_of_day);
        setInsolation(paramsFromApi.insolation);
        setTemperature(paramsFromApi.temperature);
        setWindSpeed(paramsFromApi.wind_speed);
    }

    return (
        <div className="flex flex-col items-center w-full">
            <div className="p-4 mx-auto">
                <h2>Aktualne parametry symulacji:</h2>
                <p>🗓️ Pora roku: {seasons[season]}</p>
                <p>🕙 Pora dnia: {daytimes[dayTime]}</p>
                <p>🌡️ Temperatura: {temperature} °C</p>
                <p>☀️ Nasłonecznienie: {insolation} W/m<sup>2</sup></p>
                <p>💨 Prędkość wiatru: {windSpeed} m/s</p>
                <button type="button" onClick={() => getSimulationParameters().then(e => {
                    updateSimulationParameters(e);
                })}>Pobierz aktualne dane:
                </button>
            </div>
            <div className="p-4">
                <h2>Zmień porę dnia: </h2>
                <select onChange={e => setDaytimeToChange(e.target.value)}>
                    <option value="">Wybierz</option>
                    <option value="early morning">Ranek (4:00 - 8:00)</option>
                    <option value="morning">Przedpołudnie (8:00-12:00)</option>
                    <option value="afternoon">Popołudnie (12:00 - 16:00)</option>
                    <option value="evening">Późne popołudnie (16:00 - 20:00)</option>
                    <option value="late evening">Wieczór (20:00 - 00:00)</option>
                    <option value="night">Noc (00:00 -04:00)</option>
                </select>
                <button onClick={() => changeDaytime(daytimeToChange).then(e => {
                    updateSimulationParameters(e);
                })} type="button">Zmień
                </button>
            </div>
            <div className="p-4">
                <h2>Zmień porę roku: </h2>
                <select onChange={e => setSeasonToChange(e.target.value)}>
                    <option value="">Wybierz</option>
                    <option value="spring">Wiosna (marzec -maj)</option>
                    <option value="summer">Lato (czerwiec - sierpień)</option>
                    <option value="autumn">Jesień (wrzesień - listopad)</option>
                    <option value="winter">Zima (grudzień -luty)</option>
                </select>
                <button onClick={() => changeSeason(seasonToChange).then(e => {
                    updateSimulationParameters(e);
                })} type="button">Zmień
                </button>
            </div>
        </div>

    )
}

export default Simulation;