import React, { useState } from "react";
import { sendSosAlert } from "./SosService";
import { useAuth } from "../Administration/AuthContext";

const SosButton = () => {
    const { user } = useAuth();
    const [message, setMessage] = useState("");
    const [location, setLocation] = useState("Brak Lokalizacji");
    const [status, setStatus] = useState(null);

    const handleSend = async () => {
        const finalMessage = message.trim() || "ALARM SOS";

        const report = {
            userID: user?.id || "00000000-0000-0000-0000-000000000000",
            location: location,
            message: finalMessage
        };

        try {
            await sendSosAlert(report);
            setStatus("success");
            setMessage("");

            setTimeout(() => setStatus(null), 3000);
        } catch (error) {
            console.error(error);
            setStatus("error");
            setTimeout(() => setStatus(null), 3000);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.headerRow}>
                <h3 style={styles.header}>PANEL AWARYJNY SOS</h3>
                {user && <span style={styles.userInfo}>Zglasza: {user.name}</span>}
            </div>

            <div style={{marginBottom: "10px"}}>
                <label style={styles.label}>Lokalizacja:</label>
                <input
                    style={styles.input}
                    type="text"
                    value={location}
                    onChange={(e) => setLocation(e.target.value)}
                    placeholder="Wpisz lokalizacje..."
                />
            </div>

            <textarea
                style={styles.textarea}
                placeholder="Opisz zagrozenie..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                rows={3}
            />

            <button
                onClick={handleSend}
                style={status === "success" ? styles.buttonSuccess : status === "error" ? styles.buttonError : styles.button}
                disabled={status === "success"}
            >
                {status === "success" ? "WYSLANO ZGLOSZENIE" : status === "error" ? "BLAD WYSYLANIA" : "WYSLIJ SOS"}
            </button>
        </div>
    );
};

const styles = {
    container: {
        backgroundColor: "#b71c1c",
        padding: "20px",
        borderRadius: "8px",
        color: "white",
        marginBottom: "20px",
        boxShadow: "0 4px 10px rgba(0,0,0,0.5)"
    },
    headerRow: {
        display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "10px"
    },
    header: { margin: 0, color: "#fff" },
    userInfo: { fontSize: "0.85rem", opacity: 0.8 },

    label: { fontSize: "0.8rem", textTransform: "uppercase", fontWeight: "bold", display: "block", marginBottom: "5px" },

    input: {
        width: "100%",
        padding: "8px",
        borderRadius: "4px",
        border: "none",
        backgroundColor: "#ffebee",
        color: "#b71c1c",
        fontWeight: "bold",
        marginBottom: "10px"
    },
    textarea: {
        width: "100%",
        padding: "10px",
        borderRadius: "4px",
        border: "none",
        backgroundColor: "#ffebee",
        color: "#b71c1c",
        fontWeight: "bold",
        marginBottom: "10px",
        resize: "vertical",
        minHeight: "60px"
    },
    button: {
        width: "100%",
        padding: "12px",
        backgroundColor: "#fff",
        color: "#b71c1c",
        fontWeight: "900",
        border: "none",
        borderRadius: "4px",
        cursor: "pointer",
        fontSize: "1.1rem",
        textTransform: "uppercase"
    },
    buttonSuccess: {
        width: "100%", padding: "12px", backgroundColor: "#2e7d32", color: "white", fontWeight: "bold", border: "none", borderRadius: "4px"
    },
    buttonError: {
        width: "100%", padding: "12px", backgroundColor: "#000", color: "white", fontWeight: "bold", border: "none", borderRadius: "4px"
    }
};

export default SosButton;