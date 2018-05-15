package hr.foi.thesis.security.auditinterceptor;

import java.util.List;
import java.util.regex.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "eventCatalog")
public class EventCatalog {
    
    private List<String> mappings;
    private List<String> methods;

    public void setMappings(List<String> mappings) {
        this.mappings = mappings;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getMappings() {
        return mappings;
    }

    public List<String> getMethods() {
        return methods;
    }
    
    public boolean mappingExists(String mapping, String method) {
        return methods.contains(method) && mappings.stream().anyMatch((String t) -> {
            return Pattern.compile(t).matcher(mapping).matches();
        });
    }
}
