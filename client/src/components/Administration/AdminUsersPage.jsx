import React, {useEffect, useState} from 'react';
import {Table, Button, Alert, Spinner, Container, Badge} from 'react-bootstrap';
import {useNavigate} from 'react-router-dom';
import api from './api.js';

import { getPendingRequests, approveRequest, rejectRequest } from '../DeviceControl/ScheduleService.js';

const AdminUsersPage = () => {
    const navigate = useNavigate();


    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null);


    const [requests, setRequests] = useState([]);
    const [loadingRequests, setLoadingRequests] = useState(false);

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);


    const fetchData = async () => {
        setLoading(true);
        setError(null);
        try {

            const usersResponse = await api.get('/users');
            const usersData = usersResponse.data.content || usersResponse.data || [];
            setUsers(usersData);


            const requestsResponse = await getPendingRequests();
            setRequests(requestsResponse.data || []);

        } catch (err) {
            console.error(err);
            if (err.response?.status === 403) {
                setError("Brak uprawnień do przeglądania tej strony.");
            } else {
                setError("Nie udało się pobrać danych.");
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);


    const toggleUserStatus = async (user) => {
        setActionLoading(user.id);
        setError(null);
        setSuccess(null);
        try {
            if (user.active) {
                await api.patch(`/users/${user.id}/deactivate`);
                setSuccess(`Użytkownik ${user.login} został dezaktywowany.`);
            } else {
                await api.patch(`/users/${user.id}/activate`);
                setSuccess(`Użytkownik ${user.login} został aktywowany.`);
            }
            fetchData(); // Odśwież wszystko
        } catch (err) {
            setError('Nie udało się zmienić statusu użytkownika.');
        } finally {
            setActionLoading(null);
        }
    };


    const handleRequestDecision = async (reqId, decision) => {
        setActionLoading(reqId);
        setError(null);
        setSuccess(null);
        try {
            if (decision === 'APPROVE') {
                await approveRequest(reqId);
                setSuccess("Wniosek zatwierdzony!");
            } else {
                await rejectRequest(reqId);
                setSuccess("Wniosek odrzucony.");
            }

            const res = await getPendingRequests();
            setRequests(res.data || []);
        } catch (err) {
            console.error(err);
            setError("Błąd podczas przetwarzania wniosku.");
        } finally {
            setActionLoading(null);
        }
    };


    const getUserLoginById = (userId) => {
        const found = users.find(u => u.id === userId);
        return found ? found.login : userId;
    };

    if (error) {
        return (
            <Container className="mt-5 text-center">
                <Alert variant="danger"><h4>Błąd</h4><p>{error}</p></Alert>
                <Button variant="secondary" onClick={() => navigate('/')}>Wróć</Button>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <h1 className="mb-4">Panel Administratora</h1>

            {success && <Alert variant="success">{success}</Alert>}

            {/* --- SEKCJA 1: UŻYTKOWNICY --- */}
            <h3>👤 Zarządzanie Użytkownikami</h3>
            {loading ? (
                <div className="text-center my-3"><Spinner animation="border"/></div>
            ) : (
                <Table striped bordered hover responsive className="shadow-sm mb-5">
                    <thead>
                    <tr>
                        <th>Login</th>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Email</th>
                        <th>Rola</th>
                        <th>Pokój</th>
                        <th>Status</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length === 0 ? (
                        <tr>
                            <td colSpan="8" className="text-center text-muted">
                                Brak użytkowników
                            </td>
                        </tr>
                    ) : (
                        users.map((user) => (
                            <tr key={user.id || user.login}>
                                <td>{user.login || '-'}</td>
                                <td>{user.firstName || user.first_name || '-'}</td>
                                <td>{user.lastName || user.last_name || '-'}</td>
                                <td>{user.email || '-'}</td>
                                <td>{user.role?.name || user.role || '-'}</td>
                                <td>{user.details?.roomNumber || '-'}</td>
                                <td>
                                    {user.active ? (
                                        <Badge bg="success">Aktywne</Badge>
                                    ) : (
                                        <Badge bg="secondary">Nieaktywne</Badge>
                                    )}
                                </td>
                                <td>
                                    <Button
                                        size="sm"
                                        variant={user.active ? 'danger' : 'success'}
                                        disabled={actionLoading === user.id}
                                        onClick={() => toggleUserStatus(user)}
                                    >
                                        {actionLoading === user.id ? <Spinner size="sm" animation="border"/> : (user.active ? 'Dezaktywuj' : 'Aktywuj')}
                                    </Button>
                                    <Button
                                        size="sm"
                                        variant="primary"
                                        onClick={() => navigate(`/users/edit/${user.id}`)}
                                    >
                                        Edytuj
                                    </Button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </Table>
            )}

            {/* --- SEKCJA 2: WNIOSKI O URZĄDZENIA --- */}
            <h3 className="mt-5">📩 Oczekujące Wnioski (Urządzenia)</h3>
            <p className="text-muted">Tutaj zatwierdzasz prośby mieszkańców o dodanie lub usunięcie sprzętu.</p>

            {loading ? (
                <div className="text-center my-3"><Spinner animation="border"/></div>
            ) : (
                <Table striped bordered hover responsive className="shadow-sm border-primary">
                    <thead className="bg-light">
                    <tr>
                        <th>Użytkownik</th>
                        <th>Typ Prośby</th>
                        <th>Szczegóły</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {requests.length === 0 ? (
                        <tr><td colSpan="4" className="text-center py-4">Brak oczekujących wniosków.</td></tr>
                    ) : (
                        requests.map((req) => (
                            <tr key={req.id}>
                                <td>
                                    {/* Wyświetlamy login zamiast surowego UUID */}
                                    <strong>{getUserLoginById(req.userId)}</strong>
                                </td>
                                <td>
                                    {req.requestType === 'ADD' ? (
                                        <Badge bg="primary">DODANIE +</Badge>
                                    ) : (
                                        <Badge bg="warning" text="dark">USUNIĘCIE x</Badge>
                                    )}
                                </td>
                                <td>
                                    {req.requestType === 'ADD' ? (
                                        <>
                                            <strong>{req.deviceName}</strong> ({req.deviceType})<br/>
                                            <small>Powierzchnia: {req.area} m²</small>
                                        </>
                                    ) : (
                                        <>
                                            ID Urządzenia:<br/>
                                            <small style={{fontFamily:'monospace'}}>{req.targetDeviceId}</small>
                                        </>
                                    )}
                                </td>
                                <td>
                                    <div className="d-flex gap-2">
                                        <Button
                                            variant="success" size="sm"
                                            onClick={() => handleRequestDecision(req.id, 'APPROVE')}
                                            disabled={actionLoading === req.id}
                                        >
                                            Zatwierdź
                                        </Button>
                                        <Button
                                            variant="danger" size="sm"
                                            onClick={() => handleRequestDecision(req.id, 'REJECT')}
                                            disabled={actionLoading === req.id}
                                        >
                                            Odrzuć
                                        </Button>
                                        {actionLoading === req.id && <Spinner size="sm" animation="border"/>}
                                    </div>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </Table>
            )}

            <Button variant="secondary" onClick={() => navigate('/')} className="mt-5 mb-5">
                Wróć do strony głównej
            </Button>
            <Button variant="success" onClick={() => navigate('/users/create')}>
                Stwórz użytkownika
            </Button>
        </Container>
    );
};

export default AdminUsersPage;
