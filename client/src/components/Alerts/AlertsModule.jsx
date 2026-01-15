import React from "react";
import SosButton from "./SosButton";
import RulesManager from "./RulesManager";
import AlertHistory from "./AlertHistory";

const AlertsModule = () => {
    return (
        <div style={styles.container}>
            <h2 style={styles.header}></h2>

            <div style={styles.section}>
                <SosButton />
            </div>

            <div style={styles.grid}>
                <div style={styles.column}>
                    <RulesManager />
                </div>


                <div style={styles.column}>
                    <AlertHistory />
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: {
        width: "100%",
        maxWidth: "1200px",
        margin: "0 auto",
        padding: "20px",
        fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif"
    },
    header: {
        textAlign: "center",
        marginBottom: "30px",
        color: "#fff",
        borderBottom: "1px solid #333",
        paddingBottom: "10px"
    },
    section: {
        marginBottom: "30px"
    },
    grid: {
        display: "flex",
        flexDirection: "column",
        gap: "20px"
    },
    column: {
        flex: 1
    }
};

export default AlertsModule;