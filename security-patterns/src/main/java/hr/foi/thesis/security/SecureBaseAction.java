package hr.foi.thesis.security;

import com.sun.security.auth.UserPrincipal;
import hr.foi.thesis.security.authenforcer.AuthenticationEnforcer;
import hr.foi.thesis.security.authenforcer.AuthorizationEnforcer;
import hr.foi.thesis.security.securelogger.SecureLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import java.security.PrivilegedActionException;
import java.util.Set;

@Component
public class SecureBaseAction {

    @Autowired
    private AuthenticationEnforcer authenticationEnforcer;

    @Autowired
    private AuthorizationEnforcer authorizationEnforcer;

    @Autowired
    private InterceptingValidator interceptingValidator;

    @Autowired
    private SecureLogger secureLogger;

    public <T> Set<ConstraintViolation<T>> request(RequestContext<T> context)
            throws LoginException, PrivilegedActionException {
        // Authentication
        authenticationEnforcer.authenticate(context);

        // Log subject
        Subject subject = context.getSubject();
        String name = subject != null ? ((UserPrincipal) subject.getPrincipals().toArray()[0]).getName() : "unknown";
        String message = String.format("Subject %s has made a request to %s from %s",
                                       name,
                                       context.getRequest().getRequestURI(),
                                       context.getRequest().getRemoteAddr());
        secureLogger.log(message);

        // Authorization
        if(authorizationEnforcer.isAuthorized(context)) {

            // Try validation if data exists
            T data = context.getData();
            if(data != null) {
                secureLogger.log(name + " sent request data");
                Set<ConstraintViolation<T>> violations = interceptingValidator.validate(data);
                secureLogger.log(name + " had violations " + violations.toString());
                return violations;
            }

            return null;
        }

        // Authorization fails
        throw new PrivilegedActionException(new Exception("Unauthorized action"));
    }
}
