import React, { useState } from "react";
import { Container, Form, Button, Alert, Spinner } from "react-bootstrap";
import api from "./api.js";

const ForgotPasswordPage = () => {
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(null);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        try {
            console.log(email);
            await api.post("/users/forgot-password", { email });
            console.log(email);
            setSuccess(
                "Jeżeli konto istnieje, wysłaliśmy link do zmiany hasła na podany email."
            );
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "Nie udało się wysłać maila resetującego hasło."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: "400px" }}>
            <h2 className="mb-3 text-center">Zapomniałem hasła</h2>

            {success && <Alert variant="success">{success}</Alert>}
            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="podaj email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </Form.Group>

                <Button type="submit" className="w-100" disabled={loading}>
                    {loading ? (
                        <Spinner animation="border" size="sm" />
                    ) : (
                        "Wyślij link resetujący"
                    )}
                </Button>
            </Form>
        </Container>
    );
};

export default ForgotPasswordPage;
