// src/pages/Auth/LoginPage.jsx
import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Card, Spinner } from 'react-bootstrap';
import { useAuth } from './AuthContext.jsx';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [loginState, setLoginState] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { login, user } = useAuth();
    const navigate = useNavigate();


    useEffect(() => {
        if (user && window.location.pathname === '/login') {
            navigate('/');
        }
    }, [user, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);

        try {
            await login(loginState, password);

        } catch (err) {
            console.log(err)
            const { status, message, code } = err || {};

            let errorMessage = 'Coś poszło nie tak. Spróbuj ponownie.';

            if (message?.includes('is inactive')) {
                errorMessage = 'Konto jest nieaktywne. Skontaktuj się z administratorem.';
            } else if (code === 'INVALID_CREDENTIALS' || status === 401) {
                errorMessage = 'Nieprawidłowy login lub hasło.';
            } else if (status === 404) {
                errorMessage = 'Nieprawidłowy login lub hasło.';
            } else if (status === 400) {
                errorMessage = message || 'Wprowadzono niepoprawne dane.';
            } else if (status === 500) {
                errorMessage = 'Błąd serwera. Spróbuj ponownie za chwilę.';
            } else if (message) {
                errorMessage = message;
            }

            setError(errorMessage);
            console.error('Błąd logowania:', err);

        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: '420px' }}>
            <Card className="shadow-lg border-0">
                <Card.Header as="h3" className="text-center bg-primary text-white py-3">
                    Logowanie
                </Card.Header>
                <Card.Body className="p-4">
                    {error && (
                        <Alert variant="danger" className="d-flex justify-content-between align-items-start">
                            <div>
                                <strong>Błąd:</strong> {error}
                            </div>

                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={() => setError('')}
                                className="ms-3"
                            >
                                Zamknij
                            </Button>
                        </Alert>
                    )}

                    <Form onSubmit={handleSubmit} className="mt-3">
                        <Form.Group className="mb-4" controlId="formLogin">
                            <Form.Label className="fw-bold">Login</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Wpisz login"
                                value={loginState}
                                onChange={(e) => setLoginState(e.target.value)}
                                required
                                disabled={isSubmitting}
                                autoFocus
                            />
                        </Form.Group>

                        <Form.Group className="mb-4" controlId="formPassword">
                            <Form.Label className="fw-bold">Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Wpisz hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                disabled={isSubmitting}
                            />
                        </Form.Group>

                        <Button
                            variant="primary"
                            type="submit"
                            className="w-100 py-2 fw-bold"
                            disabled={isSubmitting}
                            size="lg"
                        >
                            {isSubmitting ? (
                                <>
                                    <Spinner as="span" animation="border" size="sm" role="status" className="me-2" />
                                    Trwa logowanie...
                                </>
                            ) : (
                                'Zaloguj się'
                            )}
                        </Button>

                        <div className="text-center mt-4">
                            <p className="text-muted mb-2">Nie masz konta?</p>
                            <Button variant="link" onClick={() => navigate('/register')} className="p-0">
                                Zarejestruj się
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default LoginPage;