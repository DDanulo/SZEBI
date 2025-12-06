import {useEffect, useState} from "react";

export default function PredictionViewer() {
    const [forecast, setForecast] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetch("prediction/latest")
            .then(response => response.json())
            .then(data => {
                setForecast(data);
                setLoading(false);
            })
    }, []);

    if (loading) {
        return <p>Pobieranie ostatniej prognozy...</p>
    }

    return <>
        <h2>Prognoza zużycia energii elektrycznej na za tydzień: </h2>
        <h3>Zużycie: {forecast.forecastedUsage} kWh, Stworzona: {forecast.creationTime}</h3>
    </>
}