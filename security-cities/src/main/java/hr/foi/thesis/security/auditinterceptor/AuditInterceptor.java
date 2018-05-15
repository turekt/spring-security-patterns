package hr.foi.thesis.security.auditinterceptor;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class AuditInterceptor extends GenericFilterBean {
    
    private EventCatalog catalog;
    private AuditLog auditLog;

    public AuditInterceptor(EventCatalog catalog, AuditLog auditLog) {
        this.catalog = catalog;
        this.auditLog = auditLog;
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        if(catalog.mappingExists(uri, method)) {
            auditLog.log("Request", method, uri, request.getRemoteAddr(), request.getQueryString(), request.getRemotePort());
        }
        
        fc.doFilter(sr, sr1);
        
        if(catalog.mappingExists(uri, method)) {
            HttpServletResponse response = (HttpServletResponse) sr1;
            auditLog.log("Response", method, uri, request.getRemoteAddr(), "-", response.getStatus());
        }
    }
}
