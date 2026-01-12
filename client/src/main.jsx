import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { BrowserRouter } from 'react-router-dom'
import {AuthProvider} from "./components/Administration/AuthContext.jsx";   // ← dodaj to

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <AuthProvider>
        <BrowserRouter>          // ← tutaj owijamy całą aplikację
            <App />
        </BrowserRouter>
        </AuthProvider>
    </React.StrictMode>,
)