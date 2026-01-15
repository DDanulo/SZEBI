import {BrowserRouter as Router, Routes, Route, Link} from 'react-router-dom';
import './App.css';

import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import Simulation from "./components/Simulation/SimulationManagement.jsx";
import Communication from "./components/Communication/CommunicationPage.jsx";
import AdminUsersPage from "./components/Administration/AdminUsersPage.jsx";
import LoginPage from "./components/Administration/LoginPage.jsx";
import RegisterPage from "./components/Administration/RegisterPage.jsx";
import ControlDevicePage from './components/DeviceControl/ControlDevicePage.jsx';


import { useAuth } from "./components/Administration/AuthContext.jsx";

function App() {

    const { user, logout } = useAuth();
    return (
        <Router>
<<<<<<< Updated upstream
            <div className="app-container">
=======
            <Routes>
                {/* Wpisz se na stronce na koncu /control na razie pozniej bedzie ladny przycisk */}
                <Route path="/control" element={<ControlDevicePage />} />
>>>>>>> Stashed changes

                <header style={styles.header}>
                    <nav style={styles.nav}>
                        <Link to="/simulation" style={styles.button}>Simulation</Link>
                        <Link to="/alerts" style={styles.button}>Alerts</Link>
                        <Link to="/schedule" style={styles.button}>Control</Link>
                        <Link to="/residents" style={styles.button}>Administration</Link>
                        <Link to="/reports" style={styles.button}>Reports</Link>
                        <Link to="/predictions" style={styles.button}>Predictions</Link>
                        <Link to="/communication" style={styles.button}>Communication</Link>
                        <Link to="/control" style={styles.button}>DeviceControl</Link>
                        {user ? (
                            <Link
                                to="/"
                                onClick={(e) => {
                                    if (!window.confirm('Czy na pewno chcesz się wylogować?')) {
                                        e.preventDefault();
                                        return;
                                    }
                                    logout();

                                }}
                                style={styles.button}
                            >
                                Wyloguj
                            </Link>
                        ) : (
                            <Link to="/login" style={styles.button}>
                                Zaloguj się
                            </Link>
                        )}
                    </nav>
                </header>

                <main style={{ padding: '20px', textAlign: 'left' }}>
                    <Routes>
                        <Route path="/simulation" element={<Simulation />} />
                        <Route path="/alerts" element={<AlertHistory />} />
                        <Route path="/schedule" element={<ScheduleManager />} />
                        <Route path="/residents" element={<AdminUsersPage />} />
                        <Route path="/reports" element={<ReportModule />} />
                        <Route path="/predictions" element={<PredictionViewer />} />
                        <Route path="/communication" element={<Communication />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/control" element={<ControlDevicePage />} />
                    </Routes>
                </main>

            </div>
        </Router>
    );
}

const styles = {

    header: {
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '70px',
        backgroundColor: '#333',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
        boxShadow: '0 2px 5px rgba(0,0,0,0.5)'
    },
    nav: {
        display: 'flex',
        justifyContent: 'space-around',
        flexWrap: 'wrap',
        gap: '10px'
    },
    button: {
        textDecoration: 'none',
        color: 'white',
        backgroundColor: '#555',
        padding: '10px 20px',
        borderRadius: '5px',
        fontWeight: 'bold',
        fontSize: '14px',
        transition: 'background 0.3s',
    },
    mainContent: {
        marginTop: '70px',
        padding: '40px',
        textAlign: 'left'
    }
};

export default App
