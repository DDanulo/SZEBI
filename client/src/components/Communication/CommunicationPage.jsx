import { useState } from 'react';
import { Link } from 'react-router-dom';
import ResidentView from "./ResidentView.jsx";
import AdminView from "./AdminView.jsx";
import ConversationsView from "./ConversationsView.jsx";
import {useAuth} from "../Administration/AuthContext.jsx";

const CommunicationPage = () => {
    const [view, setView] = useState('resident');
    const [residentViewKey, setResidentViewKey] = useState(0);

    const handleAnnouncementCreated = () => {
        setResidentViewKey(prevKey => prevKey + 1);
        setView('resident');
    };

    // Trzeba zmienić samemu
    const { user } = useAuth(); // <-- pobieramy user z contextu
    const currentUserId = user?.userId;

    return (
        <div className="communication-page">
            <Link to="/" className="back-link">
                &larr; Powrót do strony głównej
            </Link>
            <h1 className="page-title">Moduł Komunikacji</h1>
            <div className="view-switcher">
                <button
                    onClick={() => setView('resident')}
                    className={`switch-button ${view === 'resident' ? 'active' : ''}`}
                >
                    Ogłoszenia
                </button>
                <button
                    onClick={() => setView('messages')}
                    className={`switch-button ${view === 'messages' ? 'active' : ''}`}
                >
                    Wiadomości
                </button>
                {user?.role === 'ADMIN' && (
                    <button
                        onClick={() => setView('admin')}
                        className={`switch-button ${view === 'admin' ? 'active' : ''}`}
                    >
                        Panel Admina
                    </button>
                )}
            </div>

            <hr className="divider" />

            {view === 'resident' && <ResidentView key={residentViewKey} />}
            {view === 'admin' && <AdminView onAnnouncementCreated={handleAnnouncementCreated} />}
            {view === 'messages' && <ConversationsView currentUserId={currentUserId} />}
        </div>
    );
};

export default CommunicationPage;
