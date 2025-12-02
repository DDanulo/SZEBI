import React, { useState, useEffect } from 'react';
import { getSchedulesByDevice, addSchedule, deleteSchedule, getAllDevices, turnDeviceOn, turnDeviceOff } from './ScheduleService';
const ScheduleManager = () => {
    const [devices, setDevices] = useState([]);
    const [selectedDeviceId, setSelectedDeviceId] = useState('');
    const [schedules, setSchedules] = useState([]);
    const [start, setStart] = useState('');
    const [end, setEnd] = useState('');


    useEffect(() => {
        loadDevices();
    }, []);


    useEffect(() => {
        if (selectedDeviceId) {
            loadSchedules(selectedDeviceId);
        } else {
            setSchedules([]);
        }
    }, [selectedDeviceId]);

    const loadDevices = async () => {
        try {
            const response = await getAllDevices();
            setDevices(response.data);

            if (!selectedDeviceId && response.data.length > 0) {
                setSelectedDeviceId(response.data[0].id);
            }
        } catch (error) {
            console.error("Błąd pobierania urządzeń", error);
        }
    };

    const loadSchedules = async (deviceId) => {
        try {
            const response = await getSchedulesByDevice(deviceId);
            setSchedules(response.data);
        } catch (error) {
            console.error("Błąd pobierania harmonogramu", error);
        }
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        if (!selectedDeviceId) {
            alert("Wybierz urządzenie!");
            return;
        }

        const newSchedule = {
            deviceId: selectedDeviceId,
            start: start,
            end: end
        };

        try {
            await addSchedule(newSchedule);
            setStart('');
            setEnd('');
            alert("Dodano!");
            loadSchedules(selectedDeviceId);
        } catch (error) {
            console.error("Błąd dodawania", error);
            if (error.response && error.response.status === 409) {
                const msg = error.response.data.message || "Błąd dat!";
                alert("Błąd: " + msg);
            } else {
                alert("Wystąpił błąd serwera.");
            }
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteSchedule(id);
            loadSchedules(selectedDeviceId);
        } catch (error) {
            console.error("Błąd usuwania", error);
        }
    };
    // PRZEŁĄCZANIE STANU ---
    const toggleState = async (device, targetState) => {
        try {
            if (targetState === true) {
                await turnDeviceOn(device.id);
            } else {
                await turnDeviceOff(device.id);
            }

            loadDevices();
        } catch (error) {
            alert("Błąd sterowania urządzeniem");
        }
    };

    // Filtrowanie urządzeń
    const activeDevices = devices.filter(d => d.on === true);
    const passiveDevices = devices.filter(d => d.on === false);

    return (
        <div className="p-6 max-w-6xl mx-auto space-y-8 text-gray-100">

            {/* 1. HARMONOGRAM */}
            <div className="bg-gray-800 p-6 rounded-xl shadow-lg border-2 border-gray-500">
                <h2 className="text-2xl font-bold mb-4 border-b-2 border-gray-500 pb-2">📅 Harmonogram</h2>

                {/* Wybór i formularz (bez zmian logicznych, tylko wygląd) */}
                <div className="flex gap-4 mb-4">
                    <div className="flex-1">
                        <label className="block text-sm text-gray-300 mb-1 font-bold">Urządzenie</label>
                        <select
                            className="w-full p-2 rounded bg-gray-700 border-2 border-gray-500 focus:outline-none focus:border-blue-400 text-white"
                            value={selectedDeviceId}
                            onChange={(e) => setSelectedDeviceId(e.target.value)}
                        >
                            {devices.map(dev => (
                                <option key={dev.id} value={dev.id}>{dev.name} ({dev.type})</option>
                            ))}
                        </select>
                    </div>
                </div>

                <form onSubmit={handleAdd} className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                    <div>
                        <label className="block text-sm text-gray-300 mb-1 font-bold">Start</label>
                        <input type="datetime-local" className="w-full p-2 rounded bg-gray-700 border-2 border-gray-500 text-white" value={start} onChange={e => setStart(e.target.value)} required />
                    </div>
                    <div>
                        <label className="block text-sm text-gray-300 mb-1 font-bold">Koniec</label>
                        <input type="datetime-local" className="w-full p-2 rounded bg-gray-700 border-2 border-gray-500 text-white" value={end} onChange={e => setEnd(e.target.value)} required />
                    </div>
                    <div className="flex items-end">
                        <button type="submit" className="w-full bg-blue-600 hover:bg-blue-500 py-2 rounded font-bold border-2 border-blue-400 transition shadow-lg">Dodaj Zadanie</button>
                    </div>
                </form>


                <div className="overflow-x-auto">
                    <table className="w-full text-left text-sm border-collapse border-2 border-gray-400">
                        <thead className="bg-gray-900 text-white uppercase tracking-wider">
                        <tr>
                            <th className="p-3 border-2 border-green-400">Start</th>
                            <th className="p-3 border-2 border-green-400">Stop</th>
                            <th className="p-3 border-2 border-green-400 text-right">Akcja</th>
                        </tr>
                        </thead>
                        <tbody>
                        {schedules.map((item) => (
                            <tr key={item.id} className="hover:bg-gray-700">
                                <td className="p-3 border-2 border-gray-400 text-green-300 font-mono">{new Date(item.start).toLocaleString()}</td>
                                <td className="p-3 border-2 border-gray-400 text-red-300 font-mono">{new Date(item.end).toLocaleString()}</td>
                                <td className="p-3 border-2 border-gray-400 text-right">
                                    <button onClick={() => handleDelete(item.id)} className="bg-red-900/40 text-red-300 border border-red-500 hover:bg-red-900 hover:text-white px-3 py-1 rounded font-bold transition">Usuń</button>
                                </td>
                            </tr>
                        ))}
                        {schedules.length === 0 && <tr><td colSpan="3" className="p-4 border-2 border-gray-400 text-center text-gray-400">Brak zadań w harmonogramie.</td></tr>}
                        </tbody>
                    </table>
                </div>
            </div>


            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">

                {/* TABELA AKTYWNE (ZIELONA RAMKA) */}
                <div className="bg-gray-800 rounded-xl shadow-lg border-2 border-green-600 overflow-hidden">
                    <div className="bg-green-900/30 p-4 border-b-2 border-green-600">
                        <h3 className="text-xl font-bold text-green-400 flex items-center gap-2">⚡ Aktywne (ON)</h3>
                    </div>
                    <div className="p-4 overflow-x-auto">
                        <table className="w-full text-left border-collapse border-2 border-green-600">
                            <thead className="bg-gray-900 text-green-100 uppercase text-xs">
                            <tr>
                                <th className="p-3 border-2 border-green-600">Nazwa</th>
                                <th className="p-3 border-2 border-green-600">Typ</th>
                                <th className="p-3 border-2 border-green-600 text-right">Sterowanie</th>
                            </tr>
                            </thead>
                            <tbody>
                            {activeDevices.map(dev => (
                                <tr key={dev.id} className="hover:bg-gray-700/50">
                                    <td className="p-3 border-2 border-green-600 font-bold text-white">{dev.name}</td>
                                    <td className="p-3 border-2 border-green-600 text-xs font-mono text-green-300">{dev.type}</td>
                                    <td className="p-3 border-2 border-green-600 text-right">
                                        <button
                                            onClick={() => toggleState(dev, false)}
                                            className="bg-red-600 hover:bg-red-500 text-white px-4 py-1 rounded text-xs font-bold border-2 border-red-400 shadow transition"
                                        >
                                            WYŁĄCZ
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            {activeDevices.length === 0 && <tr><td colSpan="3" className="p-6 border-2 border-green-600 text-center text-gray-400 italic">Brak włączonych urządzeń</td></tr>}
                            </tbody>
                        </table>
                    </div>
                </div>


                <div className="bg-gray-800 rounded-xl shadow-lg border-2 border-gray-400 overflow-hidden">
                    <div className="bg-gray-700/40 p-4 border-b-2 border-gray-400">
                        <h3 className="text-xl font-bold text-gray-300 flex items-center gap-2">💤 Pasywne (OFF)</h3>
                    </div>
                    <div className="p-4 overflow-x-auto">
                        <table className="w-full text-left border-collapse border-2 border-gray-400">
                            <thead className="bg-gray-900 text-gray-300 uppercase text-xs">
                            <tr>
                                <th className="p-3 border-2 border-gray-400">Nazwa</th>
                                <th className="p-3 border-2 border-gray-400">Typ</th>
                                <th className="p-3 border-2 border-gray-400 text-right">Sterowanie</th>
                            </tr>
                            </thead>
                            <tbody>
                            {passiveDevices.map(dev => (
                                <tr key={dev.id} className="hover:bg-gray-700/50">
                                    <td className="p-3 border-2 border-gray-400 font-semibold text-gray-300">{dev.name}</td>
                                    <td className="p-3 border-2 border-gray-400 text-xs font-mono text-gray-500">{dev.type}</td>
                                    <td className="p-3 border-2 border-gray-400 text-right">
                                        <button
                                            onClick={() => toggleState(dev, true)}
                                            className="bg-green-600 hover:bg-green-500 text-white px-4 py-1 rounded text-xs font-bold border-2 border-green-400 shadow transition"
                                        >
                                            WŁĄCZ
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            {passiveDevices.length === 0 && <tr><td colSpan="3" className="p-6 border-2 border-gray-400 text-center text-gray-500 italic">Wszystko włączone!</td></tr>}
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default ScheduleManager;