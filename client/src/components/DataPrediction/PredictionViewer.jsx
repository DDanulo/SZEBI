// import React, {useEffect, useState} from "react";
// import {generatePrediction, getLatestPrediction} from "./PredictionService.js";
//
// export default function PredictionViewer() {
//     const [forecast, setForecast] = useState(null);
//     const [loading, setLoading] = useState(true);
//
//     const generateForecast = async function () {
//         generatePrediction()
//         let p = await getLatestPrediction()
//         console.log(p.data)
//         setForecast(p.data)
//     }
//
//     useEffect(() => {
//         fetch("http://localhost:8080/prediction/latest/one")
//             .then(response => response.json())
//             .then(data => {
//                 setForecast(data);
//                 setLoading(false);
//             })
//     }, []);
//
//     if (loading) {
//         return <>
//             <button onClick={generateForecast} title="Wygeneruj prognozę">Wygeneruj prognozę</button>
//             <p>Pobieranie ostatniej prognozy...</p>
//         </>
//     }
//
//     return <>
//         <h2>🤖Moduł Prognozowania</h2>
//         <button onClick={generateForecast} title="Wygeneruj prognozę">Wygeneruj prognozę</button>
//         <h3>Prognoza zużycia energii elektrycznej na dzień: {DisplayDate(forecast.forecastDate)}</h3>
//         <table className="table-auto">
//             <thead>
//             <tr>
//                 <th>Czas wygenerowania</th>
//                 <th>Zużycie [kWh]</th>
//                 <th>Prognoza na dzień</th>
//             </tr>
//             </thead>
//             <tbody>
//             {ForecastsList([forecast])}
//             </tbody>
//         </table>
//     </>
// }
//
// function ForecastsList(forecasts) {
//     return forecasts.map((f) => {
//         return <tr id={f.id}>
//             <td>{f.creationTime}</td>
//             <td>{f.forecastedUsage}</td>
//             <td>{DisplayDate(f.forecastDate)}</td>
//         </tr>
//     })
// }
//
// function DisplayDate(date) {
//     let d = new Date(date)
//     return d.getDate() + " " + (d.getMonth() + 1) + " " + d.getFullYear()
// }

import React, { useEffect, useState } from 'react';
import { getLatestPredictions } from './PredictionService';
import { PredictionChart, PredictionTable } from './PredictionComponents';

const PredictionViewer = () => {
    const [rawData, setRawData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                const response = await getLatestPredictions();

                setRawData(response.data);

            } catch (err) {
                console.error("Błąd pobierania danych:", err);
                setError("Nie udało się pobrać prognoz.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <p>Ładowanie prognoz...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '30px' }}>
                Prognozowane średnie dzienne zużycie energii elektrycznej na następne 7 dni
            </h2>

            {/* Wykres */}
            <div style={{ marginBottom: '40px', padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', borderRadius: '8px' }}>
                <h3 style={{ marginBottom: '15px' }}>Wizualizacja graficzna</h3>
                <PredictionChart rawData={rawData} />
            </div>

            {/* Tabela */}
            <div style={{ padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', borderRadius: '8px' }}>
                <h3 style={{ marginBottom: '15px' }}>Szczegółowe dane</h3>
                <PredictionTable rawData={rawData} />
            </div>
        </div>
    );
};

export default PredictionViewer;