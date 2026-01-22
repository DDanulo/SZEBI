import {BrowserRouter as Router, Routes, Route, Link, Navigate} from 'react-router-dom';
import './App.css';


import AlertsModule from "./components/Alerts/AlertsModule.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import Simulation from "./components/Simulation/SimulationManagement.jsx";
import Communication from "./components/Communication/CommunicationPage.jsx";
import AdminUsersPage from "./components/Administration/AdminUsersPage.jsx";
import LoginPage from "./components/Administration/LoginPage.jsx";
import RegisterPage from "./components/Administration/RegisterPage.jsx";
import ControlDevicePage from './components/DeviceControl/ControlDevicePage.jsx';
import RequireRole from "./components/Administration/RequireRole.jsx";

import {useAuth} from "./components/Administration/AuthContext.jsx";
import AdminUsersEditPage from "./components/Administration/AdminUsersEditPage.jsx";
import AdminUsersCreatePage from "./components/Administration/AdminUsersCreatePage.jsx";
import ForgotPasswordPage from "./components/Administration/ForgotPasswordPage.jsx";
import ResetPasswordPage from "./components/Administration/ResetPasswordPage.jsx";

function App() {

    const {user, logout} = useAuth();
    return (
        <Router>
            <div className="app-container">

                <header style={styles.header}>
                    <nav style={styles.nav}>

                        {user ? (

                            <>
            <span style={{color: 'white', margin: '0 10px', fontWeight: 'bold'}}>
                Witaj, {user.login}!
            </span>
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
                                {user?.role === 'ADMIN' && (
                                    <Link to="/simulation" style={styles.button}>Simulation</Link>
                                )}
                                <Link to="/alerts" style={styles.button}>Alerts</Link>
                                <Link to="/" style={styles.button}>Control</Link>
                                {user?.role === 'ADMIN' && (
                                    <Link to="/users" style={styles.button}>Administration</Link>
                                )}
                                {(user?.role === 'ADMIN' || user?.role === "ENGINEER") && (
                                    <Link to="/reports" style={styles.button}>Reports</Link>
                                )}
                                {(user?.role === 'ADMIN' || user?.role === "ENGINEER") && (
                                    <Link to="/predictions" style={styles.button}>Predictions</Link>
                                )}

                                <Link to="/communication" style={styles.button}>Communication</Link>
                            </>
                        ) : (
                            <Link to="/" style={styles.button}>
                                Zaloguj się
                            </Link>
                        )}
                    </nav>
                </header>

                <main style={{padding: '20px', textAlign: 'left'}}>
                    <Routes>
                        <Route
                            path="/"
                            element={user ? <ScheduleManager/> : <Navigate to="/login" replace/>}
                        />
                        <Route path="/simulation" element={<Simulation/>}/>
                        <Route path="/alerts" element={<AlertsModule/>}/>
                        {/*<Route path="/" element={<ScheduleManager/>}/>*/}
                        <Route path="/users" element={<AdminUsersPage/>}/>
                        <Route path="/users/edit/:id" element={<AdminUsersEditPage/>}/>
                        <Route path="/users/create" element={<AdminUsersCreatePage/>}/>
                        <Route path="/reports" element={<ReportModule/>}/>
                        <Route path="/predictions" element={<PredictionViewer/>}/>
                        <Route path="/communication" element={<Communication/>}/>
                        <Route path="/login" element={<LoginPage/>}/>
                        <Route path="/register" element={<RegisterPage/>}/>
                        <Route path="/forgot-password" element={<ForgotPasswordPage/>}/>
                        <Route path="/reset-password" element={<ResetPasswordPage/>}/>
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