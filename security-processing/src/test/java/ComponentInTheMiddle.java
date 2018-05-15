package hr.foi.thesis.security;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentInTheMiddle {

    public void receive(MaskedListObfuscatedTransferObject object) throws Exception {
        for(Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);

            if(fieldValue instanceof Map) {
                Map map = (Map) fieldValue;
                map.forEach((k, v) -> System.out.println(k + ": " + v));

            } else if(fieldValue instanceof List) {
                List list = (List) fieldValue;
                list.forEach(k -> System.out.println(k));

            } else {
                System.out.println(fieldValue);
            }
        }

        // next in chain ...
    }

    public static void main(String... args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", "Peter");
        map.put("Age", "30");
        map.put("Username", "peter");
        map.put("Credit card number", "0123456789");
        MaskedListObfuscatedTransferObject object = new MaskedListObfuscatedTransferObject(map,
                Arrays.asList("Age", "Credit card number"));

        ComponentInTheMiddle component = new ComponentInTheMiddle();
        component.receive(object);
    }
}
