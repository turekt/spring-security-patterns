package hr.foi.thesis.security.securelogger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import static hr.foi.thesis.Consts.*;

public class Encrypter {

    private Cipher cipher;
    private SecretKeySpec key;

    public Encrypter(char[] encryptionPassword, String encryptionMethod) {
        try {
            cipher = Cipher.getInstance(encryptionMethod);
            key = deriveKey(encryptionPassword);
            cipher.init(Cipher.ENCRYPT_MODE, key, IV_SPEC);

        } catch (Exception ex) {
            throw new RuntimeException("Cannot initiate encrypter: " + ex.getMessage());
        }
    }

    public SecretKeySpec deriveKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secret = factory.generateSecret(spec);
        return new SecretKeySpec(secret.getEncoded(), ENCRYPTION_ALGORITHM);
    }
    
    public String encrypt(String message) {
        try {
            return Base64.getEncoder()
                         .encodeToString(cipher.doFinal(message.getBytes()));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
