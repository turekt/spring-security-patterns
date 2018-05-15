package hr.foi.thesis.security.securelogger;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static hr.foi.thesis.Consts.*;

public class SecureLoggerReader {

    private static final String LOG_PATH_PREFIX = "./logs/";
    private static final String LOG_NAME = "patterns-2017-04-22T14:45:58.516.log";
    private static final String LOG_PATH = LOG_PATH_PREFIX + LOG_NAME;
    
    public void decrypt(String logPath, char[] keyStorePassword, char[] encryptionPassword)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidAlgorithmParameterException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(logPath)));
        PublicKey publicKey = getPublicKeyFromKeyStore(keyStorePassword);
        String line = null;

        while((line = reader.readLine()) != null) {
            try {
                String[] parts = line.split(";");

                byte[] digest = Base64.getDecoder().decode(parts[1]);
                byte[] signature = Base64.getDecoder().decode(parts[2]);

                boolean goodSignature = checkSignature(parts[1].getBytes(), signature, publicKey);
                boolean goodDigest = checkDigest(digest, parts[0]);

                if(goodDigest && goodSignature) {
                    System.out.println(decrypt(parts[0], encryptionPassword));
                    continue;
                }

            } catch (SignatureException e) {
            }
            throw new SecurityException("Tampered with log on " + line);
        }
    }

    private PublicKey getPublicKeyFromKeyStore(char[] password) {
        try {
            KeyStore store = KeyStore.getInstance(KEY_STORE_TYPE);
            FileInputStream keyStoreStream = new FileInputStream(new File(KEY_STORE_PATH));

            store.load(keyStoreStream, password);
            PublicKey key = store.getCertificate(KEY_STORE_ALIAS).getPublicKey();

            Arrays.fill(password, '\u0000');
            return key;

        } catch (Exception e) {
            throw new RuntimeException("Cannot load public key from keystore: " + e.getMessage());
        }
    }
    
    private boolean checkSignature(byte[] data, byte[] signedData, PublicKey key)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(signedData);
    }
    
    private boolean checkDigest(byte[] digest, String message) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance(DIGEST_ALGORITHM);
        digester.reset();
        digester.update(message.getBytes());
        byte[] digest2 = digester.digest();
        return Arrays.equals(digest, digest2);
    }
    
    private String decrypt(String message, char[] encryptionKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec key = deriveKey(encryptionKey);
        cipher.init(Cipher.DECRYPT_MODE, key, IV_SPEC);
        return new String(cipher.doFinal(Base64.getDecoder().decode(message)));
    }

    public SecretKeySpec deriveKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secret = factory.generateSecret(spec);
        return new SecretKeySpec(secret.getEncoded(), ENCRYPTION_ALGORITHM);
    }

    public static void main(String... args) throws BadPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidKeySpecException, InvalidAlgorithmParameterException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input keystore password: ");
        String keyStorePassword = scanner.nextLine();
        System.out.println("Please input encryption password: ");
        String encryptionPassword = scanner.nextLine();

        SecureLoggerReader decrypter = new SecureLoggerReader();
        decrypter.decrypt(LOG_PATH, keyStorePassword.toCharArray(), encryptionPassword.toCharArray());
    }
}
