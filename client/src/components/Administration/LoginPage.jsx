// src/pages/Auth/LoginPage.jsx
import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Card } from 'react-bootstrap';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [loginState, setLoginState] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { login, user } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        // Jeśli użytkownik jest już zalogowany, przekierowujemy na stronę główną
        if (user) {
            navigate('/');
        }
    }, [user, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);

        const success = await login(loginState, password);

        if (success) {
            // Logowanie udane. Sprawdzamy rolę, aby przekierować na odpowiednią ścieżkę.
            // Odczyt roli z tokena zapisanego w localStorage (dla szybkiego przekierowania)
            try {
                const token = localStorage.getItem('accessToken');
                if (token) {
                    const payload = JSON.parse(atob(token.split('.')[1]));
                    if (payload.role === 'EMPLOYEE') {
                        navigate('/admin/orders');
                    } else {
                        navigate('/');
                    }
                } else {
                    navigate('/');
                }
            } catch (err) {
                // W razie błędu parsowania tokena, domyślnie przekierowujemy na stronę główną
                navigate('/');
            }
        } else {
            setError('Błędny login lub hasło. Spróbuj ponownie.');
        }

        setIsSubmitting(false);
    };

    return (
        <Container className="mt-5" style={{ maxWidth: '400px' }}>
            <Card>
                <Card.Header as="h3" className="text-center">Logowanie</Card.Header>
                <Card.Body>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleSubmit}>

                        <Form.Group className="mb-3">
                            <Form.Label>Login (Email/Nazwa)</Form.Label>
                            <Form.Control
                                type="text"
                                value={loginState}
                                onChange={(e) => setLoginState(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100 mt-3" disabled={isSubmitting}>
                            {isSubmitting ? 'Trwa logowanie...' : 'Zaloguj'}
                        </Button>

                        <p className="mt-3 text-center">
                            Nie masz konta? <Button variant="link" onClick={() => navigate('/register')}>Zarejestruj się</Button>
                        </p>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default LoginPage;