import React, { useState } from 'react';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';
import { useAuth } from './AuthContext.jsx';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [loginState, setLoginState] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [room, setRoom] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [validated, setValidated] = useState(false);


    const { register } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;

        setValidated(true);

        if (!form.checkValidity()) {
            e.stopPropagation();
            return;
        }

        setIsSubmitting(true);
        setError('');

        try {
            await register(loginState, password, firstName, lastName, email, room);

            window.alert(
                'Rejestracja zakończona sukcesem. Konto zostanie aktywowane przez administratora.'
            );

            navigate('/login');
        } catch (err) {
            const { status, message } = err || {};

            let errorMessage = 'Coś poszło nie tak. Spróbuj ponownie.';

            if (message?.includes('already')) {
                errorMessage = 'Login lub email już istnieje.';
            } else if (status === 400) {
                errorMessage = message || 'Niepoprawne dane.';
            } else if (status === 500) {
                errorMessage = 'Błąd serwera.';
            }

            setError(errorMessage);
        } finally {
            setIsSubmitting(false);
        }
    };


    return (
        <Container className="mt-5" style={{ maxWidth: '400px' }}>
            <Card>
                <Card.Header as="h3" className="text-center">Rejestracja</Card.Header>
                <Card.Body>
                    {error && (
                        <Alert
                            variant="danger"
                            className="d-flex justify-content-between align-items-start"
                        >
                            <div>
                                <strong>Błąd:</strong> {error}
                            </div>
                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={() => setError('')}
                            >
                                Zamknij
                            </Button>
                        </Alert>
                    )}


                    <Form noValidate validated={validated} onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Login</Form.Label>
                            <Form.Control
                                type="text"
                                value={loginState}
                                onChange={(e) => setLoginState(e.target.value)}
                                required
                                minLength={8}
                                isInvalid={validated && loginState.length < 8}
                            />
                            <Form.Control.Feedback type="invalid">
                                Login musi mieć długość minimum 8 znaków.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                minLength={8}
                                required
                                isInvalid={validated && password.length < 8}
                            />
                            <Form.Control.Feedback type="invalid">
                                Hasło musi mieć minimum 8 znaków.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Potwierdź Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                                isInvalid={validated && password !== confirmPassword}
                            />
                            <Form.Control.Feedback type="invalid">
                                Hasła muszą być identyczne.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Imię</Form.Label>
                            <Form.Control
                                type="text"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Nazwisko</Form.Label>
                            <Form.Control
                                type="text"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                Podaj poprawny adres email.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Pokój</Form.Label>
                            <Form.Control
                                type="text"
                                value={room}
                                onChange={(e) => setRoom(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button
                            variant="success"
                            type="submit"
                            className="w-100 mt-3"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? 'Trwa rejestracja...' : 'Zarejestruj się'}
                        </Button>

                        <p className="mt-3 text-center">
                            Masz już konto?{' '}
                            <Button variant="link" onClick={() => navigate('/login')}>
                                Zaloguj się
                            </Button>
                        </p>
                    </Form>
                </Card.Body>
            </Card>


        </Container>
    );
};

export default RegisterPage;
