import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";
import ControlDevicePage from './components/DeviceControl/ControlDevicePage.jsx';

function App() {

    return (
        <Router>
            <Routes>
                {/* Wpisz se /control na razie pozniej bedzie ladny przycisk */}
                <Route path="/control" element={<ControlDevicePage />} />


                <Route path="/" element={
                    <>
                        <AlertHistory />


                        <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
                        <div style={{ textAlign: 'left' }}>

                            <ResidentManager />
                        </div>

                        {/* --- MODUŁ ANALIZY I RAPORTOWANIA --- */}
                        <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
                        <div style={{ textAlign: 'left' }}>
                            <ReportModule />
                        </div>

                        {/* --- MODUŁ PROGNOZOWANIA --- */}
                        <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
                        <div style={{ textAlign: 'left' }}>
                            <PredictionViewer />
                        </div>
                    </>
                } />
            </Routes>
        </Router>
    )
}

export default App