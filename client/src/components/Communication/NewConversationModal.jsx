import { useState, useEffect } from 'react';
import api from "../Administration/api.js";

const NewConversationModal = ({ currentUserId, onClose, onConversationCreated }) => {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setLoading(true);
                const response = await (await api.get(`users/residents`)).data;
                // if (!response.ok) {
                //     throw new Error('Nie udało się pobrać użytkowników.');
                // }
                // const data = await response.json();
                const filteredData = response.filter(user => user.id !== currentUserId);
                setUsers(filteredData);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchUsers();
    }, [currentUserId]);

    const handleUserSelect = async (userId) => {
        try {
            const response = await fetch('http://localhost:8080/api/communication/conversations', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ user1Id: currentUserId, user2Id: userId }),
            });
            if (!response.ok) {
                throw new Error('Nie udało się utworzyć konwersacji.');
            }
            const newConversation = await response.json();
            onConversationCreated(newConversation);
        } catch (err) {
            setError(err.message);
        }
    };

    const filteredUsers = users.filter(user =>
        `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h3>Rozpocznij nową konwersację</h3>
                    <button onClick={onClose} className="close-button">&times;</button>
                </div>
                <div className="modal-body">
                    <input
                        type="text"
                        placeholder="Szukaj użytkownika..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="modal-search-input"
                    />
                    {loading && <p>Ładowanie...</p>}
                    {error && <p className="error-text">{error}</p>}
                    <ul className="user-list">
                        {filteredUsers.map(user => (
                            <li key={user.id} onClick={() => handleUserSelect(user.id)}>
                                <div className="participant-avatar">{(user.firstName?.[0] || '').toUpperCase()}</div>
                                <div className="participant-info">
                                    <div className="participant-name">
                                        {user.firstName} {user.lastName}
                                    </div>
                                    {user.room && (
                                        <div className="participant-details">
                                            Pokój: {user.room}
                                        </div>
                                    )}
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default NewConversationModal;
