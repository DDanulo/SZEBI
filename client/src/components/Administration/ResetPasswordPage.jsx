import { useSearchParams, useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
import { Container, Form, Button, Alert, Spinner } from 'react-bootstrap';
import api from './api.js';
import { useForm } from 'react-hook-form';

const ResetPasswordPage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const token = searchParams.get('token');

    const [loading, setLoading] = useState(false);
    const [serverError, setServerError] = useState('');
    const [success, setSuccess] = useState(false);

    const { register, handleSubmit, watch, formState: { errors } } = useForm();

    const onSubmit = async (data) => {
        setServerError('');
        try {
            setLoading(true);
            await api.post('/users/reset-password', {
                token,
                password: data.password
            });
            setSuccess(true);
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setServerError(
                err.response?.data?.message || 'Nie udało się zresetować hasła.'
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

    const getGroupClass = (fieldName) => errors[fieldName] ? 'mb-2 border border-danger rounded p-2' : 'mb-2';

    return (
        <Container className="mt-5" style={{ maxWidth: '500px' }}>
            <h2 className="mb-4 text-center">Reset hasła</h2>

            {serverError && <Alert variant="danger">{serverError}</Alert>}
            {success && <Alert variant="success">Hasło zostało zmienione. Możesz się zalogować.</Alert>}

            {!success && (
                <Form onSubmit={handleSubmit(onSubmit)}>
                    <Form.Group className={getGroupClass('password')}>
                        <Form.Label className="mb-1">Hasło</Form.Label>
                        <Form.Control
                            type="password"
                            {...register('password', {
                                required: 'Hasło jest wymagane',
                                validate: {
                                    length: v => v.length >= 8 || 'Minimum 8 znaków',
                                    lower: v => /[a-z]/.test(v) || 'Min. 1 mała litera',
                                    upper: v => /[A-Z]/.test(v) || 'Min. 1 duża litera',
                                    special: v => /[^A-Za-z0-9]/.test(v) || 'Min. 1 znak specjalny',
                                },
                            })}
                            isInvalid={!!errors.password}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.password?.message}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className={getGroupClass('confirmPassword')}>
                        <Form.Label className="mb-1">Potwierdź Hasło</Form.Label>
                        <Form.Control
                            type="password"
                            {...register('confirmPassword', {
                                required: 'Potwierdź hasło',
                                validate: value => value === watch('password') || 'Hasła muszą być identyczne'
                            })}
                            isInvalid={!!errors.confirmPassword}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.confirmPassword?.message}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Button type="submit" className="w-100 mt-3" disabled={loading}>
                        {loading ? <Spinner animation="border" size="sm" /> : 'Zmień hasło'}
                    </Button>
                </Form>
            )}
        </Container>
    );
};

export default ResetPasswordPage;
