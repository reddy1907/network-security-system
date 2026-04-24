package projectNS;

import java.net.*;
import java.io.*;
import java.util.*;
import java.security.KeyPair;

public class Server {

    static Map<String, Long> blockedIPs = new HashMap<>();
    static Map<String, Integer> blockCounts = new HashMap<>();
    static Map<String, Integer> attackAttempts = new HashMap<>();

    static final long BLOCK_TIME = 3000;
    static final int MAX_BLOCKS = 4;

    // 🔑 ADDED: The Master RSA Keys for the Server
    static KeyPair serverKeys;

    public static void main(String[] args) throws Exception {
        System.out.println("Generating RSA Master Keys. Please wait...");
        serverKeys = RSAUtil.generateKeyPair(); // Generate keys at startup
        System.out.println("RSA Keys Generated successfully!");

        ServerSocket server = new ServerSocket(5000);
        System.out.println("Network Security Server running on port 5000...");

        while (true) {
            Socket socket = server.accept();

            String ip = socket.getInetAddress().getHostAddress();
            long currentTime = System.currentTimeMillis();

            attackAttempts.put(ip, attackAttempts.getOrDefault(ip, 0) + 1);

            // ⛔ 1. PERMANENT BLOCK CHECK
            int currentOffenses = blockCounts.getOrDefault(ip, 0);
            if (currentOffenses >= MAX_BLOCKS) {
                System.out.println("⛔ [" + ip + "] IS PERMANENTLY BLOCKED. (Total Hits: " + attackAttempts.get(ip) + ")");
                sendToNode(ip, "PERMANENTLY_BLOCKED");
                socket.close();
                continue;
            }

            // 🔴 2. TEMPORARY BLOCK CHECK
            if (blockedIPs.containsKey(ip)) {
                long blockedAt = blockedIPs.get(ip);
                if (currentTime - blockedAt < BLOCK_TIME) {
                    System.out.println("🚫 [" + ip + "] IS TEMP BLOCKED. Please wait.");
                    sendToNode(ip, "BLOCKED");
                    socket.close();
                    continue;
                } else {
                    blockedIPs.remove(ip);
                    System.out.println("✅ [" + ip + "] Temp block expired. Unblocked.");
                }
            }

            // 🔥 3. IDS FIRST (Fast Detection)
            boolean attack = IntrusionDetector.isAttack(ip);
            String status = attack ? "ATTACK" : "NORMAL";

            // 🔐 4. DOUBLE ENCRYPTION (RSA Handshake + AES Transfer)
            try {
                // a) Give the Client our Public Key
                ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
                outObj.writeObject(serverKeys.getPublic());
                outObj.flush();

                // b) Read the locked AES Key and the encrypted Message back from Client
                ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
                String lockedAes = (String) inObj.readObject();
                String encryptedMessage = (String) inObj.readObject();

                // c) Use RSA Private Key to unlock the AES Key!
                String unlockedAesKey = RSAUtil.decrypt(lockedAes, serverKeys.getPrivate());
                System.out.println("🤝 RSA Handshake Complete! Received AES Key: " + unlockedAesKey);

                if (encryptedMessage != null) {
                    // d) Decrypt the actual message with AES
                    String message = EncryptionUtil.decrypt(encryptedMessage);
                    System.out.println("Message: " + message + " | Status: " + status);
                }
            } catch(Exception e) {
                // Ignore connection drops from spamming clients
            }

            // 🔴 5. PENALIZE IF ATTACK DETECTED
            if (attack) {
                blockedIPs.put(ip, currentTime);
                currentOffenses++;
                blockCounts.put(ip, currentOffenses);
                System.out.println("⚠ [" + ip + "] ATTACK DETECTED! (Strike " + currentOffenses + "/" + MAX_BLOCKS + ")");

                if (currentOffenses >= MAX_BLOCKS) {
                    status = "PERMANENTLY_BLOCKED";
                    System.out.println("⛔ [" + ip + "] Reached maximum strikes. Upgraded to PERMANENT BLOCK.");
                }
            }

            // 🔗 6. FORWARD TO NODE DB
            sendToNode(ip, status);
            socket.close();
        }
    }

    static void sendToNode(String ip, String status) {
        try {
            URL url = new URL("http://localhost:3001/log");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonPayload = "{\"ip\":\"" + ip + "\", \"status\":\"" + status + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(jsonPayload.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                System.out.println("❌ ERROR: Node.js (Port 3001) rejected the log! Status: " + status);
            }

        } catch (Exception e) {
            // Server offline
        }
    }
}
