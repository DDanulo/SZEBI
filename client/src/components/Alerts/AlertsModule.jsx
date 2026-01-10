import React from 'react';
import AlertHistory from "./AlertHistory";
import RulesManager from "./RulesManager";
import SosButton from "./SosButton";

const AlertsModule = () => {
    return (
        <div style={styles.container}>

            <div style={styles.headerContainer}>
                <div style={styles.titleWrapper}>
                    <h1 style={styles.headerTitle}>Moduł Alarmowy</h1>
                </div>

                <div style={styles.sosWrapper}>
                    <SosButton />
                </div>
            </div>

            <hr style={styles.divider} />

            <div style={styles.contentWrapper}>
                <section style={styles.section}>
                    <h2 style={styles.sectionTitle}>Konfiguracja Reguł</h2>
                    <RulesManager />
                </section>

                <hr style={styles.divider} />


                <section style={styles.section}>
                    <h2 style={styles.sectionTitle}>Dziennik Zdarzeń</h2>
                    <AlertHistory />
                </section>
            </div>
        </div>
    );
};


const styles = {
    container: {
        backgroundColor: '#121212',
        color: '#ffffff',
        minHeight: '100vh',
        padding: '30px',
        fontFamily: 'Arial, sans-serif',
    },
    headerContainer: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '30px',
    },
    titleWrapper: {
        textAlign: 'left',
    },
    headerTitle: {
        fontSize: '2.2rem',
        fontWeight: 'bold',
        margin: '0 0 10px 0',
        letterSpacing: '1px',
    },
    subTitle: {
        color: '#aaaaaa',
        margin: 0,
    },
    sosWrapper: {

    },
    divider: {
        border: 'none',
        borderBottom: '1px solid #333333',
        margin: '30px 0',
    },
    section: {
        marginBottom: '40px',
    },
    sectionTitle: {
        fontSize: '1.5rem',
        marginBottom: '20px',
        color: '#ffffff',
        textTransform: 'uppercase',
        letterSpacing: '0.5px',
    },
};

export default AlertsModule;