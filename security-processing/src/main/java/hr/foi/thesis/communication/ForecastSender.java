package hr.foi.thesis.communication;

import hr.foi.thesis.configuration.SecurityPropertiesConfiguration;
import hr.foi.thesis.model.Forecast;
import hr.foi.thesis.security.ObfuscatedTransferObject;
import hr.foi.thesis.util.CipherUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@Component
public class ForecastSender {

    private final JmsTemplate template;
    private final SecurityPropertiesConfiguration securityPropertiesConfiguration;

    @Autowired
    public ForecastSender(SecurityPropertiesConfiguration securityPropertiesConfiguration, ActiveMQConnectionFactory connectionFactory) {
        this.template = new JmsTemplate();
        this.template.setConnectionFactory(connectionFactory);
        this.securityPropertiesConfiguration = securityPropertiesConfiguration;
    }

    public void send(Forecast forecast, Long cityId) {
        ObfuscatedTransferObject oto = new ObfuscatedTransferObject();
        oto.set("forecastid", forecast.getId());
        oto.set("temp", forecast.getTemp());
        oto.set("tempmax", forecast.getTempMax());
        oto.set("tempmin", forecast.getTempMin());
        oto.set("humidity", forecast.getHumidity());
        oto.set("pressure", forecast.getPressure());
        oto.set("time", forecast.getTime().toString());
        oto.set("cityid", cityId);

        try {
            oto.seal(CipherUtil.defaultCipher(securityPropertiesConfiguration.getCipherPassword().toCharArray(), Cipher.ENCRYPT_MODE));
            template.send("forecast", (Session sn) -> {
                try {
                    ObjectMessage message = sn.createObjectMessage(oto);
                    System.out.println(message);
                    return message;
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                return null;
            });
        } catch(Exception e) {
            System.err.println("Failed when parsing JMS message: " + e.getMessage());
        }
    }
}
