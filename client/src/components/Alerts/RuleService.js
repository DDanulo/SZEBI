const API_URL = "http://localhost:8080/api/rules";

export const getAllRules = async () => {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Błąd pobierania reguł");
    return response.json();
};

export const addRule = async (rule) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(rule)
    });
    if (!response.ok) throw new Error("Błąd dodawania reguły");
    return response.json();
};

export const deleteRule = async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE"
    });
    if (!response.ok) throw new Error("Błąd usuwania reguły");
};