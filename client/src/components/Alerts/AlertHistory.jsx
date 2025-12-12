import {useState, useEffect} from "react";

const AlertHistory = () =>{

    const [alerts, setAlerts] = useState([]);


    useEffect(() => {
        const fetchLog = async () =>{
            try {
                const response = await fetch("http://localhost:8080/api/alerts/logs")
                const data = await response.json();


                setAlerts(data);
            } catch (error){
                console.log("Bład modułu alarmowania: ", error)
            }
        };

        fetchLog();
    }, []);


    const formatujDane = (dataBackend) => {
      if(Array.isArray(dataBackend)){
          const data = new Date(
              dataBackend[0],
              dataBackend[1] - 1,
              dataBackend[2],
              dataBackend[3],
              dataBackend[4],
              dataBackend[5],
          );
          return data.toLocaleString();
      }
      return dataBackend;
    };


    return(
      <div className="p-6">
          <h2 className="text-2xl font-bold mb-4">Dziennik Zdarzeń</h2>
          <p>Liczba alertów: {alerts.length}</p>
          <table className="text-2xl text-left border">
              <thead>
              <tr className="bg-gray-200">
                  <th className="p-2">Data</th>
                  <th className="p-2">Poziom</th>
                  <th className="p-2">Wiadomość</th>
              </tr>
              </thead>
              <tbody>
              {alerts.map((alert) => (
                  <tr key={alert.id} className="border-b">
                      <td className="p-2">{formatujDane(alert.timestamp)}</td>
                      <td className="p-2">{alert.level}</td>
                      <td className="p-2">{alert.message}</td>
                  </tr>
              ))}
              </tbody>
          </table>

      </div>
    );

};

export default AlertHistory;