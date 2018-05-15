package hr.foi.thesis.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import hr.foi.thesis.configuration.SecurityConfigurationProperties;
import hr.foi.thesis.security.messageinspector.JwtIssuedListener;
import hr.foi.thesis.security.messageinspector.JwtIssuer;
import hr.foi.thesis.security.messageinspector.JwtMessageInspector;
import hr.foi.thesis.util.PasswordUtil;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private JwtIssuer jwtIssuer;
    private JwtMessageInspector inspector;
    private SecurityConfigurationProperties securityProperties;

    @Autowired
    public AuthenticationController(SecurityConfigurationProperties securityProperties, JwtMessageInspector inspector,
            JwtIssuer jwtIssuer) {
        this.securityProperties = securityProperties;
        this.inspector = inspector;
        this.jwtIssuer = jwtIssuer;
    }
    
    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody ModelMap map) {
        String secret = (String) map.get("secret");
        if(secret == null || secret.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        
        String derived = PasswordUtil.getEncryptedPassword(secret, securityProperties.getSalt(),
                securityProperties.getIterationCount(), securityProperties.getKeyLength());
        
        if(!securityProperties.getPreSharedKey().equals(derived)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        
        try {
            String token = jwtIssuer.issue();
            ModelMap jwt = new ModelMap("token", token);
            return new ResponseEntity(jwt, HttpStatus.OK);
            
        } catch (JWTCreationException | UnsupportedEncodingException exception){
            return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
