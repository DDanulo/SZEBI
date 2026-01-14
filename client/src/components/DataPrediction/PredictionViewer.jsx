import React, { useEffect, useState } from 'react';
import {generatePrediction, getLatestPredictions} from './PredictionService';
import { PredictionChart, PredictionTable } from './PredictionComponents';

const PredictionViewer = () => {
    const [rawData, setRawData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const generateForecast = async function () {
        generatePrediction()
    }

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
                🤖〽⚡Moduł prognozowania
            </h2>
            <h3>
                Prognozowane średnie dzienne zużycie energii elektrycznej na następne 7 dni
            </h3>

            <div style={{ marginBottom: '40px', padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', borderRadius: '8px' }}>
                <h3 style={{ marginBottom: '15px' }}>Wizualizacja graficzna najnowszych prognoz zużycia energii elektrycznej</h3>
                <PredictionChart rawData={rawData} />
            </div>

            <div style={{ padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', borderRadius: '8px' }}>
                <h3 style={{ marginBottom: '15px' }}>Tabela z najnowszymi prognozami zużycia energii elektrycznej</h3>
                <PredictionTable rawData={rawData} />
            </div>

            <button onClick={generateForecast} title="Wygeneruj prognozę">Wygeneruj prognozę</button>
        </div>
    );
};

export default PredictionViewer;