import React, { useState } from "react";
import { sendSos } from "./SosService";

const SosButton = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [status, setStatus] = useState(null);

    const [message, setMessage] = useState("");
    const [location, setLocation] = useState("");

    const MOCK_USER_ID = "123e4567-e89b-12d3-a456-426614174000";

    const handleSend = async () => {
        setIsLoading(true);
        setStatus(null);
        try {
            await sendSos(MOCK_USER_ID, message, location);
            setStatus("success");
            setTimeout(() => {
                setIsOpen(false);
                setStatus(null);
                setMessage("");
                setLocation("");
            }, 2000);
        } catch (error) {
            console.error(error);
            setStatus("error");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>

            <button
                onClick={() => setIsOpen(true)}
                style={styles.sosButton}
            >
                ZGŁOŚ AWARIĘ (SOS)
            </button>


            {isOpen && (
                <div style={styles.overlay}>
                    <div style={styles.modal}>
                        <div style={styles.header}>
                            <h2 style={{ margin: 0, color: "#d32f2f" }}>WEZWANIE POMOCY</h2>
                            <button onClick={() => setIsOpen(false)} style={styles.closeX}>✕</button>
                        </div>

                        <p style={{ fontSize: "14px", color: "#ccc" }}>
                            Opisz sytuację.
                        </p>

                        <div style={styles.field}>
                            <label style={styles.label}>Treść zgłoszenia:</label>
                            <input
                                type="text"
                                value={message}
                                onChange={(e) => setMessage(e.target.value)}
                                style={styles.input}
                            />
                        </div>

                        <div style={styles.field}>
                            <label style={styles.label}>Lokalizacja:</label>
                            <input
                                type="text"
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                                style={styles.input}
                            />
                        </div>

                        {status === "success" && <div style={{ color: "#388e3c", fontWeight: "bold", marginBottom: "10px" }}>Wysłano!</div>}
                        {status === "error" && <div style={{ color: "#d32f2f", fontWeight: "bold", marginBottom: "10px" }}>Błąd wysyłania!</div>}

                        <div style={styles.footer}>
                            <button onClick={() => setIsOpen(false)} style={styles.btnCancel} disabled={isLoading}>Anuluj</button>
                            <button onClick={handleSend} style={styles.btnConfirm} disabled={isLoading}>
                                {isLoading ? "..." : "WYŚLIJ"}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
};

const styles = {
    sosButton: {
        backgroundColor: "#d32f2f",
        color: "white",
        border: "none",
        borderRadius: "4px",
        padding: "15px 30px",
        fontWeight: "bold",
        fontSize: "16px",
        cursor: "pointer",
        textTransform: "uppercase",
        letterSpacing: "1px",
    },
    overlay: {
        position: "fixed", top: 0, left: 0, width: "100vw", height: "100vh",
        backgroundColor: "rgba(0,0,0,0.8)", // Ciemniejsze tło
        display: "flex", alignItems: "center", justifyContent: "center", zIndex: 10000,
    },
    modal: {
        backgroundColor: "#1e1e1e",
        color: "#ffffff",
        padding: "25px",
        border: "1px solid #333",
        width: "400px",
        fontFamily: "Arial, sans-serif",
    },
    header: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px", borderBottom: "1px solid #333", paddingBottom: "10px" },
    closeX: { background: "none", border: "none", fontSize: "24px", cursor: "pointer", color: "#fff" },
    field: { marginBottom: "15px", textAlign: "left" },
    label: { display: "block", marginBottom: "5px", fontWeight: "bold", fontSize: "13px", color: "#ccc" },
    input: {
        width: "100%",
        padding: "10px",
        backgroundColor: "#121212",
        color: "#fff",
        border: "1px solid #444",
        fontSize: "14px",
        boxSizing: "border-box"
    },
    footer: { display: "flex", justifyContent: "flex-end", gap: "10px", marginTop: "25px" },
    btnCancel: { padding: "10px 20px", border: "1px solid #444", backgroundColor: "transparent", color: "#fff", cursor: "pointer" },
    btnConfirm: { padding: "10px 20px", border: "none", backgroundColor: "#d32f2f", color: "white", cursor: "pointer", fontWeight: "bold" }
};

export default SosButton;