package hr.foi.thesis.communication;

import hr.foi.thesis.security.ObfuscatedTransferObject;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class CitySender {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    public void send(ObfuscatedTransferObject oto) {
        jmsTemplate.send("city", (Session sn) -> {
            try {
                ObjectMessage message = sn.createObjectMessage(oto);
                System.out.println(message);
                return message;
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
            return null;
        });
    }
}
