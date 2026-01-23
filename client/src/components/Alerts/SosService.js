const API_URL = "http://localhost:8080/api/alerts/sos";

export const sendSosAlert = async (report) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(report)
    });

    if (!response.ok) {
        throw new Error(`Błąd wysyłania SOS: ${response.status}`);
    }

    const text = await response.text();
    try {
        return text ? JSON.parse(text) : {};
    } catch (e) {
        console.warn("Odpowiedź to nie JSON, zwracam jako tekst.", e);

        return { message: text };
    }
};