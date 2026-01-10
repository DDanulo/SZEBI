const API_URL = "http://localhost:8080/api/rules";

export const getAllRules = async () => {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Błąd pobierania reguł");
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const addRule = async (rule) => {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(rule),
        });
        if (!response.ok) throw new Error("Błąd dodawania reguły");
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const deleteRule = async (id) => {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "DELETE",
        });
        if (!response.ok) throw new Error("Błąd usuwania reguły");
    } catch (error) {
        console.error(error);
        throw error;
    }
};