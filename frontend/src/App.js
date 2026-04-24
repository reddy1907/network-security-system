import React, { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
} from "chart.js";

import { Pie, Line } from "react-chartjs-2";

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement
);

function App() {
  const [logs, setLogs] = useState([]);
  const [filter, setFilter] = useState("ALL");
  const [showGraphs, setShowGraphs] = useState(false);
  const [viewMode, setViewMode] = useState("LOGS"); // "LOGS" or "ANALYTICS"

  useEffect(() => {
    fetchLogs();
    const interval = setInterval(fetchLogs, 2000);
    return () => clearInterval(interval);
  }, []);

  const fetchLogs = async () => {
    try {
      const res = await axios.get("http://localhost:3001/logs");
      setLogs(res.data);
    } catch (err) {
      console.error("Error fetching logs:", err);
    }
  };

  // Stats
  const total = logs.length;
  const attacks = logs.filter((l) => l.status === "ATTACK").length;
  const blocked = logs.filter((l) => l.status === "BLOCKED").length;
  const permBlocked = logs.filter((l) => l.status === "PERMANENTLY_BLOCKED").length;
  const normal = total - attacks - blocked - permBlocked;

  // Pie Chart
  const pieData = {
    labels: ["Normal", "Attack", "Blocked", "Perm. Blocked"],
    datasets: [
      {
        data: [normal, attacks, blocked, permBlocked],
        backgroundColor: ["green", "red", "purple", "black"],
      },
    ],
  };

  // Line Chart
  const lineData = {
    labels: logs.slice(0, 10).map((_, i) => i + 1),
    datasets: [
      {
        label: "Requests",
        data: logs.slice(0, 10).map((log) =>
          log.status === "ATTACK"
            ? 1
            : log.status === "BLOCKED"
            ? 2
            : log.status === "PERMANENTLY_BLOCKED"
            ? 3
            : 0
        ),
        borderColor: "blue",
        fill: false,
      },
    ],
  };

  // Filter
  const filteredLogs =
    filter === "ALL"
      ? logs
      : logs.filter((log) => log.status === filter);

  // IP Analytics Data
  const ipAnalytics = logs.reduce((acc, log) => {
    const ip = log.ip.replace("/", "");
    if (!acc[ip]) {
      acc[ip] = { ip, total: 0, normal: 0, attacks: 0, blocked: 0, permBlocked: 0 };
    }
    acc[ip].total += 1;
    if (log.status === "NORMAL") acc[ip].normal += 1;
    if (log.status === "ATTACK") acc[ip].attacks += 1;
    if (log.status === "BLOCKED") acc[ip].blocked += 1;
    if (log.status === "PERMANENTLY_BLOCKED") acc[ip].permBlocked += 1;
    return acc;
  }, {});

  const ipAnalyticsArray = Object.values(ipAnalytics).sort((a, b) => b.total - a.total);

  return (
    <div className="container">
      <h1>🔐 Network Security Dashboard</h1>

      {/* Stats */}
      <div className="stats">
        <div className="card total">Total: {total}</div>
        <div className="card normal">Normal: {normal}</div>
        <div className="card attack">Attacks: {attacks}</div>
        <div className="card blocked">Blocked: {blocked}</div>
        <div className="card" style={{backgroundColor: "black", color: "white"}}>Perm Total: {permBlocked}</div>
      </div>

      {/* Filters */}
      <div className="filter-buttons">
        <button
          className={filter === "ALL" ? "btn active all" : "btn all"}
          onClick={() => setFilter("ALL")}
        >
          🔵 All
        </button>

        <button
          className={filter === "NORMAL" ? "btn active normal" : "btn normal"}
          onClick={() => setFilter("NORMAL")}
        >
          🟢 Normal
        </button>

        <button
          className={filter === "ATTACK" ? "btn active attack" : "btn attack"}
          onClick={() => setFilter("ATTACK")}
        >
          🔴 Attack
        </button>

        <button
          className={filter === "BLOCKED" ? "btn active blocked" : "btn blocked"}
          onClick={() => setFilter("BLOCKED")}
        >
          🟣 Blocked
        </button>

        <button
          className={filter === "PERMANENTLY_BLOCKED" ? "btn active all" : "btn all"}
          style={filter === "PERMANENTLY_BLOCKED" ? {backgroundColor: "black", color: "white"} : {}}
          onClick={() => setFilter("PERMANENTLY_BLOCKED")}
        >
          ⚫ Perm. Blocked
        </button>
      </div>

      {/* View Mode Toggle */}
      <div style={{ margin: "20px", display: "flex", justifyContent: "center", gap: "10px" }}>
        <button
          onClick={() => setViewMode("LOGS")}
          className={`btn ${viewMode === "LOGS" ? "active normal" : ""}`}
          style={{ padding: "10px 20px", fontSize: "16px", fontWeight: "bold" }}
        >
          📝 Live Traffic Logs
        </button>
        <button
          onClick={() => setViewMode("ANALYTICS")}
          className={`btn ${viewMode === "ANALYTICS" ? "active normal" : ""}`}
          style={{ padding: "10px 20px", fontSize: "16px", fontWeight: "bold", backgroundColor: viewMode === "ANALYTICS" ? "#3498db" : "" }}
        >
          🕵️ IP Attacker Analytics
        </button>
      </div>

      {/* Graph Toggle */}
      <div style={{ margin: "20px" }}>
        <button
          onClick={() => setShowGraphs(!showGraphs)}
          className="graph-btn"
        >
          📊 {showGraphs ? "Hide Graphs" : "Show Graphs"}
        </button>
      </div>

      {/* Graphs */}
      {showGraphs && (
        <>
          <div style={{ width: "300px", margin: "auto" }}>
            <h3>Traffic Distribution</h3>
            <Pie data={pieData} />
          </div>

          <div style={{ width: "600px", margin: "auto", marginTop: "20px" }}>
            <h3>Traffic Over Time</h3>
            <Line data={lineData} />
          </div>
        </>
      )}

      {/* Tables based on View Mode */}
      {viewMode === "LOGS" ? (
        <table>
          <thead>
            <tr>
              <th>IP Address</th>
              <th>Status</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {filteredLogs.length === 0 ? (
              <tr>
                <td colSpan="3">No Data</td>
              </tr>
            ) : (
              filteredLogs.map((log) => (
                <tr key={log.id}>
                  <td>{log.ip.replace("/", "")}</td>
                  <td
                    className={
                      log.status === "PERMANENTLY_BLOCKED"
                        ? "blocked-text" 
                        : log.status === "BLOCKED"
                        ? "blocked-text"
                        : log.status === "ATTACK"
                        ? "attack-text"
                        : "normal-text"
                    }
                    style={log.status === "PERMANENTLY_BLOCKED" ? { color: "white", backgroundColor: "black", fontWeight: "bold" } : {}}
                  >
                    {log.status}
                  </td>
                  <td>{new Date(log.time).toLocaleString()}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Attacker IP</th>
              <th>Total Requests Sent</th>
              <th>Normal Uses</th>
              <th>Attack Attempts</th>
              <th>Temp Blocks Hit</th>
              <th>Perm Blocks Hit</th>
            </tr>
          </thead>
          <tbody>
            {ipAnalyticsArray.length === 0 ? (
              <tr>
                <td colSpan="6">No Analytics Data</td>
              </tr>
            ) : (
              ipAnalyticsArray.map((stats, idx) => (
                <tr key={idx}>
                  <td style={{ fontWeight: "bold" }}>{stats.ip}</td>
                  <td>{stats.total}</td>
                  <td style={{ color: "green" }}>{stats.normal}</td>
                  <td style={{ color: "red", fontWeight: stats.attacks > 0 ? "bold" : "normal" }}>{stats.attacks}</td>
                  <td style={{ color: "purple" }}>{stats.blocked}</td>
                  <td style={{ color: "white", backgroundColor: stats.permBlocked > 0 ? "black" : "transparent" }}>
                    {stats.permBlocked}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;