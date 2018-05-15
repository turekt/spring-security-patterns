package hr.foi.thesis.security.messageinspector;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import hr.foi.thesis.configuration.SecurityConfigurationProperties;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

public class JwtIssuer {
    
    private SecurityConfigurationProperties securityProperties;
    private JwtMessageInspector inspector;

    public JwtIssuer(SecurityConfigurationProperties securityProperties, JwtMessageInspector inspector) {
        this.securityProperties = securityProperties;
        this.inspector = inspector;
    }
    
    public String issue() throws JWTCreationException, UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getJwtSecret());

        String jti = UUID.randomUUID().toString();
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + securityProperties.getJwtDuration()))
                .withIssuer(securityProperties.getJwtIssuer())
                .withJWTId(jti)
                .sign(algorithm);

        inspector.onJwtIssued(jti);
        return token;
    }
}
