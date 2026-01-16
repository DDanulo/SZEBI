import React, { useState, useEffect } from 'react';
import {
    getSchedulesByDevice,
    addSchedule,
    deleteSchedule,
    getAllDevices,
    turnDeviceOn,
    turnDeviceOff,
    addDevice,
    removeDevice,
    requestAddDevice,
    requestRemoveDevice
} from './ScheduleService';
import './ScheduleManager.css';
import { useAuth } from '../Administration/AuthContext';

const ScheduleManager = () => {
    const [devices, setDevices] = useState([]);
    const [selectedDeviceId, setSelectedDeviceId] = useState('');
    const [schedules, setSchedules] = useState([]);

    const [turnOnTime, setTurnOnTime] = useState('');
    const [turnOffTime, setTurnOffTime] = useState('');
    const [isRecurring, setIsRecurring] = useState(false);

    const [newDeviceName, setNewDeviceName] = useState('');
    const [newDeviceType, setNewDeviceType] = useState('APPLIANCE');
    const [area, setArea] = useState('');

    const { hasRole } = useAuth();
    const isStaff = hasRole('ADMIN') || hasRole('ENGINEER');

    // --- LOGIKA DODAWANIA ---
    const handleCreateDevice = async (e) => {
        e.preventDefault();
        const trimmedName = newDeviceName.trim();

        if (!trimmedName || !/^[a-zA-Z0-9_ ]+$/.test(trimmedName)) {
            alert("Nazwa nie może być pusta i może zawierać tylko litery i cyfry!");
            return;
        }

        const nameExists = devices.some(d => d.name.toLowerCase() === trimmedName.toLowerCase());
        if (nameExists) {
            alert("Urządzenie o takiej nazwie już istnieje! Wybierz inną.");
            return;
        }

        const parsedArea = parseFloat(area);


        if (newDeviceType !== 'WIND' && (isNaN(parsedArea) || parsedArea <= 0)) {
            alert('Powierzchnia musi być większa od 0.');
            return;
        }

        try {

            const extraParams = { area: newDeviceType === 'WIND' ? 0 : parsedArea };

            if (isStaff) {
                await addDevice(trimmedName, newDeviceType, extraParams);
                alert("Urządzenie dodane pomyślnie!");
                loadDevices();
            } else {
                await requestAddDevice(trimmedName, newDeviceType, extraParams);
                alert("Wniosek o dodanie urządzenia został wysłany do Administratora.");
            }

            setNewDeviceName('');
            setArea('');

        } catch (error) {
            console.error(error);
            const msg = error.response?.data?.message || "Wystąpił błąd.";
            alert("Błąd: " + msg);
        }
    };


    const handleRemoveDevice = async (id) => {
        const confirmMsg = isStaff ? "Usunąć urządzenie trwale?" : "Wysłać prośbę o usunięcie urządzenia?";
        if(!window.confirm(confirmMsg)) return;

        try {
            if (isStaff) {
                await removeDevice(id);
                alert("Urządzenie usunięte.");
                setDevices(devices.filter(d => d.id !== id));
                if (selectedDeviceId === id) {
                    setSelectedDeviceId('');
                    setSchedules([]);
                }
            } else {
                await requestRemoveDevice(id);
                alert("Wniosek o usunięcie został wysłany do Administratora.");
            }
        } catch (error) {
            console.error(error);
            alert("Błąd podczas usuwania.");
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

    const handleAddSchedule = async (e) => {
        e.preventDefault();
        if (!selectedDeviceId) {
            alert("Wybierz urządzenie!");
            return;
        }

        const newSchedule = {
            deviceId: selectedDeviceId,
            turnOn: turnOnTime,
            turnOff: turnOffTime,
            recurring: isRecurring
        };

        try {
            await addSchedule(newSchedule);
            setTurnOnTime('');
            setTurnOffTime('');
            setIsRecurring(false);
            alert("Zaplanowano!");
            loadSchedules(selectedDeviceId);
        } catch (error) {
            console.error("Błąd dodawania", error);
            alert("Wystąpił błąd: " + (error.response?.data?.message || error.message));
        }
    };

    const handleDeleteSchedule = async (id) => {
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
            setTimeout(loadDevices, 200);
        } catch (error) {
            console.error(error);
            alert("Błąd sterowania urządzeniem");
        }
    };

    const activeDevices = devices.filter(d => d.on === true);
    const passiveDevices = devices.filter(d => d.on === false);


    const getEnergyText = (dev) => {
        return (
            <div style={{ fontSize: '12px', marginTop: '4px', lineHeight: '1.4' }}>
                {/* Dla AGD pokaż tylko zużycie */}
                {dev.type === 'APPLIANCE' && (
                    <div style={{ color: '#ff8080' }}>
                        📉 Zużycie: <b>{dev.totalConsumed || 0} kWh</b>
                    </div>
                )}

                {/* Dla Wiatraków i Paneli pokaż tylko produkcję */}
                {(dev.type === 'WIND' || dev.type === 'SOLAR') && (
                    <div style={{ color: '#80ff80' }}>
                        📈 Produkcja: <b>{dev.totalGenerated || 0} kWh</b>
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="schedule-container">
            <div className="schedule-layout">

                <div className="left-column">

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
                                        <option value="APPLIANCE">AGD (Zużywa)</option>
                                        <option value="SOLAR">Panel Solarny (Produkuje)</option>
                                        <option value="WIND">Wiatrak (Produkuje)</option>
                                    </select>
                                </div>
                                <div className="form-group" style={{flex: 1}}>
                                    {/* ZMIANA: Blokujemy input dla Wiatraków */}
                                    <label>{newDeviceType === 'APPLIANCE' ? "Zyżycie (kwh)" : "Pow. (m²)"}</label>
                                    <input
                                        type="number"
                                        className="form-input"
                                        placeholder={newDeviceType === 'WIND' ? "Nie dotyczy" : "Wymagane"}
                                        value={area}
                                        onChange={(e) => setArea(e.target.value)}
                                        disabled={newDeviceType === 'WIND'}
                                        required={newDeviceType !== 'WIND'}
                                    />
                                </div>
                            </div>

                            <button type="submit" className="btn-primary">
                                {isStaff ? "Dodaj Urządzenie +" : "Wyślij wniosek o dodanie 📩"}
                            </button>
                        </form>
                    </div>

                    <div className="schedule-card">
                        <h3>⚡ Aktywne ({activeDevices.length})</h3>
                        {renderDeviceTable(activeDevices, false)}
                    </div>

                    <div className="schedule-card">
                        <h3>💤 Pasywne ({passiveDevices.length})</h3>
                        {renderDeviceTable(passiveDevices, true)}
                    </div>
                </div>

                <div className="right-column">
                    <div className="schedule-card">
                        <h3>📅 Harmonogram</h3>

                        <div style={{marginBottom: '20px'}}>
                            <div className="form-group">
                                <label>Wybierz urządzenie</label>
                                <select
                                    className="form-input"
                                    value={selectedDeviceId} onChange={(e) => setSelectedDeviceId(e.target.value)}
                                >
                                    <option value="">-- Wybierz --</option>
                                    {devices.map(dev => (
                                        <option key={dev.id} value={dev.id}>
                                            {dev.name} ({dev.type})
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-row">
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Start (ON)</label>
                                    <input type="datetime-local" className="form-input" value={turnOnTime} onChange={e => setTurnOnTime(e.target.value)} required />
                                </div>
                                <div className="form-group" style={{flex: 1}}>
                                    <label>Stop (OFF)</label>
                                    <input type="datetime-local" className="form-input" value={turnOffTime} onChange={e => setTurnOffTime(e.target.value)} required />
                                </div>
                            </div>

                            <div className="form-group" style={{display: 'flex', alignItems: 'center', gap: '10px', marginTop: '10px'}}>
                                <input
                                    type="checkbox"
                                    id="recurringCheck"
                                    checked={isRecurring}
                                    onChange={(e) => setIsRecurring(e.target.checked)}
                                    style={{width: '18px', height: '18px', cursor: 'pointer'}}
                                />
                                <label htmlFor="recurringCheck" style={{marginBottom: 0, cursor: 'pointer', color: isRecurring ? '#646cff' : '#aaa'}}>
                                    Powtarzaj codziennie
                                </label>
                            </div>

                            <button onClick={handleAddSchedule} className="btn-primary">Zaplanuj Zadanie</button>
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
                                            <div style={{color: '#34d399', fontSize: '0.9em'}}>
                                                Włącz: {new Date(item.turnOn).toLocaleString()}
                                            </div>
                                            <div style={{color: '#f87171', fontSize: '0.9em'}}>
                                                Wyłącz: {new Date(item.turnOff).toLocaleString()}
                                            </div>
                                            {item.recurring && (
                                                <div style={{color: '#646cff', fontSize: '0.75em', fontWeight: 'bold'}}>
                                                    ↻ Codziennie
                                                </div>
                                            )}
                                        </td>
                                        <td style={{textAlign: 'right'}}>
                                            <button onClick={() => handleDeleteSchedule(item.id)} className="btn-small btn-danger">Usuń</button>
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

    function renderDeviceTable(list, isPassive) {
        if (list.length === 0) return <p style={{color: '#666', fontStyle: 'italic', padding: '10px'}}>Pusto.</p>;
        return (
            <table className="device-table">
                <thead>
                <tr>
                    <th>Urządzenie</th>
                    <th style={{textAlign: 'right'}}>Akcja</th>
                </tr>
                </thead>
                <tbody>
                {list.map(dev => (
                    <tr key={dev.id}>
                        <td>
                            <div style={{fontWeight: 'bold'}}>
                                {dev.name}
                            </div>
                            <div style={{fontSize: '0.8em', color: '#888'}}>{dev.type}</div>
                            {getEnergyText(dev)}
                        </td>
                        <td style={{textAlign: 'right'}}>
                            <button onClick={() => toggleState(dev, isPassive)} className={`btn-small ${isPassive ? 'btn-success' : 'btn-neutral'}`}>
                                {isPassive ? 'ON' : 'OFF'}\
                            </button>

                            <button
                                onClick={() => handleRemoveDevice(dev.id)}
                                className="btn-small btn-danger"
                                title={isStaff ? "Usuń trwale" : "Zgłoś do usunięcia"}
                            >
                                ×
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    }
};

export default ScheduleManager;