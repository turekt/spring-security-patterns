package hr.foi.thesis.security.securelogger;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static hr.foi.thesis.Consts.*;

public class SecureLogger {

    private Long sequenceNumber;
    private final Encrypter encrypter;
    private final Digester digester;
    private final Signer signer;

    public SecureLogger(char[] encryptionKey, char[] keyStorePassword) {
        encrypter = new Encrypter(encryptionKey, CIPHER_ALGORITHM);
        digester = new Digester(DIGEST_ALGORITHM);
        signer = new Signer(SIGNATURE_ALGORITHM, getPrivateKeyFromKeyStore(keyStorePassword));
        this.sequenceNumber = 0L;
    }

    private PrivateKey getPrivateKeyFromKeyStore(char[] password) {
        try {
            KeyStore store = KeyStore.getInstance(KEY_STORE_TYPE);
            FileInputStream keyStoreStream = new FileInputStream(new File(KEY_STORE_PATH));

            store.load(keyStoreStream, password);
            PrivateKey key = (PrivateKey) store.getKey(KEY_STORE_ALIAS, password);

            Arrays.fill(password, '\u0000');
            return key;

        } catch (Exception e) {
            throw new RuntimeException("Cannot load private key from keystore: " + e.getMessage());
        }
    }

    public void log(String message) {
        try {
            sequenceNumber++;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String seq = format.format(new Date(System.currentTimeMillis()));

            message = String.format("%09d. %s --- %s", sequenceNumber, seq, message);
            System.out.println(message);

            String encrypted = encrypter.encrypt(message);
            String digested = digester.digest(encrypted);
            String signed = signer.sign(digested);

            message = String.format("%s;%s;%s", encrypted, digested, signed);
            LogManager.log(message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
