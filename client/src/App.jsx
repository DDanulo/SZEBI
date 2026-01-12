import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';

import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";
import {Link} from "react-router-dom";

const Simulation = () => <div style={{ padding: '20px' }}><h2>Modul symulacji</h2><p>Tutaj moduł symulacji</p></div>;
const Communication = () => <div style={{ padding: '20px' }}><h2>Komunikacja</h2><p>Tutaj moduł komunikacji</p></div>;

function App() {
    return (
        <Router>
            <div className="app-container">

                <header style={styles.header}>
                    <nav style={styles.nav}>
                        <Link to="/simulation" style={styles.button}>Simulation</Link>
                        <Link to="/alerts" style={styles.button}>Alerts</Link>
                        <Link to="/schedule" style={styles.button}>Control</Link>
                        <Link to="/residents" style={styles.button}>Administration</Link>
                        <Link to="/reports" style={styles.button}>Reports</Link>
                        <Link to="/predictions" style={styles.button}>Predictions</Link>
                        <Link to="/communication" style={styles.button}>Communication</Link>
                    </nav>
                </header>

                <main style={{ padding: '20px', textAlign: 'left' }}>
                    <Routes>
                        <Route path="/simulation" element={<Simulation />} />
                        <Route path="/alerts" element={<AlertHistory />} />
                        <Route path="/schedule" element={<ScheduleManager />} />
                        <Route path="/residents" element={<ResidentManager />} />
                        <Route path="/reports" element={<ReportModule />} />
                        <Route path="/predictions" element={<PredictionViewer />} />
                        <Route path="/communication" element={<Communication />} />
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

export default App;