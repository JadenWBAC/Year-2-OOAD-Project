package banking.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {

    /**
     * Hash a password using SHA-256
     * @param password The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a hashed password
     * @param inputPassword The plain text password to verify
     * @param hashedPassword The hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(hashedPassword);
    }
}