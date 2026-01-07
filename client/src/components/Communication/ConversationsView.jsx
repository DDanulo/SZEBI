import { useState, useEffect } from 'react';
import ConversationList from './ConversationList';
import ChatWindow from './ChatWindow.jsx';

const ConversationsView = ({ currentUserId }) => {
    const [conversations, setConversations] = useState([]);
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchConversations = async () => {
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8080/api/communication/conversations?userId=${currentUserId}`);
            if (!response.ok) {
                throw new Error('Nie udało się pobrać konwersacji.');
            }
            const data = await response.json();
            setConversations(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchConversations();
    }, [currentUserId]);

    const handleMessageSent = () => {
        fetchConversations();
        if (selectedConversation) {
            const newSelected = { ...selectedConversation };
            setSelectedConversation(null);
            setTimeout(() => setSelectedConversation(newSelected), 0);
        }
    };

    if (loading) return <p className="loading-text">Ładowanie konwersacji...</p>;
    if (error) return <p className="error-text">Błąd: {error}</p>;

    return (
        <div className="conversations-view">
            <ConversationList
                conversations={conversations}
                currentUserId={currentUserId}
                onSelectConversation={setSelectedConversation}
                selectedConversationId={selectedConversation?.id}
            />
            <ChatWindow
                conversation={selectedConversation}
                currentUserId={currentUserId}
                onMessageSent={handleMessageSent}
            />
        </div>
    );
};

export default ConversationsView;
