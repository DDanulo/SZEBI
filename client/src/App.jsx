import './App.css'

import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";
import {Link} from "react-router-dom";


function App() {

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
        <div style={{ textAlign: 'left' }}>
            <ReportModule />
        </div>


        {/* --- MODUŁ PROGNOZOWANIA --- */}
        <hr style={{ margin: '40px 0', border: '2px solid #666' }} />
        <div style={{ textAlign: 'left' }}>
            <PredictionViewer />
        </div>

        {/* --- MODUŁ KOMUNIKACJI --- */}
        <div>
            <h2>Moduł Komunikacji</h2>
            <p>Przejdź do tablicy ogłoszeń i centrum wiadomości.</p>
            <Link to="/communication">
                Przejdź do Komunikacji
            </Link>
        </div>
    </>
  )
}

export default App
