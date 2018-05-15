package hr.foi.thesis.security.messageinspector;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;

public class JwtMessageInterceptorGateway extends GenericFilterBean {
    
    private JwtMessageInspector jwtHandler;
    private JwtIssuer issuer;

    public JwtMessageInterceptorGateway(JwtMessageInspector jwtHandler, JwtIssuer issuer) {
        this.jwtHandler = jwtHandler;
        this.issuer = issuer;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        
        try {
            jwtHandler.handle(httpRequest, httpResponse);
            String freshToken = issuer.issue();
            HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(httpResponse);
            wrapper.setHeader("token", freshToken);
            filterChain.doFilter(servletRequest, wrapper);
        } catch (UnsupportedEncodingException exception) {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UTF-8 not supported. " + exception.getMessage());
        } catch (JWTVerificationException exception) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token verification failed. " + exception.getMessage());
        } catch (AuthenticationException ex) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getExplanation());
        }
    }
}
