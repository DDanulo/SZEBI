import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import ScheduleManager from './components/devicecontrol/ScheduleManager'
import ResidentManager from "./components/Administration/ResidentManager.jsx"
import LoginPage from "./components/Administration/LoginPage.jsx"
import RegisterPage from "./components/Administration/RegisterPage.jsx"

// Importujemy potrzebne komponenty z react-router-dom
import { Routes, Route } from 'react-router-dom'
import AdminUsersPage from "./components/Administration/AdminUsersPage.jsx";

function App() {
    const [count, setCount] = useState(0)

    return (
        <>
            {/* Te elementy widać zawsze – na każdej podstronie */}
            <div>
                <a href="https://vite.dev" target="_blank">
                    <img src={viteLogo} className="logo" alt="Vite logo" />
                </a>
                <a href="https://react.dev" target="_blank">
                    <img src={reactLogo} className="logo react" alt="React logo" />
                </a>
            </div>

            <h1>Vite + React</h1>

            <div className="card">
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.jsx</code> and save to test HMR
                </p>
            </div>

            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>

            <hr style={{ margin: '40px 0', border: '2px solid #666' }} />

            {/* Komponenty zawsze widoczne */}
            <div style={{ textAlign: 'left' }}>
                <ScheduleManager />
                <ResidentManager />
            </div>

            {/* Tutaj zaczyna się routing – tylko te elementy się zmieniają */}
            <Routes>
                {/* Strona główna – możesz dodać własny komponent */}
                <Route path="/" element={<div style={{ marginTop: '2rem' }}>
                    <h2>Strona główna / Dashboard</h2>
                    <p>Witaj w aplikacji! Wybierz opcję z menu lub przejdź do /login</p>
                </div>} />

                {/* Trasy publiczne */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/auth" element={<AdminUsersPage />} />
                {/* Opcjonalnie – strona 404 */}
                <Route path="*" element={<div>404 – Nie znaleziono strony</div>} />
            </Routes>
        </>
    )
}

export default App