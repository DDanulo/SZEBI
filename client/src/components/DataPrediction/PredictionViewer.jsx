import React, {useEffect, useState} from "react";
import {generatePrediction, getLatestPrediction} from "./PredictionService.js";

export default function PredictionViewer() {
    const [forecast, setForecast] = useState(null);
    const [loading, setLoading] = useState(true);

    const generateForecast = async function () {
        generatePrediction()
        let p = await getLatestPrediction()
        console.log(p.data)
        setForecast(p.data)
    }

    useEffect(() => {
        fetch("http://localhost:8080/prediction/latest")
            .then(response => response.json())
            .then(data => {
                setForecast(data);
                setLoading(false);
            })
    }, []);

    if (loading) {
        return <>
            <button onClick={generateForecast} title="Wygeneruj prognozę">Wygeneruj prognozę</button>
            <p>Pobieranie ostatniej prognozy...</p>
        </>
    }

    return <>
        <h2>🤖Moduł Prognozowania</h2>
        <button onClick={generateForecast} title="Wygeneruj prognozę">Wygeneruj prognozę</button>
        <h3>Prognoza zużycia energii elektrycznej na dzień: {DisplayDate(forecast.forecastDate)}</h3>
        <table className="table-auto">
            <thead>
            <tr>
                <th>Czas wygenerowania</th>
                <th>Zużycie [kWh]</th>
                <th>Prognoza na dzień</th>
            </tr>
            </thead>
            <tbody>
            {ForecastsList([forecast])}
            </tbody>
        </table>
    </>
}

function ForecastsList(forecasts) {
    return forecasts.map((f) => {
        return <tr>
            <td>{f.creationTime}</td>
            <td>{f.forecastedUsage}</td>
            <td>{DisplayDate(f.forecastDate)}</td>
        </tr>
    })
}

function DisplayDate(date) {
    let d = new Date(date)
    return d.getDate() + " " + (d.getMonth() + 1) + " " + d.getFullYear()
}