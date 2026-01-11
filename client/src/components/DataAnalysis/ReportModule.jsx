import React, {useCallback, useEffect, useMemo, useState} from 'react';
import { getChartData, downloadPdfReport, getDataTotal, getDevices } from './ReportService';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
    ResponsiveContainer, BarChart, Bar, PieChart, Pie, Cell } from 'recharts';
import { Download, RefreshCw } from 'lucide-react';

const ReportModule = () => {
    const formatYAxis = (value) => `${value} kWh`;
    const formatTooltip = (value) => [`${value.toFixed(2)} kWh`, "Wartość"];

    const getTodayAtMidnight = () => {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}T00:00`;
    };

    const [dateFrom, setDateFrom] = useState(getTodayAtMidnight());
    const [dateTo, setDateTo] = useState(getTodayAtMidnight());
    const [chartData, setChartData] = useState([]);
    const [reportType, setReportType] = useState('ENERGY_CONSUMPTION');
    const [viewType, setViewType] = useState('line');
    const [seriesKeys, setSeriesKeys] = useState([]);
    const [devices, setDevices] = useState([]);
    const [selectedDeviceIds, setSelectedDeviceIds] = useState([]);

    const PIE_COLORS = {
        "Produkcja": "#16a34a",
        "Zużycie": "#dc2626"
    };

    const deviceNameMap = useMemo(() => {
        const m = {};
        devices.forEach(d => { m[d.id] = d.description || d.id; });
        return m;
    }, [devices]);

    const handleGenerateChart = useCallback(async () => {
        setChartData([]);
        try {
            if (viewType === 'line' || viewType === 'bar') {
                const response = await getChartData(dateFrom, dateTo, reportType, selectedDeviceIds);
                const data = response.data || [];

                if (!selectedDeviceIds || selectedDeviceIds.length === 0) {
                    const sumByTs = new Map();
                    data.forEach(item => {
                        const d = new Date(item.timestamp);
                        const tsLabel = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:00`;
                        const prev = sumByTs.get(tsLabel) || 0;
                        sumByTs.set(tsLabel, prev + (item.value || 0));
                    });
                    const aggregated = Array.from(sumByTs.entries())
                        .map(([ts, value]) => ({ timestamp: ts, value }))
                        .sort((a, b) => a.timestamp.localeCompare(b.timestamp));
                    setChartData(aggregated);
                    setSeriesKeys([]);
                } else {
                    const byTs = new Map();
                    const devs = new Set();
                    data.forEach(item => {
                        const d = new Date(item.timestamp);
                        const tsLabel = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:00`;
                        const dev = item.deviceId || 'unknown';
                        devs.add(dev);
                        if (!byTs.has(tsLabel)) byTs.set(tsLabel, { timestamp: tsLabel });
                        const row = byTs.get(tsLabel);
                        row[dev] = (row[dev] || 0) + (item.value || 0);
                    });
                    setChartData(Array.from(byTs.values()).sort((a, b) => a.timestamp.localeCompare(b.timestamp)));
                    setSeriesKeys(Array.from(devs));
                }
            } else if (viewType === 'pie') {
                const [resCons, resProd] = await Promise.all([
                    getDataTotal(dateFrom, dateTo, 'ENERGY_CONSUMPTION', selectedDeviceIds),
                    getDataTotal(dateFrom, dateTo, 'ENERGY_PRODUCTION', selectedDeviceIds)
                ]);
                setChartData([
                    { name: "Produkcja", value: resProd.data || 0 },
                    { name: "Zużycie", value: resCons.data || 0 }
                ]);
            }
        } catch (error) {
            console.error("Błąd pobierania danych:", error);
            alert("Nie udało się pobrać danych.");
        }
    }, [dateFrom, dateTo, reportType, viewType, selectedDeviceIds]);

    useEffect(() => {
        getDevices().then(res => setDevices(res.data || [])).catch(console.error);
    }, []);

    useEffect(() => {
        handleGenerateChart();
    }, [viewType, handleGenerateChart]);

    const handleDownloadPdf = async () => {
        try {
            const response = await downloadPdfReport(dateFrom, dateTo, selectedDeviceIds);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'raport_energetyczny.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch {
            alert("Nie udało się wygenerować PDF.");
        }
    };

    const COLORS = ['#2563eb', '#16a34a', '#db2777', '#ea580c', '#9333ea', '#0891b2'];

    return (
        <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-6 text-gray-800">

            {/* --- PANEL KONTROLNY --- */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4 mb-6 p-4 bg-gray-50 rounded-lg border border-gray-100">
                <div className="flex flex-col">
                    <label className="text-xs font-bold text-gray-500 mb-1 uppercase">Typ Danych</label>
                    <select value={reportType} onChange={(e) => setReportType(e.target.value)} className="p-2 rounded border border-gray-300 bg-white text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none">
                        <option value="ENERGY_CONSUMPTION">Zużycie Energii</option>
                        <option value="ENERGY_PRODUCTION">Produkcja Energii</option>
                        <option value="DEVICE_EFFICIENCY">Efektywność</option>
                    </select>
                </div>

                <div className="flex flex-col">
                    <label className="text-xs font-bold text-gray-500 mb-1 uppercase">Urządzenie</label>
                    <select value={selectedDeviceIds[0] || 'ALL'} onChange={(e) => setSelectedDeviceIds(e.target.value === 'ALL' ? [] : [e.target.value])} className="p-2 rounded border border-gray-300 bg-white text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none">
                        <option value="ALL">Wszystkie urządzenia</option>
                        {devices.map(d => <option key={d.id} value={d.id}>{d.description || d.id}</option>)}
                    </select>
                </div>

                <div className="flex flex-col">
                    <label className="text-xs font-bold text-gray-500 mb-1 uppercase">Data Od</label>
                    <input type="datetime-local" value={dateFrom} onChange={(e) => setDateFrom(e.target.value)} className="p-2 rounded border border-gray-300 bg-white text-sm" />
                </div>

                <div className="flex flex-col">
                    <label className="text-xs font-bold text-gray-500 mb-1 uppercase">Data Do</label>
                    <input type="datetime-local" value={dateTo} onChange={(e) => setDateTo(e.target.value)} className="p-2 rounded border border-gray-300 bg-white text-sm" />
                </div>

                <div className="flex items-end gap-2">
                    <button onClick={handleGenerateChart} className="flex-1 bg-blue-600 hover:bg-blue-700 text-white p-2 rounded flex justify-center items-center gap-2 font-semibold transition">
                        <RefreshCw size={18} /> Generuj
                    </button>
                    <button onClick={handleDownloadPdf} className="bg-red-600 hover:bg-red-700 text-white p-2 rounded flex justify-center items-center gap-2 font-semibold transition" title="Pobierz PDF">
                        <Download size={18} /> PDF
                    </button>
                </div>
            </div>

            {/* --- OBSZAR WYKRESU --- */}
            <div style={{ height: '500px', width: '100%' }} className="bg-white p-4">
                <div className="flex justify-end mb-4 gap-2">
                    <button onClick={() => setViewType('line')} className={`px-3 py-1 text-sm rounded ${viewType === 'line' ? 'bg-blue-600 text-white font-bold' : 'bg-gray-100'}`}>Liniowy</button>
                    <button onClick={() => setViewType('bar')} className={`px-3 py-1 text-sm rounded ${viewType === 'bar' ? 'bg-blue-600 text-white font-bold' : 'bg-gray-100'}`}>Słupkowy</button>
                    <button onClick={() => setViewType('pie')} className={`px-3 py-1 text-sm rounded ${viewType === 'pie' ? 'bg-blue-600 text-white font-bold' : 'bg-gray-100'}`}>Kołowy (Suma)</button>
                </div>

                <ResponsiveContainer width="100%" height="100%">
                    {viewType === 'pie' ? (
                        <PieChart>
                            <Pie
                                data={chartData}
                                cx="50%"
                                cy="50%"
                                label={({name, value}) => `${name}: ${value.toFixed(2)} kWh`}
                                outerRadius={105}
                                dataKey="value"
                                nameKey="name"
                            >
                                {chartData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={PIE_COLORS[entry.name] || COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Legend />
                        </PieChart>
                    ) : viewType === 'bar' ? (
                        <BarChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} />
                            <XAxis dataKey="timestamp" />
                            <YAxis tickFormatter={formatYAxis} width={80} />
                            <Tooltip formatter={formatTooltip} />
                            <Legend />
                            {seriesKeys.length > 0
                                ? seriesKeys.map((key, idx) => <Bar key={key} dataKey={key} name={deviceNameMap[key] || key} fill={COLORS[idx % COLORS.length]} />)
                                : <Bar dataKey="value" name="Wartość" fill="#2563eb" />
                            }
                        </BarChart>
                    ) : (
                        <LineChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} />
                            <XAxis dataKey="timestamp" />
                            <YAxis tickFormatter={formatYAxis} width={80} />
                            <Tooltip formatter={formatTooltip} />
                            <Legend />
                            {seriesKeys.length > 0
                                ? seriesKeys.map((key, idx) => <Line key={key} type="monotone" dataKey={key} name={deviceNameMap[key] || key} stroke={COLORS[idx % COLORS.length]} strokeWidth={2} dot={false} />)
                                : <Line type="monotone" dataKey="value" name="Wartość" stroke="#2563eb" strokeWidth={2} dot={false} />
                            }
                        </LineChart>
                    )}
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default ReportModule;