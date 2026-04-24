import React, { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    fetchLogs();
    const interval = setInterval(fetchLogs, 2000);
    return () => clearInterval(interval);
  }, []);

  const fetchLogs = async () => {
    const res = await axios.get("https://network-security-system-olrq.onrender.com/logs");
    setLogs(res.data);
  };

  return (
    <div>
      <h1>Security Dashboard</h1>
      <table border="1">
        <thead>
          <tr>
            <th>IP</th>
            <th>Status</th>
            <th>Time</th>
          </tr>
        </thead>
        <tbody>
          {logs.map((log) => (
            <tr key={log.id}>
              <td>{log.ip}</td>
              <td style={{ color: log.status === "ATTACK" ? "red" : "green" }}>
                {log.status}
              </td>
              <td>{log.time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;