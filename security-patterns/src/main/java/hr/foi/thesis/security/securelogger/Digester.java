package hr.foi.thesis.security.securelogger;

import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.util.Base64;

public class Digester {

    private MessageDigest digest;

    public Digester(String digestAlgorithm) {
        try {
            digest = MessageDigest.getInstance(digestAlgorithm);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot initiate digester: " + ex.getMessage());
        }
    }
    
    public String digest(String message) {
        try {
            digest.update(message.getBytes());
            return Base64.getEncoder().encodeToString(digest.digest());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
