package hr.foi.thesis.security.messageinspector;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import hr.foi.thesis.configuration.SecurityConfigurationProperties;
import java.io.UnsupportedEncodingException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

public class JwtMessageInspector implements JwtIssuedListener, MessageHandler {
    
    private static final String JWT_BEARER = "Bearer ";
    
    private SecurityConfigurationProperties securityProperties;
    private String jti;

    public JwtMessageInspector(SecurityConfigurationProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) 
            throws UnsupportedEncodingException, AuthenticationException, JWTVerificationException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null || !authorizationHeader.startsWith(JWT_BEARER)) {
            throw new AuthenticationException("No authentication token provided.");
        }

        String token = authorizationHeader.substring(JWT_BEARER.length());
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getJwtSecret());

        JWTVerifier verifier = JWT.require(algorithm)
                .acceptLeeway(1)
                .acceptExpiresAt(5)
                .withJWTId(jti)
                .withIssuer(securityProperties.getJwtIssuer())
                .build();

        verifier.verify(token);
    }

    @Override
    public void onJwtIssued(String jit) {
        this.jti = jit;
    }
}
