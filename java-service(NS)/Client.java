package projectNS;

import java.net.*;
import java.io.*;
import java.security.PublicKey;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting Advanced Hybrid-Encrypted Attack Simulation...");

            // Loop 5 times (4 to build up the history, 5th to trigger the permanent block)
            for (int blockNum = 1; blockNum <= 5; blockNum++) {
                System.out.println("\n🔥 --- Initiating Attack Wave " + blockNum + " --- 🔥");

                // Send 4 rapid requests to trigger the IntrusionDetector
                for (int i = 0; i < 4; i++) {
                    try {
                        Socket socket = new Socket("localhost", 5000);

                        // 🔐 1. THE RSA HANDSHAKE
                        // Automatically receive the Server's Public Key over the network
                        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
                        PublicKey serverKey = (PublicKey) inObj.readObject();

                        // Lock our 128-bit AES Key securely using their Public Key
                        String myAesKey = "1234567890123456";
                        String lockedAes = RSAUtil.encrypt(myAesKey, serverKey);

                        // 🔐 2. THE AES MESSAGE TRANSFER
                        // Quickly encrypt our payload using AES
                        String msg = "Malicious Injection Payload " + blockNum + "-" + i;
                        String encryptedMessage = EncryptionUtil.encrypt(msg);

                        // 📤 3. SEND BOTH OVER THE NETWORK
                        ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
                        outObj.writeObject(lockedAes);         // First string is the locked AES key
                        outObj.writeObject(encryptedMessage);  // Second string is the AES encrypted payload
                        outObj.flush();

                        socket.close();

                        // Fast requests to trigger the IDS
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println("Connection failed - Server is ignoring us.");
                    }
                }

                if (blockNum <= 4) {
                    System.out.println("🛑 Wave completed. Wait 4 seconds for temp block to expire...");
                    Thread.sleep(4000);
                }
            }

            System.out.println("\n✅ Simulation complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// one time block
/*package projectNS;

import java.net.*;
import java.io.*;
    
public class Client {
    public static void main(String[] args) {
        try {

            // 🔥 LOOP → simulate attack
            for (int i = 0; i < 6; i++) {

                // ✅ FIX: use localhost (VERY IMPORTANT)
                Socket socket = new Socket("localhost", 5000);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String msg = "Hello Server";
                String encrypted = EncryptionUtil.encrypt(msg);

                System.out.println("Request " + (i + 1));
                System.out.println("Encrypted: " + encrypted);

                out.println(encrypted);

                socket.close();

                // 🔥 very fast requests → triggers attack
                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/

/* 4 times blocked one
package projectNS;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting Advanced Attack Simulation...");

            // Loop 5 times (4 to build up the history, 5th to trigger the permanent block)
            for (int blockNum = 1; blockNum <= 5; blockNum++) {
                System.out.println("\n🔥 --- Initiating Attack Wave " + blockNum + " --- 🔥");

                // Send 4 rapid requests to trigger the IntrusionDetector (Limit is 2)
                for (int i = 0; i < 4; i++) {
                    try {
                        Socket socket = new Socket("localhost", 5000);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                        String msg = "Malicious Injection Payload " + blockNum + "-" + i;
                        String encrypted = EncryptionUtil.encrypt(msg);

                        out.println(encrypted);
                        socket.close();

                        // Fast requests to trigger the IDS
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println("Connection failed.");
                    }
                }

                if (blockNum <= 4) {
                    System.out.println("🛑 Wave completed. Server should have temp-blocked us for 10 seconds.");
                    System.out.println("⏳ Waiting 11 seconds for temp block to expire before launching the next wave...");

                    // Wait 11 seconds to guarantee the server's temporary 10-sec block expires
                    Thread.sleep(11000);
                }
            }

            System.out.println("\n✅ Attack Simulation complete! Check your React Dashboard.");
            System.out.println("The IP should now show up as entirely BLACK (PERMANENTLY BLOCKED).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

/* TEN TIMES PATTERN
package projectNS;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting Advanced Attack Simulation...");

            // Loop 11 times so we hit the 10-block limit and guarantee a PERMANENT BLOCK
            for (int blockNum = 1; blockNum <= 11; blockNum++) {
                System.out.println("\n🔥 --- Initiating Attack Wave " + blockNum + " --- 🔥");

                // Send 4 rapid requests to trigger the IntrusionDetector (Limit is > 2)
                for (int i = 0; i < 4; i++) {
                    try {
                        Socket socket = new Socket("localhost", 5000);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                        String msg = "Malicious Injection Payload " + blockNum + "-" + i;
                        String encrypted = EncryptionUtil.encrypt(msg);

                        out.println(encrypted);
                        socket.close();

                        // Fast requests to trigger the IDS
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println("Connection failed.");
                    }
                }

                if (blockNum <= 10) {
                    System.out.println("🛑 Wave completed. Server should have temp-blocked us for 10 seconds.");
                    System.out.println("⏳ Waiting 11 seconds for temp block to expire before launching the next wave...");

                    // Wait 11 seconds to guarantee the server's temporary 10-sec block expires
                    Thread.sleep(11000);
                }
            }

            System.out.println("\n✅ Attack Simulation complete! Check your React Dashboard.");
            System.out.println("The IP should now show up as entirely BLACK (PERMANENTLY BLOCKED).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

*/
/*
package projectNS;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {

            // ✅ SINGLE CONNECTION
            Socket socket = new Socket("localhost", 5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String msg = "Hello Server";
            String encrypted = EncryptionUtil.encrypt(msg);

            System.out.println("Sending single request...");
            System.out.println("Encrypted: " + encrypted);

            out.println(encrypted);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/