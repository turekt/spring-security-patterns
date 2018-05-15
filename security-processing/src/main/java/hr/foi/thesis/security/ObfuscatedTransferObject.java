package hr.foi.thesis.security;

import java.io.Serializable;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class ObfuscatedTransferObject implements Serializable {
    
    private final long serialVersionUID = -1L;
    
    private HashMap<String, Object> map;
    private SealedObject sealedObject;
    
    public ObfuscatedTransferObject() {
        map = new HashMap<>();
    }
    
    public void seal(Cipher cipher, String... keysToRemove) throws Exception {
        sealedObject = new SealedObject(map, cipher);
        
        if(keysToRemove.length > 0) {
            for(String key : keysToRemove) {
                map.remove(key);
            }
        } else {
            map.clear();
        }
    }
    
    public void unseal(Cipher cipher) throws Exception {
        map = (HashMap<String, Object>) sealedObject.getObject(cipher);
    }
    
    public void set(String key, Object value) {
        map.put(key, value);
    }
    
    public Object get(String key) {
        return map.get(key);
    }
}
