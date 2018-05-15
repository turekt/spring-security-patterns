package hr.foi.thesis;

import hr.foi.thesis.security.messageinspector.JwtMessageInterceptorGateway;
import hr.foi.thesis.security.auditinterceptor.AuditInterceptor;
import java.io.File;
import java.io.IOException;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class App {

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }
    
//    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(securePipe());
        return tomcat;
    }

    private Connector securePipe() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = new ClassPathResource("securepipestore.jks").getFile();
            File truststore = new ClassPathResource("securepipestore.jks").getFile();
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(8443);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass("mainpass");
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass("mainpass");
            protocol.setKeyAlias("securepipe");
            return connector;
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot fetch keystore.", ex);
        }
    }
}
