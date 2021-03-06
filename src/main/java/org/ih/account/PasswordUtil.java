package org.ih.account;

import org.apache.commons.codec.binary.Hex;
import org.ih.common.exception.UtilityException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.UUID;

/**
 * Utility class for handling account passwords
 *
 * @author Hector Plahar
 */
public class PasswordUtil {

    private static final int HASH_BYTE_SIZE = 160;
    private static final int SALT_BYTE_SIZE = 32;
    private static final int PBKDF2_ITERATIONS = 20000;
    private static final int TOKEN_BYTE_SIZE = 128;

    public static String encryptPassword(String password, String salt) throws UtilityException {
        if (password == null || password.trim().isEmpty() || salt == null || salt.trim().isEmpty())
            throw new NullPointerException("Password and/or salt cannot be empty");

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = keyFactory.generateSecret(spec).getEncoded();
            return String.valueOf(Hex.encodeHex(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new UtilityException(e);
        }
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        return String.valueOf(Hex.encodeHex(salt));
    }

    public static String generateTemporaryPassword() {
        char[] arr = UUID.randomUUID().toString().substring(24).toCharArray();
        boolean converted = false;
        for (int i = 0; i < arr.length; i += 1) {
            if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] = (char) (arr[i] - 32);
                if (converted)
                    break;
                converted = true;
            }
        }
        return String.copyValueOf(arr);
    }

    public static String generateRandomToken(int byteSize) {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[byteSize];
        random.nextBytes(token);
        return DatatypeConverter.printBase64Binary(token);
    }
}
