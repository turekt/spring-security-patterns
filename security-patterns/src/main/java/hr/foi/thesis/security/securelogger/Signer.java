package hr.foi.thesis.security.securelogger;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class Signer {
    
    private Signature signature;
    private PrivateKey key;

    public Signer(String signatureAlgorithm, PrivateKey key) {
        try {
            signature = Signature.getInstance(signatureAlgorithm);
            this.key = key;
        } catch (Exception ex) {
            throw new RuntimeException("Cannot initiate signer: " + ex.getMessage());
        }
    }
    
    public String sign(String message) {
        try {
            signature.initSign(key);
            signature.update(message.getBytes());
            return Base64.getEncoder().encodeToString(signature.sign());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
