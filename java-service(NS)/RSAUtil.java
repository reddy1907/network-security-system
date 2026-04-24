package projectNS;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAUtil {

    private static final String ALGORITHM = "RSA";

    // 1. GENERATE MASTER KEYS (Server calls this once at startup)
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(2048); // 2048-bit RSA is the global security standard
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. ENCRYPT WITH PUBLIC KEY (Client uses this to lock the AES key)
    public static String encrypt(String plainText, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey); // Locked with Public

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. DECRYPT WITH PRIVATE KEY (Server uses this to unlock the AES key)
    public static String decrypt(String encryptedText, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey); // Unlocked with Private

            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decoded);

            return new String(decryptedBytes, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
