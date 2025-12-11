import { useState,  useEffect } from "react";
import { loginUser } from "./LoginService.js";
import { registerUser } from "./RegisterService.js";
import { getAllDevices } from "../DeviceControl/ScheduleService.js";

export default function ResidentManager() {
    const [mode, setMode] = useState(null);
    const [loggedIn, setLoggedIn] = useState(false);
    const [devices, setDevices] = useState([]);

    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [room, setRoom] = useState("");

    const [message, setMessage] = useState("");

    const handleLogin = async () => {
        try {
            const loginForm = {
                login: login,
                password: password,

            };
            await loginUser(loginForm);
            setLoggedIn(true);
            setMessage("");
        } catch (err) {
            setMessage("Logowanie nie powiodło się.",err);
        }
    };

    const handleRegister = async () => {
        try {
            const newResident = {
                login: login,
                password: password,
                firstName: firstName,
                lastName: lastName,
                email: email,
                room: room
            };
            await registerUser(newResident);
            setMessage("Rejestracja udana.");
        } catch (err) {
            setMessage("Rejestracja nie powiodła się.",err);
        }
    };

    const handleLogout = () => {
        setLoggedIn(false);
        setMode(null);
        setLogin("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setEmail("");
        setRoom("");
        setMessage("");
    };


    useEffect(() => {
        if (loggedIn) {
            getAllDevices()
                .then(res => setDevices(res.data))
                .catch(err => console.error("Błąd pobierania urządzeń:", err));
        }
    }, [loggedIn]);

    if (loggedIn) {
        return (
            <div>
                <h2>Panel użytkownika</h2>
                <button onClick={() => console.log("Dodaj urządzenie")}>Dodaj urządzenie</button>
                <button onClick={() => console.log("Usuń urządzenie")}>Usuń urządzenie</button>
                <button onClick={handleLogout}>Wyloguj</button>

                <h3>Lista urządzeń:</h3>
                {devices.length === 0 ? (
                    <p>Brak urządzeń</p>
                ) : (
                    <ul>
                        {devices.map(device => (
                            <li key={device.id}>{device.name}</li>
                        ))}
                    </ul>
                )}
            </div>
        );
    }

    return (
        <div>
            <div>
                <button onClick={() => setMode("login")}>Zaloguj</button>
                <button onClick={() => setMode("register")}>Zarejestruj</button>
            </div>

            {mode === "login" && (
                <div>
                    <h2>Logowanie</h2>
                    <input type="text" placeholder="Login" value={login} onChange={(e) => setLogin(e.target.value)} />
                    <input type="password" placeholder="Hasło" value={password} onChange={(e) => setPassword(e.target.value)} />
                    <button onClick={handleLogin}>Zaloguj</button>
                </div>
            )}

            {mode === "register" && (
                <div>
                    <h2>Rejestracja</h2>
                    <input type="text" placeholder="Login" value={login} onChange={(e) => setLogin(e.target.value)} />
                    <input type="password" placeholder="Hasło" value={password} onChange={(e) => setPassword(e.target.value)} />
                    <input type="text" placeholder="Imię" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                    <input type="text" placeholder="Nazwisko" value={lastName} onChange={(e) => setLastName(e.target.value)} />
                    <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                    <input type="text" placeholder="Pokój" value={room} onChange={(e) => setRoom(e.target.value)} />
                    <button onClick={handleRegister}>Zarejestruj</button>
                </div>
            )}

            {message && <p>{message}</p>}
        </div>
    );
}
