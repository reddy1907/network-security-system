# 🔐 Network Security Monitoring System

## 📌 Overview
A distributed network security system designed to ensure secure communication, detect malicious activities, and provide real-time monitoring.

This project integrates Node.js, a Java-based security engine, and a React dashboard to simulate a real-world intrusion detection and monitoring system.

---

## 🏗️ Architecture

Client → Node.js Backend → Java Security Engine → PostgreSQL → React Dashboard

---

## ⚙️ Tech Stack

- **Backend:** Node.js, Express.js  
- **Security Engine:** Java (AES + RSA Encryption, Intrusion Detection System)  
- **Frontend:** React.js  
- **Database:** PostgreSQL  

---

## 🚀 Features

- Secure data transmission using AES encryption  
- RSA-based key exchange for secure communication  
- Intrusion Detection System (IDS) using rate-based detection  
- Real-time monitoring dashboard with logs and graphs  
- Centralized logging system using PostgreSQL  
- Temporary and permanent IP blocking system  
- Multi-service architecture integrating Node.js and Java  

---

## 🧠 DSA & System Design Concepts Used

- **HashMap (Hashing):** Used to efficiently track IP addresses, request counts, and blocked users with constant-time lookup.  

- **Sliding Window Algorithm:** Implemented to monitor requests within a time window and detect high-frequency attacks like DDoS or brute-force attempts.  

- **Dynamic Arrays (ArrayList):** Used to store timestamps of incoming requests for each IP and enable time-based filtering.  

- **Frequency Counting:** Applied to track repeated requests and identify suspicious or malicious behavior.  

- **Conditional Logic & Thresholding:** Used to classify traffic based on limits such as maximum requests, block duration, and attack thresholds.  

- **Client-Server Architecture:** Implemented using Java sockets to simulate real-time communication between client and server.  

- **Distributed System Design:** Integrated Node.js (API layer), Java (security engine), and React (dashboard) to simulate a real-world multi-service system.  

- **Cryptography (AES + RSA):** Used AES for secure data encryption and RSA for secure key exchange during communication.  

---

## 📂 Project Structure

network-security-system/  
│  
├── frontend/  
├── backend/  
├── java-service/  

---

## ▶️ How to Run

### Backend
cd backend  
npm install  
npm start  

### Frontend
cd frontend  
npm install  
npm start  

### Java Service
Run using IntelliJ  
or via terminal:  

javac *.java  
java Server  

---

## 📊 System Flow

1. Client sends request  
2. Node.js backend processes the request  
3. Java service performs encryption handling and intrusion detection  
4. Logs are stored in PostgreSQL  
5. React dashboard displays logs and alerts  

---

## 💡 Key Highlights

- Hybrid architecture combining Node.js and Java  
- Real-world intrusion detection system simulation  
- Double encryption using AES and RSA  
- Full-stack + backend + security integration  

---


## 📈 Future Improvements

- JWT Authentication  
- Real-time alerts using WebSockets  
- Cloud deployment  
- Advanced anomaly detection  

---

## 👨‍💻 Author

**T Reddy**  
Bangalore, India  

---

