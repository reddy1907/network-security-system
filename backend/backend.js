const express = require("express");
const cors = require("cors");
const { Pool } = require("pg");
const nodemailer = require("nodemailer"); // Imported Nodemailer

const app = express();

app.use(cors());
app.use(express.json()); // ✅ JSON support

const pool = new Pool({
  user: "postgres",
  host: "localhost",
  database: "security",
  password: "Hanuman@123",
  port: 5432,
});

// 📧 SETUP NODEMAILER EMAIL SYSTEM
const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: "reddyreddyramareddy99@gmail.com",
    pass: "kusgxjanvrutjopt",
  },
});

// Prevent Gmail from instantly blocking us for spamming 50 emails a second
const emailedIPs = new Set();

app.post("/log", async (req, res) => {
  console.log("Received:", req.body);

  const { ip, status } = req.body;

  await pool.query(
    "INSERT INTO logs(ip, status) VALUES($1,$2)",
    [ip, status]
  );

  // 🔥 FIRE EMAIL ALERT IF A PERMANENT BLOCK HAPPENS
  if (status === "PERMANENTLY_BLOCKED" && !emailedIPs.has(ip)) {
    emailedIPs.add(ip); // Mark that we emailed evaluating this IP so it doesn't spam

    const mailOptions = {
      from: "reddyreddyramareddy99@gmail.com",
      to: "reddyreddyramareddy99@gmail.com",
      subject: "🚨 CRITICAL THREAT: IP PERMANENTLY BLOCKED 🚨",
      text: `SYSTEM ALERT: \n\nOur Intrusion Detection System has successfully isolated and permanently blocked an active attacker.\n\nAttacker IP Address: ${ip}\nStatus Code: ${status}\nTime Captured: ${new Date().toLocaleString()}\n\nAction Required: Please log into the React Security Dashboard to review the IP Analytics map immediately.`,
    };

    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log("❌ Email Failed (Check your App Password or Internet!):", error.message);
        emailedIPs.delete(ip); // Let it try again if it failed
      } else {
        console.log("✅ CRITICAL ALERT EMAIL SENT!: " + info.response);
      }
    });
  }

  res.send("Saved");
});

app.get("/logs", async (req, res) => {
  const result = await pool.query("SELECT * FROM logs ORDER BY id DESC");
  res.json(result.rows);
});

const PORT = process.env.PORT || 3001;

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});