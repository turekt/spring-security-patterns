package hr.foi.thesis.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaskedListObfuscatedTransferObject implements Serializable {

    private Map<String, Object> confidentialMap;
    private Map<String, Object> map;

    public MaskedListObfuscatedTransferObject(Map<String, Object> mapAllValues, List<String> confidentialKeys) {
        this.map = new HashMap<>();
        this.confidentialMap = new HashMap<>();

        mapAllValues.forEach((k, v) -> {
            if(confidentialKeys.contains(k)) {
                confidentialMap.put(k, v);
            } else {
                map.put(k, v);
            }
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> builder.append(k + ": " + v));
        return builder.toString();
    }
}
