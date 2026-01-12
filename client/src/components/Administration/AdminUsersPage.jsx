import React, { useEffect, useState } from 'react';
import { Table, Button, Alert, Spinner, Container, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api';

const AdminUsersPage = () => {
    const navigate = useNavigate();

    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null); // id usera
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const fetchUsers = async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await api.get('/users');
            setUsers(response.data);
        } catch (err) {
            setError('Nie udało się pobrać listy użytkowników.');
            console.error(err);
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
            if (user.is_active) {
                await api.patch(`/users/${user.id}/deactivate`);
                setSuccess(`Użytkownik ${user.email} został dezaktywowany.`);
            } else {
                await api.patch(`/users/${user.id}/activate`);
                setSuccess(`Użytkownik ${user.email} został aktywowany.`);
            }

            // odświeżenie listy
            fetchUsers();
        } catch (err) {
            setError('Nie udało się zmienić statusu użytkownika.');
            console.error(err);
        } finally {
            setActionLoading(null);
        }
    };

    return (

        <Container className="mt-4">
            <h1 className="mb-4">Zarządzanie Użytkownikami</h1>
            <p>Lista wszystkich użytkowników w systemie wraz z możliwością aktywacji lub dezaktywacji kont.</p>

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            {loading ? (
                <div className="text-center mt-5">
                    <Spinner animation="border" />
                </div>
            ) : (
                <Table striped bordered hover responsive className="shadow-sm">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Email</th>
                        <th>Rola</th>
                        <th>Status</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length === 0 ? (
                        <tr>
                            <td colSpan="5" className="text-center text-muted">
                                Brak użytkowników
                            </td>
                        </tr>
                    ) : (
                        users.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.email}</td>
                                <td>{user.role}</td>
                                <td>
                                    {user.is_active ? (
                                        <Badge bg="success">Aktywne</Badge>
                                    ) : (
                                        <Badge bg="secondary">Nieaktywne</Badge>
                                    )}
                                </td>
                                <td>
                                    <Button
                                        size="sm"
                                        variant={user.is_active ? 'danger' : 'success'}
                                        disabled={actionLoading === user.id}
                                        onClick={() => toggleUserStatus(user)}
                                    >
                                        {actionLoading === user.id ? (
                                            <Spinner as="span" animation="border" size="sm" />
                                        ) : user.is_active ? (
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
                Wróć do Katalogu
            </Button>
        </Container>
    );
};

export default AdminUsersPage;
