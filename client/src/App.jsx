import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import { BarChart3 } from 'lucide-react';

// Import komponentów
import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";
import DataAnalysisMenu from './components/DataAnalysis/DataAnalysisMenu.jsx';

// Komponent z główną zawartością strony (Strona Główna)
const MainContent = () => {
    const navigate = useNavigate();

    return (
        <>
            <AlertHistory />

            {/* --- Moduł sterowania --- */}
            <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
            <div style={{ textAlign: 'left' }}>
                <ScheduleManager />
                <ResidentManager />
            </div>



            {/* --- MODUŁ ANALIZY I RAPORTOWANIA --- */}
            <hr style={{ margin: '40px 0', border: '2px solid #666' }} />

            <div className="bg-white p-8 rounded-xl shadow-md border-l-8 border-blue-600 my-8 text-left flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
                        <BarChart3 className="text-blue-600" /> Moduł Analizy Danych
                    </h2>
                    <p className="text-gray-600 mt-2">
                        Przejdź do zaawansowanych statystyk, dashboardu czasu rzeczywistego i generatora raportów PDF.
                    </p>
                </div>
                <button
                    onClick={() => navigate('/analysis')}
                    className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg shadow transition transform hover:scale-105"
                >
                    Otwórz Analizę
                </button>
            </div>



            {/* --- MODUŁ PROGNOZOWANIA --- */}
            <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
            <div style={{ textAlign: 'left' }}>
                <PredictionViewer />
            </div>
        </>
    );
};

function App() {
    return (
        <Router>
            <Routes>
                {/* Główna strona ze wszystkimi modułami (oprócz pełnej analizy) */}
                <Route path="/" element={<MainContent />} />

                {/* Menu wyboru dla Analizy Danych */}
                <Route path="/analysis" element={<DataAnalysisMenu />} />
            </Routes>
        </Router>
    )
}

export default App;