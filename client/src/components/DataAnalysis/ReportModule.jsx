import React, { useState } from 'react';
import { getChartData, downloadPdfReport } from './ReportService';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, BarChart, Bar } from 'recharts';

const ReportModule = () => {
    const [dateFrom, setDateFrom] = useState('2025-12-01T00:00');
    const [dateTo, setDateTo] = useState('2025-12-07T23:59');
    const [chartData, setChartData] = useState([]);
    const [reportType, setReportType] = useState('ENERGY_CONSUMPTION');
    const [viewType, setViewType] = useState('line');

    const handleGenerateChart = async () => {
        try {
            const response = await getChartData(dateFrom, dateTo, reportType);
            const formattedData = response.data.map(item => ({
                ...item,
                timestamp: new Date(item.timestamp).toLocaleDateString() + ' ' + new Date(item.timestamp).getHours() + ':00'
            }));
            setChartData(formattedData);
        } catch (error) {
            console.error("Błąd pobierania danych wykresu", error);
            alert("Nie udało się pobrać danych.");
        }
    };

    const handleDownloadPdf = async () => {
        try {
            const response = await downloadPdfReport(dateFrom, dateTo);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'raport.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (error) {
            console.error("Błąd pobierania PDF", error);
            alert("Nie udało się wygenerować PDF.");
        }
    };

    return (
        <div className="p-6 max-w-6xl mx-auto space-y-8 text-gray-100 bg-gray-800 rounded-xl border-2 border-gray-600 mt-10">
            <h2 className="text-2xl font-bold mb-4 border-b-2 border-gray-500 pb-2">📊 Moduł Analizy i Raportowania</h2>

            {/* Panel Sterowania */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
                <div>
                    <label className="block text-sm text-gray-300 mb-1 font-bold">Rodzaj Raportu</label>
                    <select
                        value={reportType}
                        onChange={(e) => setReportType(e.target.value)}
                        className="w-full p-2 rounded bg-gray-700 border border-gray-500 text-white"
                    >
                        <option value="ENERGY_CONSUMPTION">Zużycie Energii</option>
                        <option value="ENERGY_PRODUCTION">Produkcja Energii</option>
                    </select>
                </div>
                <div>
                    <label className="block text-sm text-gray-300 mb-1 font-bold">Od</label>
                    <input
                        type="datetime-local"
                        value={dateFrom}
                        onChange={(e) => setDateFrom(e.target.value)}
                        className="w-full p-2 rounded bg-gray-700 border border-gray-500 text-white"
                    />
                </div>
                <div>
                    <label className="block text-sm text-gray-300 mb-1 font-bold">Do</label>
                    <input
                        type="datetime-local"
                        value={dateTo}
                        onChange={(e) => setDateTo(e.target.value)}
                        className="w-full p-2 rounded bg-gray-700 border border-gray-500 text-white"
                    />
                </div>
                <div className="flex items-end gap-2">
                    <button
                        onClick={handleGenerateChart}
                        className="flex-1 bg-blue-600 hover:bg-blue-500 py-2 rounded font-bold transition"
                    >
                        Generuj Wykres
                    </button>
                    <button
                        onClick={handleDownloadPdf}
                        className="bg-red-600 hover:bg-red-500 px-4 py-2 rounded font-bold transition"
                        title="Pobierz PDF"
                    >
                        PDF
                    </button>
                </div>
            </div>

            {/* Wykres */}
            {chartData.length > 0 ? (
                <div className="bg-gray-900 p-4 rounded-lg border border-gray-700" style={{ height: '400px' }}>
                    <div className="mb-2 text-right">
                        <button onClick={() => setViewType('line')} className={`mr-2 px-2 py-1 rounded ${viewType === 'line' ? 'bg-blue-600' : 'bg-gray-700'}`}>Liniowy</button>
                        <button onClick={() => setViewType('bar')} className={`px-2 py-1 rounded ${viewType === 'bar' ? 'bg-blue-600' : 'bg-gray-700'}`}>Słupkowy</button>
                    </div>

                    <ResponsiveContainer width="100%"Bh height="100%">
                        {viewType === 'line' ? (
                            <LineChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                                <XAxis dataKey="timestamp" stroke="#ccc" />
                                <YAxis stroke="#ccc" />
                                <Tooltip contentStyle={{ backgroundColor: '#333', borderColor: '#555' }} />
                                <Legend />
                                <Line type="monotone" dataKey="value" stroke="#8884d8" name="Wartość [kWh]" strokeWidth={2} />
                            </LineChart>
                        ) : (
                            <BarChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                                <XAxis dataKey="timestamp" stroke="#ccc" />
                                <YAxis stroke="#ccc" />
                                <Tooltip contentStyle={{ backgroundColor: '#333', borderColor: '#555' }} />
                                <Legend />
                                <Bar dataKey="value" fill="#82ca9d" name="Wartość [kWh]" />
                            </BarChart>
                        )}
                    </ResponsiveContainer>
                </div>
            ) : (
                <div className="text-center py-10 text-gray-500 border-2 border-dashed border-gray-600 rounded-lg">
                    Wybierz parametry i kliknij "Generuj Wykres", aby zobaczyć dane.
                </div>
            )}
        </div>
    );
};

export default ReportModule;