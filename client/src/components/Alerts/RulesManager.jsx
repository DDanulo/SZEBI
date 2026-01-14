import React, { useEffect, useState } from "react";
import { getAllRules, addRule, deleteRule } from "./RuleService";

const METRIC_LABELS = {
    TEMPERATURE: "Temperatura (°C)",
    VOLTAGE: "Napięcie (V)",
    MWH: "Zużycie chwilowe (MWh)",
    ENERGY_CONSUMPTION: "Całkowite Zużycie (Analiza)",
    ENERGY_PRODUCTION: "Całkowita Produkcja (Analiza)",
    DEVICE_EFFICIENCY: "Efektywność Urządzeń (0-1)"
};

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
            console.error("Błąd pobierania reguł:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        const trimmedName = newRule.ruleName.trim();
        const MIN_CHARS = 3;
        const MAX_VALUE_LIMIT = 1000000;
        const MIN_VALUE_LIMIT = -1000000;

        if (!trimmedName) {
            alert("Nazwa reguły nie może być pusta!");
            return;
        }
        if (trimmedName.length < MIN_CHARS) {
            alert(`Nazwa jest za krótka (min. ${MIN_CHARS} znaki).`);
            return;
        }

        if (newRule.value === "" || newRule.value === null) {
            alert("Podaj wartość progową!");
            return;
        }

        const numValue = parseFloat(newRule.value);

        if (numValue > MAX_VALUE_LIMIT || numValue < MIN_VALUE_LIMIT) {
            alert(`Wartość musi mieścić się w przedziale od ${MIN_VALUE_LIMIT} do ${MAX_VALUE_LIMIT}.`);
            return;
        }

        try {
            const ruleToSend = {
                ...newRule,
                ruleName: trimmedName,
                value: numValue
            };
            await addRule(ruleToSend);
            fetchRules();
            setNewRule({ ...newRule, ruleName: "", value: "" });
        } catch (error) {
            console.error("Błąd dodawania reguły:", error);
            alert("Nie udało się dodać reguły.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Usunąć regułę?")) return;
        try {
            await deleteRule(id);
            setRules(rules.filter((r) => r.id !== id));
        } catch (error) {
            console.error("Błąd usuwania:", error);
            alert("Błąd usuwania.");
        }
    };

    const getOperatorSymbol = (op) => {
        switch (op) {
            case "GREATER_THAN": return ">";
            case "LESS_THAN": return "<";
            case "EQUALS": return "=";
            default: return op;
        }
    };

    return (
        <div style={styles.container}>
            <h3 style={styles.header}>⚙️ Konfiguracja Reguł</h3>

            <form onSubmit={handleAdd} style={styles.formPanel}>
                <div style={styles.formRow}>
                    <div style={styles.formGroup}>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <label style={styles.label}>Nazwa (max 50)</label>
                            <span style={{color: newRule.ruleName.length === 50 ? '#d32f2f' : '#666', fontSize: '0.7rem'}}>
                                {newRule.ruleName.length}/50
                            </span>
                        </div>
                        <input
                            style={styles.input}
                            type="text"
                            placeholder="np. Spadek wydajności"
                            value={newRule.ruleName}
                            maxLength={50}
                            onChange={(e) => setNewRule({ ...newRule, ruleName: e.target.value })}
                        />
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>Metryka</label>
                        <select
                            style={styles.select}
                            value={newRule.metric}
                            onChange={(e) => setNewRule({ ...newRule, metric: e.target.value })}
                        >
                            {Object.entries(METRIC_LABELS).map(([key, label]) => (
                                <option key={key} value={key}>{label}</option>
                            ))}
                        </select>
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>Warunek</label>
                        <select
                            style={styles.select}
                            value={newRule.operator}
                            onChange={(e) => setNewRule({ ...newRule, operator: e.target.value })}
                        >
                            <option value="GREATER_THAN">&gt; (Większe)</option>
                            <option value="LESS_THAN">&lt; (Mniejsze)</option>
                            <option value="EQUALS">= (Równe)</option>
                        </select>
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>Wartość</label>
                        <input
                            style={styles.input}
                            type="number"
                            step="0.01"
                            max="1000000"
                            min="-1000000"
                            placeholder="np. 0.9"
                            value={newRule.value}
                            onKeyDown={(evt) => ["e", "E", "+"].includes(evt.key) && evt.preventDefault()}
                            onChange={(e) => setNewRule({ ...newRule, value: e.target.value })}
                        />
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>Poziom</label>
                        <select
                            style={styles.select}
                            value={newRule.level}
                            onChange={(e) => setNewRule({ ...newRule, level: e.target.value })}
                        >
                            <option value="INFO">Info</option>
                            <option value="WARNING">Warning</option>
                            <option value="CRITICAL">Critical</option>
                        </select>
                    </div>
                </div>
                <button type="submit" style={styles.addButton}>DODAJ REGUŁĘ</button>
            </form>

            <div style={styles.tableContainer}>
                {loading ? <p style={{color: "#888"}}>Ładowanie...</p> : (
                    <table style={styles.table}>
                        <thead>
                        <tr style={styles.theadRow}>
                            <th style={{...styles.th, width: "30%"}}>Nazwa</th>
                            <th style={{...styles.th, width: "40%"}}>Warunek</th>
                            <th style={{...styles.th, width: "15%"}}>Poziom</th>
                            <th style={{...styles.th, width: "15%"}}>Akcja</th>
                        </tr>
                        </thead>
                        <tbody>
                        {rules.map((rule) => (
                            <tr key={rule.id} style={styles.tr}>
                                <td style={styles.td}>{rule.ruleName}</td>
                                <td style={styles.td}>
                                    <span style={{color: "#4fc3f7"}}>{METRIC_LABELS[rule.metric] || rule.metric}</span>
                                    <span style={{color: "#fff", margin: "0 10px", fontWeight: "bold"}}>{getOperatorSymbol(rule.operator)}</span>
                                    <span style={{color: "#fff", fontWeight: "bold"}}>{rule.value}</span>
                                </td>
                                <td style={styles.td}>
                                    <span style={getBadgeStyle(rule.level)}>{rule.level}</span>
                                </td>
                                <td style={styles.td}>
                                    <button onClick={() => handleDelete(rule.id)} style={styles.deleteBtn}>USUŃ</button>
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

const styles = {
    container: {
        backgroundColor: "#121212",
        padding: "20px",
        borderRadius: "8px",
        border: "1px solid #333",
        color: "#e0e0e0"
    },
    header: { marginTop: 0, marginBottom: "20px", color: "#fff" },
    formPanel: {
        backgroundColor: "#1e1e1e",
        padding: "15px",
        borderRadius: "6px",
        border: "1px solid #333",
        marginBottom: "20px"
    },
    formRow: { display: "flex", gap: "15px", flexWrap: "wrap", alignItems: "flex-end" },
    formGroup: { display: "flex", flexDirection: "column", flex: 1, minWidth: "100px" },
    label: { fontSize: "0.8rem", marginBottom: "5px", color: "#aaa", textTransform: "uppercase" },
    input: { padding: "8px", backgroundColor: "#2c2c2c", border: "1px solid #444", color: "#fff", borderRadius: "4px" },
    select: { padding: "8px", backgroundColor: "#2c2c2c", border: "1px solid #444", color: "#fff", borderRadius: "4px" },
    addButton: {
        marginTop: "15px", width: "100%", padding: "10px", backgroundColor: "#388e3c", color: "white",
        border: "none", borderRadius: "4px", cursor: "pointer", fontWeight: "bold"
    },
    tableContainer: { overflowX: "auto" },
    table: {
        width: "100%",
        borderCollapse: "collapse",
        fontSize: "0.9rem",
        tableLayout: "fixed"
    },
    theadRow: { backgroundColor: "#1e1e1e", color: "#aaa", textTransform: "uppercase" },

    // 4. STYLE: textAlign: "left"
    th: { padding: "10px", textAlign: "left", borderBottom: "2px solid #444", wordWrap: "break-word" },
    tr: { borderBottom: "1px solid #333" },
    td: { padding: "10px", color: "#ddd", wordWrap: "break-word", verticalAlign: "middle", textAlign: "left" }, // Tutaj zmieniono na LEFT

    deleteBtn: {
        backgroundColor: "transparent", border: "1px solid #d32f2f", color: "#d32f2f",
        padding: "4px 8px", borderRadius: "4px", cursor: "pointer", fontSize: "0.75rem"
    }
};

const getBadgeStyle = (level) => {
    const base = { padding: "2px 6px", borderRadius: "4px", fontSize: "0.7rem", fontWeight: "bold", display: "inline-block" };
    switch (level) {
        case "CRITICAL": return { ...base, border: "1px solid #d32f2f", color: "#d32f2f" };
        case "WARNING": return { ...base, border: "1px solid #f57c00", color: "#f57c00" };
        default: return { ...base, border: "1px solid #1976d2", color: "#1976d2" };
    }
};

export default RulesManager;