import { useState, useEffect } from 'react';
import { useAuth } from "../Administration/AuthContext.jsx";
import './style.css';

const ResidentView = () => {
    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [filterLevel, setFilterLevel] = useState('');
    const [filterBuilding, setFilterBuilding] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    const { user } = useAuth(); // Pobieramy dane o roli użytkownika

    useEffect(() => {
        const fetchAnnouncements = async () => {
            try {
                setLoading(true);
                // Pobieramy ogłoszenia z Twojego API
                const response = await fetch('http://localhost:8080/api/communication/announcements');
                if (!response.ok) {
                    throw new Error('Nie udało się pobrać ogłoszeń.');
                }
                const data = await response.json();

                // Sortowanie: najnowsze na górze
                data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
                setAnnouncements(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAnnouncements();
    }, []);

    // Funkcja do zamykania ogłoszenia przez Admina
    const handleClose = async (id) => {
        if (!window.confirm("Czy na pewno chcesz zamknąć to ogłoszenie?")) return;

        try {
            const response = await fetch(`http://localhost:8080/api/communication/announcements/${id}/close`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                // Po sukcesie w bazie, aktualizujemy stan lokalny (usuwamy z widoku)
                setAnnouncements(prev => prev.filter(ann => ann.id !== id));
            } else {
                alert("Wystąpił błąd podczas zamykania ogłoszenia.");
            }
        } catch (err) {
            console.error("Błąd sieci:", err);
            alert("Błąd połączenia z serwerem.");
        }
    };

    const formatujDate = (isoString) => {
        const date = new Date(isoString);
        return date.toLocaleString('pl-PL', {
            year: 'numeric', month: 'long', day: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    const getLevelClass = (level) => {
        switch (level) {
            case 'SOS':
            case 'CRITICAL':
                return 'level-critical';
            case 'SEVERE':
                return 'level-severe';
            case 'INFO':
            default:
                return 'level-info';
        }
    };

    // Logika filtrowania
    const filteredAnnouncements = announcements.filter(ann => {
        // Nie pokazujemy ogłoszeń, które mają już status CLOSED
        const isStillActive = ann.status !== 'CLOSED';

        const levelMatch = filterLevel ? ann.level === filterLevel : true;
        const buildingMatch = filterBuilding ? (ann.building || '').toLowerCase().includes(filterBuilding.toLowerCase()) : true;
        const searchTermMatch = searchTerm ?
            ann.content.toLowerCase().includes(searchTerm.toLowerCase()) ||
            ann.authorLogin.toLowerCase().includes(searchTerm.toLowerCase()) : true;

        return isStillActive && levelMatch && buildingMatch && searchTermMatch;
    });

    if (loading) {
        return <p className="loading-text">Ładowanie ogłoszeń...</p>;
    }

    if (error) {
        return <p className="error-text">Błąd: {error}</p>;
    }

    return (
        <div className="resident-view">
            <h2 className="view-title">Przeglądaj ogłoszenia</h2>

            <div className="filter-panel">
                <div className="filter-grid">
                    <input
                        type="text"
                        placeholder="Szukaj w treści lub autorze..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="filter-input"
                    />
                    <select
                        value={filterLevel}
                        onChange={(e) => setFilterLevel(e.target.value)}
                        className="filter-select"
                    >
                        <option value="">Wszystkie poziomy</option>
                        <option value="INFO">INFO</option>
                        <option value="SEVERE">SEVERE</option>
                        <option value="CRITICAL">CRITICAL</option>
                        <option value="SOS">SOS</option>
                    </select>
                    <input
                        type="text"
                        placeholder="Filtruj po budynku..."
                        value={filterBuilding}
                        onChange={(e) => setFilterBuilding(e.target.value)}
                        className="filter-input"
                    />
                </div>
            </div>

            {filteredAnnouncements.length === 0 ? (
                <p className="no-results-text">Brak aktywnych ogłoszeń spełniających kryteria.</p>
            ) : (
                <div className="announcements-list">
                    {filteredAnnouncements.map(announcement => (
                        <div key={announcement.id} className={`announcement-banner ${getLevelClass(announcement.level)}`}>
                            <div className="announcement-header">
                                <div className="header-left">
                                    <span className="announcement-level-text">{announcement.level}</span>
                                    <span className="announcement-author">Autor: <strong>{announcement.authorLogin}</strong></span>
                                </div>
                                <div className="header-right">
                                    <span className="announcement-timestamp">{formatujDate(announcement.timestamp)}</span>

                                    {user?.role === 'ADMIN' && (
                                        <button
                                            onClick={() => handleClose(announcement.id)}
                                            className="close-btn"
                                            title="Zamknij i archiwizuj"
                                        >
                                            ✖
                                        </button>
                                    )}
                                </div>
                            </div>

                            <p className="announcement-content">
                                {announcement.content}
                            </p>

                            {announcement.building && (
                                <div className="announcement-footer">
                                    <span>Dotyczy budynku: <strong>{announcement.building}</strong></span>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ResidentView;