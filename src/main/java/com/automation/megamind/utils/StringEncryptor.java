package com.automation.megamind.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.automation.megamind.constants.Constants.CONFIG_PROPERTIES;
import static com.automation.megamind.utils.PropertyReader.getProperty;

/**
 * Utility class to demonstrate AES encryption and decryption of a string.
 *
 * WARNING: The secret key and IV are hardcoded for demonstration purposes only.
 * This is highly insecure and MUST NOT be used in a production environment.
 * Keys and IVs must be securely managed and stored externally.
 */
public class StringEncryptor {

    // --- SECURITY RISK: DO NOT USE HARDCODED KEYS IN PRODUCTION ---
    // Key must be 16, 24, or 32 bytes (128, 192, or 256 bits) for AES.
    private static final String SECRET_KEY_STRING = "ThisIsASecretKey"; // 16 bytes (128 bits)
    private static final String INIT_VECTOR_STRING = "RandomInitVector"; // 16 bytes (128 bits)
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    // -------------------------------------------------------------

    private static final SecretKeySpec SECRET_KEY = new SecretKeySpec(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8), "AES");

    private static final IvParameterSpec IV_SPEC = new IvParameterSpec(INIT_VECTOR_STRING.getBytes(StandardCharsets.UTF_8));

    /**
     * Encrypts the given plaintext string using AES encryption (CBC mode).
     *
     * @param plainText The string value to be encrypted.
     * @return The Base64 encoded ciphertext string, or null if encryption fails.
     */
    public static String encryptString(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            System.err.println("Input string cannot be null or empty.");
            return null;
        }

        try {
            // 1. Get the Cipher instance for AES
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            // 2. Initialize the cipher for encryption mode
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, IV_SPEC);

            // 3. Encrypt the data
            byte[] encryptedBytes = cipher.doFinal(
                    plainText.getBytes(StandardCharsets.UTF_8)
            );

            // 4. Encode the resulting byte array to a Base64 string for safe transport/storage
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            System.err.println("Error during encryption: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypts the given Base64 encoded ciphertext string using AES encryption (CBC mode).
     *
     * @param cipherText The Base64 encoded string value to be decrypted.
     * @return The original plaintext string, or null if decryption fails.
     */
    public static String decryptString(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            System.err.println("Input string cannot be null or empty.");
            return null;
        }

        try {
            // 1. Get the Cipher instance for AES
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            // 2. Initialize the cipher for decryption mode
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, IV_SPEC);

            // 3. Decode the Base64 string back into a byte array
            byte[] decryptedBytes = cipher.doFinal(
                    Base64.getDecoder().decode(cipherText)
            );

            // 4. Convert the resulting byte array back to the original string
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Error during decryption: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static String getPwd(String user){
        String pwd = "";
        if(user.equals("Amit"))
           pwd= decryptString("5k7ZlLM7WIribxyEQoJ6RQ==");
        else if(user.equals("Vishal"))
           pwd= decryptString("tP0lrn6oLXHMXYN1zp1qXg==");
        else if(user.equals("Vikash"))
            pwd= decryptString("DHZ30CjbRN8f6SWPLAHggg==");
        else if(user.equals("Shalini"))
            pwd= decryptString("tP0lrn6oLXHMXYN1zp1qXg==");
        else if(user.equals("nidhi"))
            pwd= decryptString("5k7ZlLM7WIribxyEQoJ6RQ==");
        return pwd;

    }

    /**
     * Main method to demonstrate the usage of the encryptString and decryptString methods.
     */
    public static void main(String[] args) {
        String originalString = "abcd12345";
        System.out.println("Original Text: " + originalString);

        // --- Encryption ---
        String encryptedText = encryptString(originalString);

        if (encryptedText != null) {
            System.out.println("Encrypted (Base64) Text: " + encryptedText);

            // --- Decryption ---
            String decryptedText = decryptString(encryptedText);

            if (decryptedText != null) {
                System.out.println("Decrypted Text: " + decryptedText);
            }
        }
    }
}
