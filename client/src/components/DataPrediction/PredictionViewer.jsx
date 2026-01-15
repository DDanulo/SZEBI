import React, {useEffect, useState} from 'react';
import {generatePrediction, getLatestPredictions, getPredictionsByDateRange} from './PredictionService';
import {PredictionChart, PredictionTable} from './PredictionComponents';

const PredictionViewer = () => {
    const [rawData, setRawData] = useState([]);

    const [tableData, setTableData] = useState([]);

    const [dateFrom, setDateFrom] = useState('');
    const [dateTo, setDateTo] = useState('');

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const generateForecast = async function () {
        try {
            setLoading(true);
            await generatePrediction();
            await fetchData();
        } catch (err) {
            console.error("Błąd generowania:", err);
            setError("Nie udało się wygenerować prognozy.");
            setLoading(false);
        }
    };

    const fetchData = async () => {
        try {
            setLoading(true);
            const response = await getLatestPredictions();

            setRawData(response.data);
            setTableData(response.data);
            setError(null);
        } catch (err) {
            console.error("Błąd pobierania danych:", err);
            setError("Nie udało się pobrać prognoz. Brak autoryzacji.");
        } finally {
            setLoading(false);
        }
    };

    const handleFilter = async () => {
        if (!dateFrom || !dateTo) {
            alert("Proszę wybrać obie daty (Od i Do).");
            return;
        }

        try {
            setLoading(true);
            const response = await getPredictionsByDateRange(dateFrom, dateTo);

            setTableData(response.data);
            setError(null);
        } catch (err) {
            console.error("Błąd filtrowania:", err);
            setError("Nie udało się pobrać danych archiwalnych.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    if (loading && rawData.length === 0) return <p>Ładowanie modułu...</p>;

    if (error) return <p style={{color: 'red', textAlign: 'center'}}>{error}</p>

    return (
        <div style={{padding: '20px', maxWidth: '1200px', margin: '0 auto'}}>
            <h2 style={{textAlign: 'center', marginBottom: '30px'}}>
                🤖〽⚡ Moduł prognozowania
            </h2>

            <div style={{
                marginBottom: '40px',
                padding: '20px',
                boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
                borderRadius: '8px'
            }}>
                <h3 style={{marginBottom: '15px'}}>Wizualizacja graficzna najnowszych prognoz</h3>
                <PredictionChart rawData={rawData}/>
            </div>

            <div style={{padding: '20px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', borderRadius: '8px'}}>
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    flexWrap: 'wrap',
                    marginBottom: '15px'
                }}>
                    <h3>Historia prognoz</h3>

                    <div style={{display: 'flex', gap: '10px', alignItems: 'center'}}>
                        <label>
                            Od:
                            <input
                                type="datetime-local"
                                value={dateFrom}
                                onChange={(e) => setDateFrom(e.target.value)}
                                style={{marginLeft: '5px', padding: '5px'}}
                            />
                        </label>
                        <label>
                            Do:
                            <input
                                type="datetime-local"
                                value={dateTo}
                                onChange={(e) => setDateTo(e.target.value)}
                                style={{marginLeft: '5px', padding: '5px'}}
                            />
                        </label>
                        <button
                            onClick={handleFilter}
                            style={{
                                padding: '6px 12px',
                                cursor: 'pointer',
                                backgroundColor: '#007bff',
                                color: 'white',
                                border: 'none',
                                borderRadius: '4px'
                            }}
                        >
                            Filtruj
                        </button>
                        <button
                            onClick={fetchData}
                            title="Reset tabeli do najnowszych"
                            style={{
                                padding: '6px 12px',
                                cursor: 'pointer',
                                backgroundColor: '#6c757d',
                                color: 'white',
                                border: 'none',
                                borderRadius: '4px'
                            }}
                        >
                            Reset
                        </button>
                    </div>
                </div>

                <PredictionTable rawData={tableData}/>
            </div>

            <div style={{marginTop: '30px', textAlign: 'center'}}>
                <button
                    onClick={generateForecast}
                    title="Wygeneruj nową prognozę na podstawie aktualnych danych"
                    style={{
                        padding: '10px 20px',
                        fontSize: '16px',
                        cursor: 'pointer',
                        backgroundColor: '#28a745',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px'
                    }}
                >
                    Wygeneruj nową prognozę
                </button>
            </div>
        </div>
    );
};

export default PredictionViewer;