const ConversationList = ({ conversations, currentUserId, onSelectConversation, selectedConversationId, onNewConversation }) => {

    const getParticipant = (participants) => {
        return participants.find(p => p.id !== currentUserId);
    };

    return (
        <div className="conversation-list">
            <div className="list-header-container">
                <h3 className="list-header">Twoje konwersacje</h3>
                <button className="new-conversation-btn" onClick={onNewConversation}>+</button>
            </div>
            {conversations.length > 0 ? (
                <ul>
                    {conversations.map(conv => {
                        const participant = getParticipant(conv.participants);
                        if (!participant) return null;

                        const initial = (participant.firstName?.[0] || '').toUpperCase();

                        return (
                            <li
                                key={conv.id}
                                className={`conversation-item ${selectedConversationId === conv.id ? 'active' : ''}`}
                                onClick={() => onSelectConversation(conv)}
                            >
                                <div className="participant-avatar">{initial}</div>
                                <div className="participant-info">
                                    <div className="participant-name">
                                        {participant.firstName} {participant.lastName}
                                    </div>
                                    {participant.room && (
                                        <div className="participant-details">
                                            Budynek: {participant.room}
                                        </div>
                                    )}
                                </div>
                            </li>
                        );
                    })}
                </ul>
            ) : (
                <p className="no-results-text">Brak konwersacji.</p>
            )}
        </div>
    );
};

export default ConversationList;
