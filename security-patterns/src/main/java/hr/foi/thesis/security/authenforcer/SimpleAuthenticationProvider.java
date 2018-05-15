package hr.foi.thesis.security.authenforcer;

import hr.foi.thesis.model.Credentials;
import hr.foi.thesis.model.Person;
import hr.foi.thesis.repository.PersonRepository;
import hr.foi.thesis.util.PasswordUtil;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import static hr.foi.thesis.Consts.*;

@Component
public class SimpleAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        String username = a.getName();
        String password = (String) a.getCredentials();
        
        Person p = personRepository.findByCredentialsUsername(username);
        
        if(p != null) {
            Credentials c = p.getCredentials();
            String dbPassword = c.getPassword();
            String b64Pass = PasswordUtil.getEncryptedPassword(password, c.getSalt(), ITERATION_COUNT, KEY_LENGTH);

            if (b64Pass != null && dbPassword.equals(b64Pass)) {
                Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                authorities.add(p.getRole().equals(ROLE_ADMIN_STRING) ? ROLE_ADMIN : ROLE_USER);
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }
        }
        
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
