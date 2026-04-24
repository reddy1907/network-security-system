package projectNS;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final String key = "1234567890123456"; // 16 chars = 128-bit key

    public static String encrypt(String str) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);//Gets AES cipher instance
            cipher.init(Cipher.ENCRYPT_MODE, skey);// Initialize for Encryption , Sets cipher to encryption mode

            byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));//Converts input string → bytes
            return Base64.getEncoder().encodeToString(encrypted);//Converts binary data → readable string

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String str) { //Input: encrypted string, Output: original text

        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skey); //Initialize for Decryption

            byte[] decoded = Base64.getDecoder().decode(str);// Converts readable string → original encrypted bytes
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8); // Decrypts bytes → original plaintext string

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
//Cipher → Core class for encryption/decryption
//SecretKeySpec → Used to create a key object from bytes
//Base64 → Encodes binary data into readable string format
//StandardCharsets → Ensures UTF-8 encoding consistency
//ALGORITHM = "AES" → Specifies encryption algorithm
//TRANSFORMATION = "AES" → Defines cipher configuration