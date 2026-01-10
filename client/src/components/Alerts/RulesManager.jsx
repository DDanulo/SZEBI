import React, { useEffect, useState } from "react";
import { getAllRules, addRule, deleteRule } from "./RuleService";

const RulesManager = () => {
    const [rules, setRules] = useState([]);
    const [loading, setLoading] = useState(false);

    const [newRule, setNewRule] = useState({
        ruleName: "",
        metric: "TEMPERATURE",
        value: "",
        operator: "GREATER_THAN",
        level: "WARNING",
    });

    useEffect(() => {
        fetchRules();
    }, []);

    const fetchRules = async () => {
        setLoading(true);
        try {
            const data = await getAllRules();
            setRules(data);
        } catch (error) {
            console.error(error);
            alert("Nie udało się pobrać reguł!");
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        if (!newRule.ruleName || !newRule.metric || !newRule.value) {
            alert("Wypełnij wszystkie pola!");
            return;
        }

        try {
            const ruleToSend = { ...newRule, value: parseFloat(newRule.value) };
            await addRule(ruleToSend);
            fetchRules();
            setNewRule({ ...newRule, ruleName: "", value: "" });
        } catch (error) {
            console.error(error);
            alert("Błąd podczas dodawania reguły");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tę regułę?")) return;
        try {
            await deleteRule(id);
            setRules(rules.filter((rule) => rule.id !== id));
        } catch (error) {
            console.error(error);
            alert("Błąd usuwania");
        }
    };

    return (
        <div style={styles.container}>


            <div style={styles.formWrapper}>
                <h4 style={styles.subHeader}>Dodaj nową regułę</h4>
                <form onSubmit={handleAdd} style={styles.form}>
                    <div style={styles.formGroup}>
                        <label style={styles.label}>Nazwa reguły:</label>
                        <input
                            type="text"
                            value={newRule.ruleName}
                            onChange={(e) => setNewRule({ ...newRule, ruleName: e.target.value })}
                            style={styles.input}
                        />
                    </div>

                    <div style={styles.row}>
                        <div style={styles.formGroup}>
                            <label style={styles.label}>Metryka:</label>
                            <select
                                value={newRule.metric}
                                onChange={(e) => setNewRule({ ...newRule, metric: e.target.value })}
                                style={styles.select}
                            >
                                <option value="TEMPERATURE">Temperatura</option>
                                <option value="VOLTAGE">Napięcie</option>
                                <option value="MWH">Zużycie (MWh)</option>
                            </select>
                        </div>

                        <div style={styles.formGroup}>
                            <label style={styles.label}>Operator:</label>
                            <select
                                value={newRule.operator}
                                onChange={(e) => setNewRule({ ...newRule, operator: e.target.value })}
                                style={styles.select}
                            >
                                <option value="GREATER_THAN">&gt; (Większe)</option>
                                <option value="LESS_THAN">&lt; (Mniejsze)</option>
                                <option value="EQUALS">= (Równe)</option>
                            </select>
                        </div>

                        <div style={styles.formGroup}>
                            <label style={styles.label}>Próg:</label>
                            <input
                                type="number"
                                step="0.1"
                                value={newRule.value}
                                onChange={(e) => setNewRule({ ...newRule, value: e.target.value })}
                                style={styles.input}
                            />
                        </div>
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>Poziom:</label>
                        <select
                            value={newRule.level}
                            onChange={(e) => setNewRule({ ...newRule, level: e.target.value })}
                            style={styles.select}
                        >
                            <option value="INFO">INFO</option>
                            <option value="WARNING">WARNING</option>
                            <option value="CRITICAL">CRITICAL</option>
                        </select>
                    </div>

                    <button type="submit" style={styles.addButton}>DODAJ REGUŁĘ</button>
                </form>
            </div>


            <div style={styles.tableWrapper}>
                <h4 style={styles.subHeader}>Lista aktywnych reguł</h4>
                {loading ? (
                    <p>Ładowanie...</p>
                ) : rules.length === 0 ? (
                    <p style={{ color: "#777" }}>Brak reguł.</p>
                ) : (
                    <table style={styles.table}>
                        <thead>
                        <tr style={styles.tableHeaderRow}>
                            <th style={styles.th}>Nazwa</th>
                            <th style={styles.th}>Warunek</th>
                            <th style={styles.th}>Poziom</th>
                            <th style={styles.th}>Akcja</th>
                        </tr>
                        </thead>
                        <tbody>
                        {rules.map((rule) => (
                            <tr key={rule.id} style={styles.tableRow}>
                                <td style={styles.td}>{rule.ruleName}</td>
                                <td style={styles.td}>
                                    {rule.metric} {rule.operator === "GREATER_THAN" ? ">" : rule.operator === "LESS_THAN" ? "<" : "="} {rule.value}
                                </td>
                                <td style={styles.td}>
                                    <span style={getBadgeStyle(rule.level)}>{rule.level}</span>
                                </td>
                                <td style={styles.td}>
                                    <button onClick={() => handleDelete(rule.id)} style={styles.deleteButton}>USUŃ</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};


const getBadgeStyle = (level) => {
    const base = { padding: "2px 6px", border: "1px solid", fontSize: "11px", fontWeight: "bold", textTransform: 'uppercase' };
    switch (level) {
        case "CRITICAL": return { ...base, borderColor: "#d32f2f", color: "#d32f2f" };
        case "WARNING": return { ...base, borderColor: "#ff9800", color: "#ff9800" };
        case "INFO": return { ...base, borderColor: "#2196f3", color: "#2196f3" };
        default: return base;
    }
};


const styles = {
    container: { color: '#ffffff', textAlign: 'left' },
    formWrapper: { marginBottom: '30px', border: '1px solid #333', padding: '20px' },
    subHeader: { marginTop: 0, marginBottom: '20px', color: '#aaaaaa', textTransform: 'uppercase', fontSize: '0.9rem' },
    form: { display: "flex", flexDirection: "column", gap: "15px" },
    formGroup: { display: "flex", flexDirection: "column" },
    row: { display: "flex", gap: "15px" },
    label: { fontWeight: "bold", marginBottom: "5px", color: "#ccc", fontSize: '12px', textTransform: 'uppercase' },
    input: { padding: "10px", backgroundColor: "#1e1e1e", color: "#fff", border: "1px solid #444", borderRadius: '0' },
    select: { padding: "10px", backgroundColor: "#1e1e1e", color: "#fff", border: "1px solid #444", borderRadius: '0' },
    addButton: { padding: "12px", backgroundColor: "#388e3c", color: "white", border: "none", cursor: "pointer", fontWeight: "bold", textTransform: 'uppercase', marginTop: '10px' },

    tableWrapper: { border: '1px solid #333', padding: '20px' },
    table: { width: "100%", borderCollapse: "collapse", fontSize: '14px' },
    tableHeaderRow: { backgroundColor: "#1e1e1e", textTransform: 'uppercase' },
    th: { textAlign: "left", padding: "12px", borderBottom: "2px solid #444", color: '#aaaaaa', fontSize: '12px' },
    tableRow: { borderBottom: "1px solid #333" },
    td: { padding: "12px", textAlign: "left" },
    deleteButton: { backgroundColor: "transparent", color: "#d32f2f", border: "1px solid #d32f2f", padding: "5px 10px", cursor: "pointer", fontSize: '11px', textTransform: 'uppercase' },
};

export default RulesManager;