import './App.css'

import AlertHistory from "./components/Alerts/AlertHistory.jsx";
import ScheduleManager from './components/DeviceControl/ScheduleManager.jsx';
import ReportModule from './components/DataAnalysis/ReportModule.jsx';
import PredictionViewer from "./components/DataPrediction/PredictionViewer.jsx";
import ResidentManager from "./components/Administration/ResidentManager.jsx";


function App() {

  return (
    <>
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
    </>
  )
}

export default App
