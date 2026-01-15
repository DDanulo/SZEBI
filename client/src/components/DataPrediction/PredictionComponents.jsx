import React, {useMemo} from 'react';
import {CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis} from 'recharts';

export const PredictionChart = ({rawData}) => {

    const chartData = useMemo(() => {
        if (!rawData || rawData.length === 0) return [];

        const formatted = rawData.map(item => ({
            date: new Date(item.forecastDate).toLocaleDateString('pl-PL', {
                day: '2-digit',
                month: '2-digit'
            }),
            fullDate: new Date(item.forecastDate).toLocaleDateString('pl-PL', {
                weekday: 'long',
                day: 'numeric',
                month: 'long',
                year: 'numeric'
            }),
            usage: parseFloat(item.forecastedUsage.toFixed(2))
        }));

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
                        name="Przewidywane średnie dzienne zużycie [kWh]"
                        stroke='#fbcc2d'
                        activeDot={{r: 8}}
                        strokeWidth={3}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

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

export const PredictionTable = ({rawData}) => {
    if (!rawData || rawData.length === 0) {
        return <p>Brak danych do tabeli.</p>;
    }

    // eslint-disable-next-line react-hooks/rules-of-hooks
    const groupedData = useMemo(() => {
        const groups = rawData.reduce((acc, item) => {
            const key = item.creationTime;
            if (!acc[key]) {
                acc[key] = [];
            }
            acc[key].push(item);
            return acc;
        }, {});

        return Object.entries(groups).sort((a, b) => {
            return new Date(b[0]) - new Date(a[0]);
        });
    }, [rawData]);

    const formatCreationTime = (isoString) => {
        return new Date(isoString).toLocaleString('pl-PL', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    return (
        <div style={{marginTop: '20px', overflowX: 'auto'}}>
            <table style={{width: '100%', borderCollapse: 'collapse', textAlign: 'center'}}>
                <thead>
                <tr style={{backgroundColor: '#e6bd34', borderBottom: '2px solid #000'}}>
                    <th style={styles.th}>Data wygenerowania prognozy</th>
                    <th style={styles.th}>Dzień prognozy</th>
                    <th style={styles.th}>Przewidywane średnie dzienne zużycie</th>
                </tr>
                </thead>
                <tbody>
                {groupedData.map(([creationTime, rows]) => (
                    <React.Fragment key={creationTime}>
                        {rows.map((row, index) => (
                            <tr key={row.id} style={{borderBottom: '1px solid #000'}}>

                                {index === 0 && (
                                    <td
                                        rowSpan={rows.length}
                                        style={{
                                            ...styles.td,
                                            backgroundColor: '#fae16b',
                                            fontWeight: 'bold',
                                            verticalAlign: 'middle'
                                        }}
                                    >
                                        {formatCreationTime(creationTime)}
                                    </td>
                                )}

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
                    </React.Fragment>
                ))}
                </tbody>
            </table>
        </div>
    );
};

const styles = {
    th: {
        padding: '12px',
        border: '1px solid #000',
        color: '#000000'
    },
    td: {
        padding: '10px',
        border: '1px solid #000',
        color: '#000000',
        backgroundColor: '#fae16b'
    }
};