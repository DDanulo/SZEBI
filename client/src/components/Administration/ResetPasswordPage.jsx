import { useSearchParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { Container, Form, Button, Alert, Spinner } from 'react-bootstrap';
import api from './api.js';

const ResetPasswordPage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const token = searchParams.get('token');

    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!password || !confirmPassword) {
            setError('Wszystkie pola są wymagane.');
            return;
        }

        if (password !== confirmPassword) {
            setError('Hasła nie są takie same.');
            return;
        }

        try {
            setLoading(true);

            await api.post('/users/reset-password', {
                token,
                password
            });

            setSuccess('Hasło zostało zmienione. Możesz się zalogować.');
            setTimeout(() => navigate('/login'), 2000);

        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Nie udało się zresetować hasła.'
            );
        } finally {
            setLoading(false);
        }
    };

    if (!token) {
        return (
            <Container className="mt-5">
                <Alert variant="danger">
                    Nieprawidłowy lub brak tokenu resetu hasła.
                </Alert>
            </Container>
        );
    }

    return (
        <Container className="mt-5" style={{ maxWidth: '500px' }}>
            <h2 className="mb-4 text-center">Reset hasła</h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            {!success && (
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Nowe hasło</Form.Label>
                        <Form.Control
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Powtórz hasło</Form.Label>
                        <Form.Control
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />
                    </Form.Group>

                    <Button
                        type="submit"
                        className="w-100"
                        disabled={loading}
                    >
                        {loading ? (
                            <Spinner size="sm" />
                        ) : (
                            'Zmień hasło'
                        )}
                    </Button>
                </Form>
            )}
        </Container>
    );
};

export default ResetPasswordPage;
