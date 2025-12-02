import { useEffect, useState } from "react";
import { getAllResidents, createResident } from "./ResidentService.js";

export default function ResidentManager() {
    const [residents, setResidents] = useState([]);
    const [loading, setLoading] = useState(false);

    // pola formularza
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    async function load() {
        setLoading(true);
        try {
            const data = await getAllResidents();
            setResidents(data);
        } finally {
            setLoading(false);
        }
    }

    async function handleAdd(e) {
        e.preventDefault();

        if (!login || !password) {
            alert("Wypełnij oba pola.");
            return;
        }

        const newResident = {
            login: login,
            password: password
        };


        await createResident(newResident);

        setLogin("");
        setPassword("");

        load();
    }

    useEffect(() => {
        load();
    }, []);

    return (
        <div style={{ padding: 20, maxWidth: 400 }}>
            <h2>Dodaj mieszkańca</h2>

            <form onSubmit={handleAdd}>
                <div>
                    <input
                        type="text"
                        placeholder="Login"
                        value={login}
                        onChange={(e) => setLogin(e.target.value)}
                        required
                    />
                </div>

                <div style={{ marginTop: 8 }}>
                    <input
                        type="password"
                        placeholder="Hasło"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" style={{ marginTop: 10 }}>
                     Dodaj
                </button>
            </form>

            <h2 style={{ marginTop: 30 }}>Lista mieszkańców</h2>

            {loading ? (
                <p>Ładowanie...</p>
            ) : (
                <ul>
                    {residents.map(r => (
                        <li key={r.id}>{r.login}</li>
                    ))}
                </ul>
            )}
        </div>
    );
}
