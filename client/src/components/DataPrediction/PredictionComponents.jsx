import React, {useMemo} from 'react';
import {CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis} from 'recharts';

/**
 * Komponent wyświetlający wykres liniowy.
 * Przyjmuje surowe dane z backendu w propsie 'rawData'.
 */
export const PredictionChart = ({rawData}) => {

    // Używamy useMemo, aby przetwarzać dane tylko wtedy, gdy rawData się zmieni,
    // a nie przy każdym przerysowaniu komponentu.
    const chartData = useMemo(() => {
        if (!rawData || rawData.length === 0) return [];

        const formatted = rawData.map(item => ({
            // Formatowanie daty na oś X (np. "06.01")
            date: new Date(item.forecastDate).toLocaleDateString('pl-PL', {
                day: '2-digit',
                month: '2-digit'
            }),
            // Formatowanie pełnej daty do Tooltipa
            fullDate: new Date(item.forecastDate).toLocaleDateString('pl-PL', {
                weekday: 'long',
                day: 'numeric',
                month: 'long',
                year: 'numeric'
            }),
            // Zaokrąglenie wartości
            usage: parseFloat(item.forecastedUsage.toFixed(2))
        }));

        // Sortowanie chronologiczne
        return formatted.sort(() => {
            return 0;
        });
    }, [rawData]);

    if (!chartData.length) {
        return <p>Brak danych do wyświetlenia.</p>;
    }

    return (
        <div style={{width: '100%', height: '400px'}}>
            <ResponsiveContainer width="100%" height="100%">
                <LineChart
                    data={chartData}
                    margin={{top: 5, right: 30, left: 20, bottom: 5}}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="date"/>
                    <YAxis label={{value: 'kWh', angle: -90, position: 'insideLeft'}}/>
                    <Tooltip content={<CustomTooltip/>}/>
                    <Legend/>
                    <Line
                        type="monotone"
                        dataKey="usage"
                        name="Przewidywane zużycie"
                        stroke='#fbcc2d'
                        activeDot={{r: 8}}
                        strokeWidth={3}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

// Tooltip wydzielony jako komponent pomocniczy (nie musi być eksportowany)
const CustomTooltip = ({active, payload}) => {
    if (active && payload && payload.length) {
        const dataPoint = payload[0].payload;
        return (
            <div style={{
                backgroundColor: '#58328e',
                border: '1px solid #ccc',
                padding: '10px',
                borderRadius: '5px',
                zIndex: 1000
            }}>
                <p style={{color: '#fbe02d', fontWeight: 'bold', margin: 0}}>{dataPoint.fullDate}</p>
                <p style={{color: '#fbe02d', margin: 0}}>
                    Zużycie: {dataPoint.usage} kWh
                </p>
            </div>
        );
    }
    return null;
};

export const PredictionTable = ({ rawData }) => {
    // Jeśli brak danych, nie wyświetlamy nic lub komunikat
    if (!rawData || rawData.length === 0) {
        return <p>Brak danych do tabeli.</p>;
    }

    // Pobieramy czas utworzenia z PIERWSZEGO elementu (zakładamy, że reszta ma ten sam)
    const creationTimeRaw = rawData[0].creationTime;

    // Formatujemy czas utworzenia (np. 06.01.2026 15:47)
    const creationTimeFormatted = new Date(creationTimeRaw).toLocaleString('pl-PL', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });

    return (
        <div style={{ marginTop: '20px', overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'center' }}>
                <thead>
                <tr style={{ backgroundColor: '#f4f4f4', borderBottom: '2px solid #ddd' }}>
                    <th style={styles.th}>Data utworzenia prognozy</th>
                    <th style={styles.th}>Dzień prognozy</th>
                    <th style={styles.th}>Przewidywane zużycie (kWh)</th>
                </tr>
                </thead>
                <tbody>
                {rawData.map((row, index) => (
                    <tr key={row.id} style={{ borderBottom: '1px solid #eee' }}>

                        {/* LOGIKA MERGE'OWANIA KOMÓREK (ROWSPAN) */}
                        {/* Renderujemy tę komórkę TYLKO dla pierwszego wiersza */}
                        {index === 0 && (
                            <td
                                rowSpan={rawData.length}
                                style={{ ...styles.td, backgroundColor: '#fafafa', fontWeight: 'bold', verticalAlign: 'middle' }}
                            >
                                {creationTimeFormatted}
                            </td>
                        )}

                        {/* Pozostałe komórki renderują się normalnie dla każdego wiersza */}
                        <td style={styles.td}>
                            {new Date(row.forecastDate).toLocaleDateString('pl-PL', {
                                weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric'
                            })}
                        </td>
                        <td style={styles.td}>
                            {row.forecastedUsage.toFixed(2)} kWh
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

// Proste style inline dla czytelności (możesz to przenieść do CSS)
const styles = {
    th: {
        padding: '12px',
        border: '1px solid #ddd',
        color: '#333'
    },
    td: {
        padding: '10px',
        border: '1px solid #ddd',
        color: '#555'
    }
};