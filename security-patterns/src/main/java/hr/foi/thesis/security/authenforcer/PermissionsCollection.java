package hr.foi.thesis.security.authenforcer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(value = "permissions")
public class PermissionsCollection {

    private List<String> adminPermissions;
    private List<String> userPermissions;

    public List<String> getAdminPermissions() {
        return adminPermissions;
    }

    public void setAdminPermissions(List<String> adminPermissions) {
        this.adminPermissions = adminPermissions;
    }

    public List<String> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<String> userPermissions) {
        this.userPermissions = userPermissions;
    }
}
