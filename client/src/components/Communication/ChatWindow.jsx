import { useState, useEffect, useRef } from 'react';

const ChatWindow = ({ conversation, currentUserId }) => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const messagesEndRef = useRef(null);

    useEffect(() => {
        if (conversation) {
            const sortedMessages = [...conversation.messages].sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
            setMessages(sortedMessages);
        } else {
            setMessages([]);
        }
    }, [conversation]);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    const getRecipient = () => {
        if (!conversation) return null;
        return conversation.participants.find(p => p.id !== currentUserId);
    };

    const handleSendMessage = async (e) => {
        e.preventDefault();
        const recipient = getRecipient();
        if (!newMessage.trim() || !recipient) return;

        try {
            const response = await fetch('http://localhost:8080/api/communication/messages', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: newMessage,
                    senderId: currentUserId,
                    recipientId: recipient.id
                })
            });

            if (!response.ok) {
                throw new Error('Błąd wysyłania wiadomości');
            }

            const sentMessage = await response.json();
            setMessages(prevMessages => [...prevMessages, sentMessage]);
            setNewMessage('');

        } catch (error) {
            console.error("Błąd wysyłania wiadomości:", error);
        }
    };

    const recipient = getRecipient();

    if (!conversation || !recipient) {
        return (
            <div className="chat-window placeholder">
                <div>
                    <h3>Wybierz konwersację</h3>
                    <p>Wybierz konwersację z listy po lewej stronie, aby zobaczyć wiadomości.</p>
                </div>
            </div>
        );
    }

    const formatujDate = (isoString) => {
        const date = new Date(isoString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = String(date.getFullYear()).slice(-2);
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${day}.${month}.${year} ${hours}:${minutes}`;
    };

    return (
        <div className="chat-window">
            <div className="chat-header">
                <h3>{recipient.firstName} {recipient.lastName}</h3>
                <p className="participant-contact">{recipient.email}</p>
            </div>
            <div className="messages-area">
                {messages.map(msg => (
                    <div key={msg.id} className={`message-bubble ${msg.senderId === currentUserId ? 'sent' : 'received'}`}>
                        <p>{msg.content}</p>
                        <span className="message-timestamp">{formatujDate(msg.timestamp)}</span>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <form className="message-input-form" onSubmit={handleSendMessage}>
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Napisz wiadomość..."
                    autoComplete="off"
                />
                <button type="submit">Wyślij</button>
            </form>
        </div>
    );
};

export default ChatWindow;
