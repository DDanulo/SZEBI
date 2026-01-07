import { useState, useEffect } from 'react';
import ConversationList from './ConversationList';
import ChatWindow from './ChatWindow.jsx';
import NewConversationModal from "./NewConversationModal.jsx";

const ConversationsView = ({ currentUserId }) => {
    const [conversations, setConversations] = useState([]);
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

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
    };

    const handleConversationCreated = (newConversation) => {
        fetchConversations();
        setSelectedConversation(newConversation);
        setIsModalOpen(false);
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
                onNewConversation={() => setIsModalOpen(true)}
            />
            <ChatWindow
                conversation={selectedConversation}
                currentUserId={currentUserId}
                onMessageSent={handleMessageSent}
            />
            {isModalOpen && (
                <NewConversationModal
                    currentUserId={currentUserId}
                    onClose={() => setIsModalOpen(false)}
                    onConversationCreated={handleConversationCreated}
                />
            )}
        </div>
    );
};

export default ConversationsView;
