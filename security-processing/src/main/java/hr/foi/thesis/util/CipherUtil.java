package hr.foi.thesis.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CipherUtil {

    public static final byte[] SALT = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A };
    public static final byte[] IV_VALUE = new byte[] { 0x00, 0x01, 0x03, 0x03, 0x04, 0x0A, 0x01, 0x06, 0x02, 0x08, 0x0F, 0x05, 0x0B, 0x04, 0x0C, 0x0F };
    public static final IvParameterSpec IV_SPEC = new IvParameterSpec(IV_VALUE);

    public static final int ITERATION_COUNT = 1000;
    public static final int KEY_LENGTH = 128;
    public static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";

    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = String.format("%s/CBC/PKCS5Padding", ENCRYPTION_ALGORITHM);

    public static SecretKeySpec deriveKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secret = factory.generateSecret(spec);
        return new SecretKeySpec(secret.getEncoded(), ENCRYPTION_ALGORITHM);
    }

    public static Cipher defaultCipher(char[] encryptionPassword, int mode) throws Exception {
        return defaultCipher(encryptionPassword, CIPHER_ALGORITHM, mode);
    }

    public static Cipher defaultCipher(char[] encryptionPassword, String encryptionMethod, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(encryptionMethod);
        SecretKeySpec key = deriveKey(encryptionPassword);
        cipher.init(mode, key, IV_SPEC);
        return cipher;
    }
}
