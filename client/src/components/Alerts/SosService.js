const API_URL = "http://localhost:8080/api/alerts/sos";

export const sendSos = async (userId, message, location) => {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                userID: userId,
                message: message,
                location: location,
            }),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Błąd wysyłania zgłoszenia SOS");
        }

        return await response.text();
    } catch (error) {
        console.error("Błąd serwisu SOS:", error);
        throw error;
    }
};