package hr.foi.thesis.security.authenforcer;

import hr.foi.thesis.security.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.foi.thesis.Consts.ROLE_ADMIN;
import static hr.foi.thesis.Consts.ROLE_USER;

@Component
public class AuthorizationEnforcer {

    @Autowired
    private PermissionsCollection permissionsCollection;

    public <T> boolean isAuthorized(RequestContext<T> context) {
        Subject subject = context.getSubject();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if(subject != null) {
            authorities = subject.getPublicCredentials(SimpleGrantedAuthority.class);
        }

        List<String> adminPermissions = permissionsCollection.getAdminPermissions();
        List<String> userPermissions = permissionsCollection.getUserPermissions();
        if(adminPermissions.contains(context.getRequest().getRequestURI())) {
            // Admin role
            return authorities.contains(ROLE_ADMIN);
        }

        if(userPermissions.contains(context.getRequest().getRequestURI())) {
            // User role
            return authorities.contains(ROLE_USER) || authorities.contains(ROLE_ADMIN);
        } else {
            // Public
            return true;
        }
    }
}
