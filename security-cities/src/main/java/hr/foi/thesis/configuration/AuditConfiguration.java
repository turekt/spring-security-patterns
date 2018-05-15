package hr.foi.thesis.configuration;

import hr.foi.thesis.security.auditinterceptor.AuditInterceptor;
import hr.foi.thesis.security.auditinterceptor.AuditLog;
import hr.foi.thesis.security.auditinterceptor.EventCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfiguration {
    
    @Autowired
    private EventCatalog catalog;
    
    @Bean
    public FilterRegistrationBean filterRegistrationBean(AuditInterceptor auditInterceptor) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(auditInterceptor);
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
    
    @Bean
    public AuditInterceptor auditInterceptor(AuditLog auditLog) {
        return new AuditInterceptor(catalog, auditLog);
    }
    
    @Bean
    public AuditLog auditLog() {
        return new AuditLog();
    }
}
