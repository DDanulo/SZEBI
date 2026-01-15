import React from 'react';
import { useForm } from 'react-hook-form';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';
import { useAuth } from './AuthContext.jsx';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const { register: registerUser } = useAuth();
    const navigate = useNavigate();
    const [serverError, setServerError] = React.useState('');

    const {
        register,
        handleSubmit,
        watch,
        formState: { errors, isSubmitting },
    } = useForm();

    const onSubmit = async (data) => {
        setServerError('');
        try {
            await registerUser(
                data.login,
                data.password,
                data.firstName,
                data.lastName,
                data.email,
                data.room
            );
            alert('Rejestracja zakończona sukcesem!');
            navigate('/login');
        } catch (err) {
            if (err.code === 'CONFLICT' && err.errors?.length > 0) {
                const fieldError = err.errors[0];
                setServerError(`${fieldError.field.toUpperCase()}: ${fieldError.message}`);
            } else {
                setServerError(err.message || 'Coś poszło nie tak');
            }
        }
    };


    const getGroupClass = (fieldName) => errors[fieldName] ? 'mb-2 border border-danger rounded p-2' : 'mb-2';

    return (

        <Container className="mt-5 d-flex justify-content-center">
            <Card style={{ width: '100%', maxWidth: '450px' }}>
                <Card.Header as="h3" className="text-center">
                    Rejestracja
                </Card.Header>
                <Card.Body>
                    {serverError && <Alert variant="danger">{serverError}</Alert>}

                    <Form onSubmit={handleSubmit(onSubmit)} noValidate>
                        {/** Login */}
                        <Form.Group className={getGroupClass('login')}>
                            <Form.Label className="mb-1">Login</Form.Label>
                            <Form.Control
                                type="text"
                                {...register('login', { required: 'Login jest wymagany', minLength: { value: 8, message: 'Login min 8 znaków' } })}
                                isInvalid={!!errors.login}
                            />
                            <Form.Control.Feedback type="invalid">{errors.login?.message}</Form.Control.Feedback>
                        </Form.Group>

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
                                    validate: (value) => value === watch('password') || 'Hasła muszą być identyczne',
                                })}
                                isInvalid={!!errors.confirmPassword}
                            />
                            <Form.Control.Feedback type="invalid">{errors.confirmPassword?.message}</Form.Control.Feedback>
                        </Form.Group>


                        <Form.Group className={getGroupClass('firstName')}>
                            <Form.Label className="mb-1">Imię</Form.Label>
                            <Form.Control
                                type="text"
                                {...register('firstName', { required: 'Imię jest wymagane' })}
                                isInvalid={!!errors.firstName}
                            />
                            <Form.Control.Feedback type="invalid">{errors.firstName?.message}</Form.Control.Feedback>
                        </Form.Group>


                        <Form.Group className={getGroupClass('lastName')}>
                            <Form.Label className="mb-1">Nazwisko</Form.Label>
                            <Form.Control
                                type="text"
                                {...register('lastName', { required: 'Nazwisko jest wymagane' })}
                                isInvalid={!!errors.lastName}
                            />
                            <Form.Control.Feedback type="invalid">{errors.lastName?.message}</Form.Control.Feedback>
                        </Form.Group>


                        <Form.Group className={getGroupClass('email')}>
                            <Form.Label className="mb-1">Email</Form.Label>
                            <Form.Control
                                type="email"
                                {...register('email', {
                                    required: 'Email jest wymagany',
                                    pattern: {
                                        value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                                        message: 'Niepoprawny email',
                                    },
                                })}
                                isInvalid={!!errors.email}
                            />
                            <Form.Control.Feedback type="invalid">{errors.email?.message}</Form.Control.Feedback>
                        </Form.Group>


                        <Form.Group className={getGroupClass('room')}>
                            <Form.Label className="mb-1">Pokój</Form.Label>
                            <Form.Control
                                type="text"
                                {...register('room', { required: 'Pokój jest wymagany' })}
                                isInvalid={!!errors.room}
                            />
                            <Form.Control.Feedback type="invalid">{errors.room?.message}</Form.Control.Feedback>
                        </Form.Group>

                        <Button type="submit" className="w-100 mt-3" disabled={isSubmitting}>
                            {isSubmitting ? 'Trwa rejestracja...' : 'Zarejestruj się'}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default RegisterPage;
