import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Container, Form, Button, Alert, Spinner } from 'react-bootstrap';
import api from './api.js';

const AdminUsersEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const [role, setRole] = useState('resident'); // resident / engineer / administrator

    // Fetch user data
    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await api.get(`/users/${id}`);
                const data = response.data;
                setUser(data);
                setRole(data.role?.toLowerCase());
            } catch (err) {
                console.error(err);
                setError("Nie udało się pobrać danych użytkownika.");
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, [id]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        console.log("dupa")
        console.log(name,value,type,checked)
        setUser((prev) => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError(null);
        setSuccess(null);

        try {
            let url = '';
            let body = { ...user };


            let req = {
                login: body.login,
                password: body.password,
                firstName: body.firstName,
                lastName: body.lastName,
                email: body.email,
            }

            if (role === 'resident') {
                url = '/users/residents';
                req.room = body.details?.roomNumber;
                body.room = body.details?.roomNumber;
                console.log(req)
            }
            else if (role === 'engineer') url = '/users/engineers';
            else if (role === 'admin') url = '/users/administrators';

            await api.patch(url, body);
            setSuccess('Dane użytkownika zostały zaktualizowane pomyślnie.');
            console.log("cos tam ",req)
            const merged = {
                ...user,
                ...req
            }
            console.log("cos tam ",merged)
            setUser(merged);
        } catch (err) {
            console.error(err);
            setError('Nie udało się zaktualizować danych użytkownika.');
        } finally {
            setSaving(false);
        }
    };

    if (loading) {
        return (
            <Container className="text-center mt-5">
                <Spinner animation="border" />
            </Container>
        );
    }

    if (!user) {
        return (
            <Container className="text-center mt-5">
                <Alert variant="danger">Nie znaleziono użytkownika.</Alert>
                <Button onClick={() => navigate('/users')}>Powrót</Button>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <h2>Edytuj użytkownika: {user.login}</h2>
            <p>Rola: <strong>{role}</strong></p>

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            <Form onSubmit={handleSubmit}>

                <Form.Group className="mb-3">
                    <Form.Label>Hasło</Form.Label>
                    <Form.Control
                        type="password"
                        name="password"
                        value={user.password || ''}
                        placeholder="Wpisz nowe hasło jeśli chcesz zmienić"
                        onChange={handleChange}
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Imię</Form.Label>
                    <Form.Control
                        type="text"
                        name="firstName"
                        value={user.firstName || ''}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Nazwisko</Form.Label>
                    <Form.Control
                        type="text"
                        name="lastName"
                        value={user.lastName || ''}
                        onChange={handleChange}
                    />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        name="email"
                        value={user.email || ''}
                        onChange={handleChange}
                    />
                </Form.Group>

                {role === 'resident' && (
                    <Form.Group className="mb-3">
                        <Form.Label>Pokój</Form.Label>
                        <Form.Control
                            type="text"
                            value={user.details?.roomNumber || ''}
                            onChange={(e) =>
                                setUser(prev => ({
                                    ...prev,
                                    details: {
                                        ...prev.details,
                                        roomNumber: e.target.value
                                    }
                                }))
                            }
                        />
                    </Form.Group>
                )}


                <Button type="submit" disabled={saving} className="me-2">
                    {saving ? <Spinner as="span" animation="border" size="sm" /> : 'Zapisz'}
                </Button>
                <Button variant="secondary" onClick={() => navigate('/users')}>
                    Powrót
                </Button>
            </Form>
        </Container>
    );
};

export default AdminUsersEditPage;
