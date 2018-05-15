package hr.foi.thesis.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import static hr.foi.thesis.util.HexUtil.bytesToHex;
import static hr.foi.thesis.util.HexUtil.hexToBytes;

public class PasswordUtil {
    
    public static String getEncryptedPassword(String password, String saltString, int iterations, int keyLength) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), hexToBytes(saltString), iterations, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] encoded = factory.generateSecret(spec).getEncoded();
            return bytesToHex(encoded);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
