package hr.foi.thesis.security.authenforcer;

import com.sun.security.auth.UserPrincipal;
import hr.foi.thesis.security.RequestContext;
import java.util.Arrays;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import javax.security.auth.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

@Component
public class AuthenticationEnforcer {
    
    @Autowired
    private SimpleAuthenticationProvider authProvider;

    public void authenticate(RequestContext context) throws LoginException {
        authenticateViaForm(context);
    }

    protected void authenticateViaForm(RequestContext context) throws LoginException {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        ModelMap map = context.getModelMap();
        if(a == null || a instanceof AnonymousAuthenticationToken) {
            // Unauthenticated, try authenticating
            if (map != null && map.containsAttribute("username")
                    && map.containsAttribute("password")) {

                // Username and password supplied, authenticate
                String username = (String) map.get("username");
                String password = (String) map.get("password");
                Subject subject;

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
                a = authProvider.authenticate(token);

                if (a == null) {
                    throw new LoginException("Wrong username/password combination");
                }

                subject = createSubject(a);
                SecurityContextHolder.getContext().setAuthentication(a);

                context.setSubject(subject);
                return;
            }
        } else {
            // Authenticated, create a subject
            context.setSubject(createSubject(a));
            return;
        }

        // Unauthenticated, credentials not provided
        context.setSubject(null);
    }

    private Subject createSubject(Authentication a) {
        return new Subject(false,
                           new HashSet<>(Arrays.asList(new UserPrincipal(a.getName()))),
                           new HashSet<>(a.getAuthorities()),
                           new HashSet<>(Arrays.asList(a.getCredentials())));
    }
}
