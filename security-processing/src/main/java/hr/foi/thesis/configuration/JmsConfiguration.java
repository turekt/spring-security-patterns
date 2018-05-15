package hr.foi.thesis.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import java.util.Arrays;

@Configuration
public class JmsConfiguration {
    
    @Bean
    public ActiveMQConnectionFactory factoryOne() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setTrustedPackages(Arrays.asList("hr.foi.thesis.communication"));
        factory.setTrustAllPackages(true);
        return factory;
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService broker(ActiveMQConnectionFactory factoryOne) throws Exception {
        final BrokerService broker = new BrokerService();
        broker.addConnector(factoryOne.getBrokerURL());
        broker.setPersistent(false);
        return broker;
    }
    
    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factoryOne) {
        JmsTemplate template = new JmsTemplate(factoryOne);
        template.setPubSubDomain(true);
        return template;
    }
}
