import React, {useEffect, useState} from 'react';
import {Table, Button, Alert, Spinner, Container, Badge} from 'react-bootstrap';
import {useNavigate} from 'react-router-dom';
import api from './api.js';

const AdminUsersPage = () => {
        const navigate = useNavigate();

        const [users, setUsers] = useState([]);
        const [loading, setLoading] = useState(true);
        const [actionLoading, setActionLoading] = useState(null);
        const [error, setError] = useState(null);
        const [success, setSuccess] = useState(null);

        const fetchUsers = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await api.get('/users');
                setUsers(response.data.content || response.data || []);
            } catch (err) {
                console.error(err);

                if (err.response?.status === 403) {
                    setError("Brak uprawnień do przeglądania tej strony.");
                } else {
                    setError("Nie udało się pobrać listy użytkowników.");
                }
            } finally {
                setLoading(false);
            }
        };

        useEffect(() => {
            fetchUsers();
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

                fetchUsers();
            } catch (err) {
                if (err.response?.status === 403) {
                    setError('Brak uprawnień do wykonania tej akcji.');
                } else {
                    setError('Nie udało się zmienić statusu użytkownika.');
                }
                console.error(err);
            } finally {
                setActionLoading(null);
            }
        };

        if (error) {
            return (
                <Container className="mt-5 text-center">
                    <Alert variant="danger">
                        <h4>Błąd</h4>
                        <p>{error}</p>
                    </Alert>

                    <Button variant="secondary" onClick={() => navigate('/')}>
                        Wróć do strony głównej
                    </Button>
                </Container>
            );
        }

        return (
            <Container className="mt-4">
                <h1 className="mb-4">Zarządzanie Użytkownikami</h1>
                <p>Lista wszystkich użytkowników w systemie wraz z możliwością aktywacji lub dezaktywacji kont.</p>

                {/* Komunikaty błędów i sukcesów */}
                {success && <Alert variant="success">{success}</Alert>}

                {loading ? (
                    <div className="text-center mt-5">
                        <Spinner animation="border"/>
                    </div>
                ) : (
                    <Table striped bordered hover responsive className="shadow-sm">
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
                                            disabled={actionLoading === (user.id || user.login)}
                                            onClick={() => toggleUserStatus(user)}
                                        >
                                            {actionLoading === (user.id || user.login) ? (
                                                <Spinner as="span" animation="border" size="sm"/>
                                            ) : user.active ? (
                                                'Dezaktywuj'
                                            ) : (
                                                'Aktywuj'
                                            )}
                                        </Button>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </Table>
                )}

                <Button variant="secondary" onClick={() => navigate('/')} className="mt-3">
                    Wróć do strony głównej
                </Button>
            </Container>
        );
    }
;

export default AdminUsersPage;