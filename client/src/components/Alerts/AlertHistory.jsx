import React, { useState, useEffect } from "react";

const AlertHistory = () => {
    const [alerts, setAlerts] = useState([]);

    useEffect(() => {
        fetchLogs();
        const interval = setInterval(fetchLogs, 5000);
        return () => clearInterval(interval);
    }, []);

    const fetchLogs = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/alerts/logs");
            if (response.ok) {
                const data = await response.json();
                setAlerts(data.reverse());
            }
        } catch (error) {
            console.error("Błąd pobierania logów:", error);
        }
    };

    const formatDate = (isoString) => {
        if (!isoString) return "-";
        return new Date(isoString).toLocaleString("pl-PL");
    };

    return (
        <div style={styles.container}>
            <div style={styles.headerRow}>
                <h3 style={styles.title}>📜 Dziennik Zdarzeń</h3>
                <span style={styles.count}>Liczba wpisów: {alerts.length}</span>
            </div>

            <div style={styles.tableWrapper}>
                <table style={styles.table}>
                    <thead>
                    <tr style={styles.theadRow}>
                        <th style={{...styles.th, width: "20%"}}>Data</th>
                        <th style={{...styles.th, width: "10%"}}>Poziom</th>
                        <th style={{...styles.th, width: "15%"}}>Źródło</th>
                        <th style={{...styles.th, width: "15%"}}>Lokalizacja</th>
                        <th style={{...styles.th, width: "40%"}}>Wiadomość</th>
                    </tr>
                    </thead>
                    <tbody>
                    {alerts.length === 0 ? (
                        <tr>
                            <td colSpan="5" style={styles.empty}>Brak alertów w systemie.</td>
                        </tr>
                    ) : (
                        alerts.map((alert) => (
                            <tr key={alert.id} style={styles.tr}>
                                <td style={styles.td}>{formatDate(alert.timestamp)}</td>
                                <td style={styles.td}>
                                    <span style={getBadgeStyle(alert.level)}>{alert.level}</span>
                                </td>
                                <td style={styles.td}>{alert.source || "System"}</td>
                                <td style={styles.td}>{alert.location || "-"}</td>
                                <td style={styles.tdMessage}>{alert.message}</td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

const styles = {
    container: {
        padding: "20px",
        backgroundColor: "#121212",
        color: "#e0e0e0",
        borderRadius: "8px",
        border: "1px solid #333",
        marginTop: "20px"
    },
    headerRow: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: "15px",
        borderBottom: "1px solid #333",
        paddingBottom: "10px"
    },
    title: { margin: 0, color: "#fff" },
    count: { color: "#888", fontSize: "0.9rem" },

    tableWrapper: { overflowX: "auto" },
    table: {
        width: "100%",
        borderCollapse: "collapse",
        fontSize: "0.9rem",
        tableLayout: "fixed"
    },
    theadRow: { backgroundColor: "#1e1e1e", color: "#aaa", textTransform: "uppercase", fontSize: "0.8rem" },


    th: { padding: "12px", textAlign: "left", borderBottom: "2px solid #444", wordWrap: "break-word" },
    tr: { borderBottom: "1px solid #333" },
    td: { padding: "12px", color: "#ddd", wordWrap: "break-word", verticalAlign: "top" },
    tdMessage: { padding: "12px", color: "#fff", fontWeight: "500", wordWrap: "break-word", verticalAlign: "top" },

    empty: { padding: "20px", textAlign: "center", color: "#666", fontStyle: "italic" }
};

const getBadgeStyle = (level) => {
    const base = { padding: "3px 8px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "bold", textTransform: "uppercase", display: "inline-block" };
    switch (level) {
        case "CRITICAL": return { ...base, backgroundColor: "#d32f2f", color: "#fff" };
        case "WARNING": return { ...base, backgroundColor: "#f57c00", color: "#fff" };
        case "SOS": return { ...base, backgroundColor: "#b71c1c", color: "#fff", border: "1px solid white" };
        case "INFO": return { ...base, backgroundColor: "#1976d2", color: "#fff" };
        default: return { ...base, backgroundColor: "#616161", color: "#fff" };
    }
};

export default AlertHistory;