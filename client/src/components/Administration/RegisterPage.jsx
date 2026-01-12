// src/pages/Auth/RegisterPage.jsx
import React, { useState } from 'react';
import { Container, Form, Button, Alert, Card } from 'react-bootstrap';
import { useAuth } from './AuthContext.jsx';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [loginState, setLoginState] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { register } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (password !== confirmPassword) {
            setError('Hasła nie są identyczne!');
            return;
        }

        // Minimalna walidacja hasła po stronie klienta
        if (password.length < 6) {
            setError('Hasło musi mieć co najmniej 6 znaków.');
            return;
        }

        setIsSubmitting(true);

        try {
            const isRegistered = await register(loginState, password);

            if (isRegistered) {
                // Po rejestracji użytkownik nie jest automatycznie logowany (Wymaganie D2)
                setSuccess('Rejestracja udana! Zaloguj się teraz.');
                setLoginState('');
                setPassword('');
                setConfirmPassword('');
            }
        } catch (err) {
            setError(err.message || 'Błąd serwera podczas rejestracji.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: '400px' }}>
            <Card>
                <Card.Header as="h3" className="text-center">Rejestracja</Card.Header>
                <Card.Body>
                    {error && <Alert variant="danger">{error}</Alert>}
                    {success && <Alert variant="success">{success}</Alert>}

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

                        <Form.Group className="mb-3">
                            <Form.Label>Potwierdź Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="success" type="submit" className="w-100 mt-3" disabled={isSubmitting}>
                            {isSubmitting ? 'Trwa rejestracja...' : 'Zarejestruj się'}
                        </Button>

                        <p className="mt-3 text-center">
                            Masz już konto? <Button variant="link" onClick={() => navigate('/login')}>Zaloguj</Button>
                        </p>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default RegisterPage;