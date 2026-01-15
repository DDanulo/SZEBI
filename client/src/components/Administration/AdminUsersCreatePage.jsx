import React, { useState } from "react";
import { Container, Form, Button, Alert, Spinner, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import api from "./api";

const AdminUsersCreatePage = () => {
    const navigate = useNavigate();

    const [role, setRole] = useState("resident");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [room, setRoom] = useState("");

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(null);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        let url = "";
        let body = { login, password, firstName, lastName, email };

        if (role === "resident") {
            url = "/users/residents";
            body.room = room;
        } else if (role === "engineer") {
            url = "/users/engineers";
        } else if (role === "administrator") {
            url = "/users/administrators";
        }

        try {
            const response = await api.post(url, body);
            setSuccess(`Użytkownik ${response.data.login} został utworzony!`);

        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Nie udało się utworzyć użytkownika.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="mt-4">
            <h1>Tworzenie nowego użytkownika</h1>
            <p>Wybierz rolę i wypełnij dane użytkownika.</p>

            {success && <Alert variant="success">{success}</Alert>}
            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                    <Form.Label>Rola</Form.Label>
                    <Form.Select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="resident">Resident</option>
                        <option value="engineer">Engineer</option>
                        <option value="administrator">Administrator</option>
                    </Form.Select>
                </Form.Group>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Login</Form.Label>
                            <Form.Control
                                type="text"
                                value={login}
                                onChange={(e) => setLogin(e.target.value)}
                                required
                            />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Imię</Form.Label>
                            <Form.Control
                                type="text"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                            />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nazwisko</Form.Label>
                            <Form.Control
                                type="text"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </Form.Group>

                {role === "resident" && (
                    <Form.Group className="mb-3">
                        <Form.Label>Pokój</Form.Label>
                        <Form.Control
                            type="text"
                            value={room}
                            onChange={(e) => setRoom(e.target.value)}
                        />
                    </Form.Group>
                )}

                <div className="d-flex justify-content-between">
                    <Button variant="secondary" onClick={() => navigate("/users")}>
                        Powrót
                    </Button>
                    <Button type="submit" variant="primary" disabled={loading}>
                        {loading ? <Spinner animation="border" size="sm" /> : "Utwórz użytkownika"}
                    </Button>
                </div>
            </Form>
        </Container>
    );
};

export default AdminUsersCreatePage;
