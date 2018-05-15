package hr.foi.thesis.configuration;

import hr.foi.thesis.security.messageinspector.JwtIssuer;
import hr.foi.thesis.security.messageinspector.JwtMessageInspector;
import hr.foi.thesis.security.messageinspector.JwtMessageInterceptorGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {
    
    @Autowired
    private SecurityConfigurationProperties securityConfigurationProperties;
    
    @Bean
    public FilterRegistrationBean jsonWebTokenFilter(JwtMessageInterceptorGateway jwtMessageInterceptorGateway) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(jwtMessageInterceptorGateway);
        bean.addUrlPatterns("/city/*");
        bean.setOrder(2);
        return bean;
    }
    
    @Bean
    public JwtMessageInterceptorGateway jwtMessageInterceptorGateway(JwtMessageInspector jwtMessageHandler, JwtIssuer jwtIssuer) {
        return new JwtMessageInterceptorGateway(jwtMessageHandler, jwtIssuer);
    }
    
    @Bean
    public JwtIssuer jwtIssuer(JwtMessageInspector jwtMessageHandler) {
        return new JwtIssuer(securityConfigurationProperties, jwtMessageHandler);
    }
    
    @Bean
    public JwtMessageInspector jwtMessageHandler() {
        return new JwtMessageInspector(securityConfigurationProperties);
    }
}
