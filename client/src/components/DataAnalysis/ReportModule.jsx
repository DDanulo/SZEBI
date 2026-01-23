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
    const [isLoading, setIsLoading] = useState(false);
    const [fetchError, setFetchError] = useState(null);

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
        setIsLoading(true);
        setFetchError(null);
        try {
            if (viewType === 'line' || viewType === 'bar') {
                const response = await getChartData(dateFrom, dateTo, reportType, selectedDeviceIds);
                const data = response.data || [];

                if (data.length === 0) {
                    setChartData([]);
                } else if (!selectedDeviceIds || selectedDeviceIds.length === 0) {
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
                const consVal = resCons.data || 0;
                const prodVal = resProd.data || 0;

                if (consVal === 0 && prodVal === 0) {
                    setChartData([]);
                } else {
                    setChartData([
                        { name: "Produkcja", value: prodVal },
                        { name: "Zużycie", value: consVal }
                    ]);
                }
            }
        } catch (error) {
            console.error("Błąd pobierania danych:", error);
            setFetchError("Nie udało się pobrać danych.");
        } finally {
            setIsLoading(false);
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
        <div className="bg-white rounded-2xl shadow-lg border border-gray-200 p-8 text-gray-800">

            {/* --- PANEL KONTROLNY --- */}
            <div className="flex flex-col mb-10 p-10 bg-gray-50 rounded-2xl border border-gray-100 shadow-sm gap-20">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-x-24 gap-y-16">
                    <div className="flex flex-col gap-8">
                        <label className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
                            Typ Danych
                        </label>
                        <select
                            value={reportType}
                            onChange={(e) => setReportType(e.target.value)}
                            className="p-4 rounded-xl border border-gray-300 bg-white text-base shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:outline-none transition-all hover:border-gray-400"
                        >
                            <option value="ENERGY_CONSUMPTION">Zużycie Energii</option>
                            <option value="ENERGY_PRODUCTION">Produkcja Energii</option>
                        </select>
                    </div>

                    <div className="flex flex-col gap-8">
                        <label className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
                            Urządzenie
                        </label>
                        <select
                            value={selectedDeviceIds[0] || 'ALL'}
                            onChange={(e) => setSelectedDeviceIds(e.target.value === 'ALL' ? [] : [e.target.value])}
                            className="p-4 rounded-xl border border-gray-300 bg-white text-base shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:outline-none transition-all hover:border-gray-400"
                        >
                            <option value="ALL">Wszystkie urządzenia</option>
                            {devices.map(d => <option key={d.id} value={d.id}>{d.description || d.id}</option>)}
                        </select>
                    </div>
                </div>
                
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-24 gap-y-16 items-end">
                    <div className="flex flex-col gap-8">
                        <label className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
                            Data Od
                        </label>
                        <input
                            type="datetime-local"
                            value={dateFrom}
                            onChange={(e) => setDateFrom(e.target.value)}
                            className="p-4 rounded-xl border border-gray-300 bg-white text-base shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:outline-none transition-all hover:border-gray-400"
                        />
                    </div>

                    <div className="flex flex-col gap-8">
                        <label className="text-sm font-semibold text-gray-700 uppercase tracking-wider">
                            Data Do
                        </label>
                        <input
                            type="datetime-local"
                            value={dateTo}
                            onChange={(e) => setDateTo(e.target.value)}
                            className="p-4 rounded-xl border border-gray-300 bg-white text-base shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:outline-none transition-all hover:border-gray-400"
                        />
                    </div>

                    <div className="flex items-end gap-6">
                        <button
                            onClick={handleGenerateChart}
                            className="flex-1 h-[54px] bg-blue-600 hover:bg-blue-700 text-white px-6 rounded-xl flex justify-center items-center gap-3 font-bold transition-all shadow-md hover:shadow-lg active:scale-95"
                        >
                            <RefreshCw size={22} /> Generuj
                        </button>
                        <button
                            onClick={handleDownloadPdf}
                            className="w-[54px] h-[54px] bg-red-600 hover:bg-red-700 text-white rounded-xl flex justify-center items-center transition-all shadow-md hover:shadow-lg active:scale-95"
                            title="Pobierz PDF"
                        >
                            <Download size={22} />
                        </button>
                    </div>
                </div>
            </div>

            {/* --- OBSZAR WYKRESU --- */}
            <div style={{ minHeight: '500px', width: '100%' }} className="bg-white p-6 border-t border-gray-100 mt-4">
                <div className="flex justify-between items-center mb-6">
                    <h3 className="text-lg font-semibold text-gray-700">Wizualizacja danych</h3>
                    <div className="flex gap-2">
                        <button onClick={() => setViewType('line')} className={`px-4 py-2 text-sm rounded-lg transition ${viewType === 'line' ? 'bg-blue-600 text-white font-bold shadow-md' : 'bg-gray-100 hover:bg-gray-200 text-gray-600'}`}>Liniowy</button>
                        <button onClick={() => setViewType('bar')} className={`px-4 py-2 text-sm rounded-lg transition ${viewType === 'bar' ? 'bg-blue-600 text-white font-bold shadow-md' : 'bg-gray-100 hover:bg-gray-200 text-gray-600'}`}>Słupkowy</button>
                        <button onClick={() => setViewType('pie')} className={`px-4 py-2 text-sm rounded-lg transition ${viewType === 'pie' ? 'bg-blue-600 text-white font-bold shadow-md' : 'bg-gray-100 hover:bg-gray-200 text-gray-600'}`}>Kołowy (Suma)</button>
                    </div>
                </div>

                <div className="relative" style={{ height: '400px' }}>
                    {isLoading && (
                        <div className="absolute inset-0 z-10 flex items-center justify-center bg-white bg-opacity-70">
                            <div className="flex flex-col items-center">
                                <RefreshCw className="animate-spin text-blue-600 mb-2" size={32} />
                                <p className="text-gray-500 font-medium">Pobieranie danych...</p>
                            </div>
                        </div>
                    )}

                    {fetchError && (
                        <div className="absolute inset-0 z-10 flex items-center justify-center bg-red-50 rounded-lg">
                            <div className="text-center p-6">
                                <p className="text-red-600 font-bold text-lg mb-2">Błąd!</p>
                                <p className="text-red-500">{fetchError}</p>
                                <button onClick={handleGenerateChart} className="mt-4 text-blue-600 font-semibold underline">Spróbuj ponownie</button>
                            </div>
                        </div>
                    )}

                    {devices.length === 0 && !isLoading && (
                         <div className="absolute inset-0 z-10 flex items-center justify-center bg-gray-50 rounded-lg border-2 border-dashed border-gray-200">
                            <p className="text-gray-500 font-medium text-lg">Brak dostępnych urządzeń w systemie.</p>
                         </div>
                    )}

                    {chartData.length === 0 && !isLoading && !fetchError && devices.length > 0 && (
                        <div className="absolute inset-0 z-10 flex items-center justify-center bg-gray-50 rounded-lg border-2 border-dashed border-gray-200">
                            <div className="text-center">
                                <p className="text-gray-500 font-medium text-lg">Brak danych do wyświetlenia dla wybranego okresu.</p>
                                <p className="text-gray-400 text-sm mt-1">Spróbuj zmienić zakres dat lub typ danych.</p>
                            </div>
                        </div>
                    )}

                    <ResponsiveContainer width="100%" height="100%">
                        {viewType === 'pie' ? (
                            <PieChart>
                                <Pie
                                    data={chartData}
                                    cx="50%"
                                    cy="50%"
                                    label={({name, value}) => `${name}: ${value.toFixed(2)} kWh`}
                                    outerRadius={100}
                                    dataKey="value"
                                    nameKey="name"
                                >
                                    {chartData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={PIE_COLORS[entry.name] || COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                                <Legend />
                            </PieChart>
                        ) : viewType === 'bar' ? (
                            <BarChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                                <XAxis dataKey="timestamp" tick={{fontSize: 12}} tickMargin={10} />
                                <YAxis tickFormatter={formatYAxis} width={80} tick={{fontSize: 12}} />
                                <Tooltip formatter={formatTooltip} contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}} />
                                <Legend wrapperStyle={{paddingTop: '20px'}} />
                                {seriesKeys.length > 0
                                    ? seriesKeys.map((key, idx) => <Bar key={key} dataKey={key} name={deviceNameMap[key] || key} fill={COLORS[idx % COLORS.length]} radius={[4, 4, 0, 0]} />)
                                    : <Bar dataKey="value" name="Wartość" fill="#2563eb" radius={[4, 4, 0, 0]} />
                                }
                            </BarChart>
                        ) : (
                            <LineChart data={chartData}>
                                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                                <XAxis dataKey="timestamp" tick={{fontSize: 12}} tickMargin={10} />
                                <YAxis tickFormatter={formatYAxis} width={80} tick={{fontSize: 12}} />
                                <Tooltip formatter={formatTooltip} contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}} />
                                <Legend wrapperStyle={{paddingTop: '20px'}} />
                                {seriesKeys.length > 0
                                    ? seriesKeys.map((key, idx) => <Line key={key} type="monotone" dataKey={key} name={deviceNameMap[key] || key} stroke={COLORS[idx % COLORS.length]} strokeWidth={3} dot={false} activeDot={{ r: 6 }} />)
                                    : <Line type="monotone" dataKey="value" name="Wartość" stroke="#2563eb" strokeWidth={3} dot={false} activeDot={{ r: 6 }} />
                                }
                            </LineChart>
                        )}
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
};

export default ReportModule;