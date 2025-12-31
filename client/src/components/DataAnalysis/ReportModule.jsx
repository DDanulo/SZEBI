import React, {useCallback, useEffect, useMemo, useState} from 'react';
import { getChartData, downloadPdfReport, getDataTotal, getDevices } from './ReportService';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
    ResponsiveContainer, BarChart, Bar, PieChart, Pie, Cell } from 'recharts';

const ReportModule = () => {
    const [dateFrom, setDateFrom] = useState('2025-12-01T00:00');
    const [dateTo, setDateTo] = useState('2025-12-07T23:59');
    const [chartData, setChartData] = useState([]);
    const [reportType, setReportType] = useState('ENERGY_CONSUMPTION');
    const [viewType, setViewType] = useState('line');
    const [seriesKeys, setSeriesKeys] = useState([]);
    const [granularity, setGranularity] = useState('daily'); // 'daily' | 'hourly'
    const [devices, setDevices] = useState([]);
    const [selectedDeviceIds, setSelectedDeviceIds] = useState([]);

    const deviceNameMap = useMemo(() => {
        const m = {};
        devices.forEach(d => { m[d.id] = d.description || d.id; });
        return m;
    }, [devices]);

    const handleGenerateChart = useCallback(async () => {
        setChartData([]);

        try {
            if (viewType === `line` || viewType === `bar`) {
                const response = await getChartData(dateFrom, dateTo, reportType, selectedDeviceIds);

                if (!selectedDeviceIds || selectedDeviceIds.length === 0) {
                    if (reportType !== 'DEVICE_EFFICIENCY') {
                        const sumByTs = new Map();
                        response.data.forEach(item => {
                            const d = new Date(item.timestamp);
                            const tsLabel = d.toLocaleDateString() + ' ' + d.getHours() + ':00';
                            const prev = sumByTs.get(tsLabel) || 0;
                            sumByTs.set(tsLabel, prev + (item.value || 0));
                        });
                        const aggregated = Array.from(sumByTs.entries())
                            .map(([ts, value]) => ({ timestamp: ts, value }))
                            .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
                        setChartData(aggregated);
                        setSeriesKeys([]);
                    } else {
                        const agg = new Map();
                        response.data.forEach(item => {
                            const d = new Date(item.timestamp);
                            const tsLabel = d.toLocaleDateString() + ' ' + d.getHours() + ':00';
                            const entry = agg.get(tsLabel) || { sum: 0, count: 0 };
                            entry.sum += (item.value || 0);
                            entry.count += 1;
                            agg.set(tsLabel, entry);
                        });
                        const averaged = Array.from(agg.entries())
                            .map(([ts, { sum, count }]) => ({ timestamp: ts, value: count ? sum / count : 0 }))
                            .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
                        setChartData(averaged);
                        setSeriesKeys([]);
                    }
                } else {
                    const byTs = new Map();
                    const devices = new Set();

                    response.data.forEach(item => {
                        const d = new Date(item.timestamp);
                        const tsLabel = d.toLocaleDateString() + ' ' + d.getHours() + ':00';
                        const dev = item.deviceId || 'unknown-device';
                        devices.add(dev);
                        if (!byTs.has(tsLabel)) {
                            byTs.set(tsLabel, { timestamp: tsLabel });
                        }
                        const row = byTs.get(tsLabel);
                        row[dev] = (row[dev] || 0) + (item.value || 0);
                    });

                    const pivoted = Array.from(byTs.values()).sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
                    setChartData(pivoted);
                    setSeriesKeys(Array.from(devices));
                }
            }
            if (viewType === `pie`) {
                const responseConsumption = await getDataTotal(dateFrom, dateTo, `ENERGY_CONSUMPTION`, selectedDeviceIds);
                const responseProduction = await getDataTotal(dateFrom, dateTo, `ENERGY_PRODUCTION`, selectedDeviceIds);
                const valConsumption = responseConsumption.data;
                const valProduction = responseProduction.data;

                const pieData = [
                    { name: "Produkcja Energii", value: valProduction },
                    { name: "Zużycie Energii", value: valConsumption }
                ];

                setChartData(pieData);
            }

        } catch (error) {
            console.error("Błąd pobierania danych wykresu", error);
            alert("Nie udało się pobrać danych.");
        }
    }, [dateFrom, dateTo, reportType, viewType, selectedDeviceIds]);

    useEffect(() => {
        if (viewType === `pie` || viewType === `line` || viewType === `bar`) {
            handleGenerateChart();
        }
    }, [viewType, handleGenerateChart]);

    useEffect(() => {
        (async () => {
            try {
                const res = await getDevices();
                setDevices(res.data || []);
            } catch (e) {
                console.error('Błąd pobierania listy urządzeń', e);
            }
        })();
    }, []);

    const handleDownloadPdf = async () => {
        try {
            const response = await downloadPdfReport(dateFrom, dateTo, selectedDeviceIds, granularity);
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

    const COLORS = ['#00C49F', '#FF8042', '#8884d8', '#82ca9d', '#ffc658', '#a4de6c', '#d0ed57', '#8dd1e1', '#d88884', '#c0c0c0'];
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
                        <option value="DEVICE_EFFICIENCY">Sprawność Urządzeń</option>
                    </select>
                </div>
                <div>
                    <label className="block text-sm text-gray-300 mb-1 font-bold">Urządzenie</label>
                    <select
                        value={selectedDeviceIds[0] || 'ALL'}
                        onChange={(e) => {
                            const val = e.target.value;
                            if (val === 'ALL') {
                                setSelectedDeviceIds([]);
                            } else {
                                setSelectedDeviceIds([val]);
                            }
                        }}
                        className="w-full p-2 rounded bg-gray-700 border border-gray-500 text-white"
                        title="Wybierz urządzenie lub Wszystkie"
                    >
                        <option value="ALL" className="bg-gray-700">Wszystkie urządzenia</option>
                        {devices.map(d => (
                            <option key={d.id} value={d.id} className="bg-gray-700">
                                {d.description || d.id}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label className="block text-sm text-gray-300 mb-1 font-bold">Granularność PDF</label>
                    <select
                        value={granularity}
                        onChange={(e) => setGranularity(e.target.value)}
                        className="w-full p-2 rounded bg-gray-700 border border-gray-500 text-white"
                    >
                        <option value="hourly" className="bg-gray-700">Godzinowa</option>
                        <option value="daily" className="bg-gray-700">Dzienna</option>
                        <option value="weekly" className="bg-gray-700">Tygodniowa</option>
                        <option value="monthly" className="bg-gray-700">Miesięczna</option>
                        <option value="yearly" className="bg-gray-700">Roczna</option>
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
                        <button onClick={() => setViewType('pie')} className={`ml-2 px-2 py-1 rounded ${viewType === 'pie' ? 'bg-blue-600' : 'bg-gray-700'}`}>Kołowy</button>
                    </div>

                    <ResponsiveContainer width="100%" height="100%">
                        {viewType === 'line' ? (
                            <LineChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                                <XAxis dataKey="timestamp" stroke="#ccc" />
                                <YAxis stroke="#ccc" />
                                <Tooltip contentStyle={{ backgroundColor: '#333', borderColor: '#555' }} />
                                <Legend />
                                {seriesKeys.length > 0 ? (
                                    seriesKeys.map((key, idx) => (
                                        <Line key={key}
                                              type="monotone"
                                              dataKey={key}
                                              stroke={COLORS[idx % COLORS.length]}
                                              name={deviceNameMap[key] || key}
                                              strokeWidth={2}
                                        />
                                    ))
                                ) : (
                                    <Line type="monotone" dataKey="value" stroke="#8884d8" name="Wartość" strokeWidth={2} />
                                )}
                            </LineChart>
                        ) : viewType === 'bar' ? (
                            <BarChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                                <XAxis dataKey="timestamp" stroke="#ccc" />
                                <YAxis stroke="#ccc" />
                                <Tooltip contentStyle={{ backgroundColor: '#333', borderColor: '#555' }} />
                                <Legend />
                                {seriesKeys.length > 0 ? (
                                    seriesKeys.map((key, idx) => (
                                        <Bar key={key} dataKey={key} fill={COLORS[idx % COLORS.length]} name={deviceNameMap[key] || key} />
                                    ))
                                ) : (
                                    <Bar dataKey="value" fill="#82ca9d" name="Wartość" />
                                )}
                            </BarChart>
                        ) : (
                            <PieChart>
                                <Pie data={chartData} cx="50%" cy="50%" labelLine={false}
                                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                                    outerRadius={80}
                                    fill="#8884d8"
                                    dataKey="value"
                                    nameKey="name"
                                >
                                    {chartData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip contentStyle={{ backgroundColor: '#333', borderColor: '#555'}}
                                         itemStyle={{ color: '#ccc' }} />
                                <Legend />
                            </PieChart>
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