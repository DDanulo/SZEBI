import React, { useState, useEffect } from 'react';
import { getSchedulesByDevice, addSchedule, deleteSchedule, getAllDevices, turnDeviceOn, turnDeviceOff, addDevice, removeDevice } from './ScheduleService';
// Importujemy Twój CSS
import './ScheduleManager.css';

const ScheduleManager = () => {
    const [devices, setDevices] = useState([]);
    const [selectedDeviceId, setSelectedDeviceId] = useState('');
    const [schedules, setSchedules] = useState([]);

    // Zmienione nazwy stanów dla jasności (backend oczekuje turnOn/turnOff)
    const [turnOnTime, setTurnOnTime] = useState('');
    const [turnOffTime, setTurnOffTime] = useState('');

    const [newDeviceName, setNewDeviceName] = useState('');
    const [newDeviceType, setNewDeviceType] = useState('APPLIANCE');
    const [area, setArea] = useState('');

    // --- LOGIKA ---
    const handleCreateDevice = async (e) => {
        e.preventDefault();
        const parsedArea = parseFloat(area);
        if (isNaN(parsedArea) || parsedArea <= 0) {
            alert('Wymagana jest poprawna wartość powierzchni (> 0).');
            return;
        }
        try {
            const extraParams = { area: parsedArea };
            await addDevice(newDeviceName, newDeviceType, extraParams);
            setNewDeviceName('');
            setArea('');
            alert("Urządzenie dodane!");
            loadDevices();
        } catch (error) {
            console.error(error);
            alert("Błąd dodawania urządzenia");
        }
    };

    const handleRemoveDevice = async (id) => {
        if(!window.confirm("Usunąć urządzenie?")) return;
        try {
            await removeDevice(id);
            loadDevices();
        } catch (error) {
            console.error(error);
            alert("Błąd usuwania urządzenia");
        }
    };

    useEffect(() => { loadDevices(); }, []);

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

        // --- ZMIANA: Używamy nazw zgodnych z nowym DTO w Javie ---
        const newSchedule = {
            deviceId: selectedDeviceId,
            turnOn: turnOnTime,   // Zamiast start
            turnOff: turnOffTime  // Zamiast end
        };

        try {
            await addSchedule(newSchedule);
            setTurnOnTime('');
            setTurnOffTime('');
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

    const toggleState = async (device, targetState) => {
        try {
            if (targetState === true) {
                await turnDeviceOn(device.id);
            } else {
                await turnDeviceOff(device.id);
            }
            loadDevices();
        } catch (error) {
            console.error(error);
            alert("Błąd sterowania urządzeniem");
        }
    };

    const activeDevices = devices.filter(d => d.on === true);
    const passiveDevices = devices.filter(d => d.on === false);

    return (
        <div className="schedule-container">
            <div className="schedule-layout">

                {/* --- LEWA KOLUMNA: URZĄDZENIA --- */}
                <div className="left-column">

                    {/* KARTA DODAWANIA */}
                    <div className="schedule-card">
                        <h3>🛠 Dodaj Urządzenie</h3>
                        <form onSubmit={handleCreateDevice}>
                            <div className="form-group">
                                <label>Nazwa urządzenia</label>
                                <input
                                    type="text" className="form-input" placeholder="np. Lodówka"
                                    value={newDeviceName} onChange={(e) => setNewDeviceName(e.target.value)} required
                                />
                            </div>
                            <div className="form-row">
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Typ</label>
                                    <select className="form-input" value={newDeviceType} onChange={(e) => setNewDeviceType(e.target.value)}>
                                        <option value="APPLIANCE">AGD</option>
                                        <option value="SOLAR">Panel Solarny</option>
                                        <option value="WIND">Wiatrak</option>
                                    </select>
                                </div>
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Powierzchnia (m²)</label>
                                    <input
                                        type="number" className="form-input" placeholder="> 0"
                                        value={area} onChange={(e) => setArea(e.target.value)} required min="0.01" step="0.01"
                                    />
                                </div>
                            </div>
                            <button type="submit" className="btn-primary">Dodaj Urządzenie +</button>
                        </form>
                    </div>

                    {/* KARTA LISTA URZĄDZEŃ (AKTYWNE) */}
                    <div className="schedule-card">
                        <h3>
                            ⚡ Aktywne (ON)
                            <span className="status-badge badge-green">{activeDevices.length}</span>
                        </h3>
                        {activeDevices.length === 0 ? (
                            <p style={{color: '#666', fontStyle: 'italic'}}>Brak włączonych urządzeń.</p>
                        ) : (
                            <table className="device-table">
                                <thead>
                                <tr>
                                    <th>Nazwa / Typ</th>
                                    <th style={{textAlign: 'right'}}>Akcja</th>
                                </tr>
                                </thead>
                                <tbody>
                                {activeDevices.map(dev => (
                                    <tr key={dev.id}>
                                        <td>
                                            <div style={{fontWeight: 'bold'}}>{dev.name}</div>
                                            <div style={{fontSize: '0.8em', color: '#888'}}>{dev.type}</div>
                                        </td>
                                        <td style={{textAlign: 'right'}}>
                                            <button onClick={() => toggleState(dev, false)} className="btn-small btn-neutral">OFF</button>
                                            <button onClick={() => handleRemoveDevice(dev.id)} className="btn-small btn-danger">×</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        )}
                    </div>

                    {/* KARTA LISTA URZĄDZEŃ (PASYWNE) */}
                    <div className="schedule-card">
                        <h3>
                            💤 Pasywne (OFF)
                            <span className="status-badge badge-gray">{passiveDevices.length}</span>
                        </h3>
                        {passiveDevices.length === 0 ? (
                            <p style={{color: '#666', fontStyle: 'italic'}}>Wszystko włączone!</p>
                        ) : (
                            <table className="device-table">
                                <thead>
                                <tr>
                                    <th>Nazwa / Typ</th>
                                    <th style={{textAlign: 'right'}}>Akcja</th>
                                </tr>
                                </thead>
                                <tbody>
                                {passiveDevices.map(dev => (
                                    <tr key={dev.id}>
                                        <td>
                                            <div style={{fontWeight: 'bold'}}>{dev.name}</div>
                                            <div style={{fontSize: '0.8em', color: '#888'}}>{dev.type}</div>
                                        </td>
                                        <td style={{textAlign: 'right'}}>
                                            <button onClick={() => toggleState(dev, true)} className="btn-small btn-success">ON</button>
                                            <button onClick={() => handleRemoveDevice(dev.id)} className="btn-small btn-danger">×</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        )}
                    </div>

                </div>

                {/* --- PRAWA KOLUMNA: HARMONOGRAMY --- */}
                <div className="right-column">
                    <div className="schedule-card">
                        <h3>📅 Harmonogram</h3>

                        <div style={{marginBottom: '20px'}}>
                            <div className="form-group">
                                <label>Wybierz urządzenie</label>
                                <select
                                    className="form-input"
                                    value={selectedDeviceId} onChange={(e) => setSelectedDeviceId(e.target.value)}
                                    disabled={devices.length === 0}
                                >
                                    {devices.length > 0 ? (
                                        devices.map(dev => <option key={dev.id} value={dev.id}>{dev.name} ({dev.type})</option>)
                                    ) : <option disabled value="">Brak urządzeń</option>}
                                </select>
                            </div>
                            <div className="form-row">
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Data WŁĄCZENIA</label>
                                    <input type="datetime-local" className="form-input" value={turnOnTime} onChange={e => setTurnOnTime(e.target.value)} required />
                                </div>
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Data WYŁĄCZENIA</label>
                                    <input type="datetime-local" className="form-input" value={turnOffTime} onChange={e => setTurnOffTime(e.target.value)} required />
                                </div>
                            </div>
                            <button onClick={handleAdd} className="btn-primary">Zaplanuj Zadanie</button>
                        </div>

                        <h4 style={{fontSize: '0.9rem', color: '#888', textTransform: 'uppercase', borderBottom: '1px solid #333', paddingBottom: '5px'}}>
                            Zaplanowane zadania:
                        </h4>

                        {schedules.length === 0 ? (
                            <div style={{padding: '20px', textAlign: 'center', color: '#555', fontStyle: 'italic'}}>
                                Brak zadań dla wybranego urządzenia.
                            </div>
                        ) : (
                            <table className="device-table">
                                <tbody>
                                {schedules.map((item) => (
                                    <tr key={item.id}>
                                        <td>
                                            {/* --- TUTAJ POPRAWKA: turnOn / turnOff --- */}
                                            <div style={{color: '#34d399', fontSize: '0.9em'}}>
                                                Włącz: {new Date(item.turnOn).toLocaleString()}
                                            </div>
                                            <div style={{color: '#f87171', fontSize: '0.9em'}}>
                                                Wyłącz: {new Date(item.turnOff).toLocaleString()}
                                            </div>
                                        </td>
                                        <td style={{textAlign: 'right'}}>
                                            <button onClick={() => handleDelete(item.id)} className="btn-small btn-danger">Usuń</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                </div>

            </div>
        </div>
    );
};

export default ScheduleManager;