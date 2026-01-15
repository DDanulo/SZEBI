import { useState } from 'react';
import {useAuth} from "../Administration/AuthContext.jsx";



const CreateAnnouncementForm = ({ onAnnouncementCreated }) => {
    const [content, setContent] = useState('');
    const [level, setLevel] = useState('INFO');
    const [building, setBuilding] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    // Trzeba zmienić samemu
    const { user } = useAuth(); // <-- pobieramy user z contextu
    const authorId = user?.userId;



    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (!content || !level) {
            setError('Treść i poziom ogłoszenia są wymagane.');
            return;
        }

        const newAnnouncement = {
            content,
            level,
            building: building || null,
            authorId,
        };

        try {
            const response = await fetch('http://localhost:8080/api/communication/announcements', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newAnnouncement),
            });

            if (!response.ok) {
                throw new Error('Nie udało się utworzyć ogłoszenia.');
            }

            const created = await response.json();
            setSuccess(`Ogłoszenie "${created.id}" zostało pomyślnie utworzone!`);
            setContent('');
            setBuilding('');
            setLevel('INFO');

            if (onAnnouncementCreated) {
                onAnnouncementCreated();
            }

        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="create-announcement-form">
            <h3>Nowe Ogłoszenie</h3>
            {error && <p className="form-error">{error}</p>}
            {success && <p className="form-success">{success}</p>}
            <div className="form-group">
                <label htmlFor="content">Treść</label>
                <textarea
                    id="content"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    rows="4"
                    required
                />
            </div>
            <div className="form-group">
                <label htmlFor="level">Poziom</label>
                <select
                    id="level"
                    value={level}
                    onChange={(e) => setLevel(e.target.value)}
                >
                    <option value="INFO">INFO</option>
                    <option value="SEVERE">SEVERE</option>
                    <option value="CRITICAL">CRITICAL</option>
                    <option value="SOS">SOS</option>
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="building">Budynek (opcjonalnie)</label>
                <input
                    type="text"
                    id="building"
                    value={building}
                    onChange={(e) => setBuilding(e.target.value)}
                    placeholder="Np. A1, B2"
                />
            </div>
            <button type="submit">
                Opublikuj Ogłoszenie
            </button>
        </form>
    );
};

export default CreateAnnouncementForm;
