import './App.css'
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";

function App() {

  return (
    <>
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
    </>
  )
}

export default App
