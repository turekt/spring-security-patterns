package hr.foi.thesis.communication;

import javax.crypto.Cipher;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import hr.foi.thesis.configuration.SecurityPropertiesConfiguration;
import hr.foi.thesis.model.City;
import hr.foi.thesis.model.Coordinates;
import hr.foi.thesis.repositories.CityRepository;
import hr.foi.thesis.security.ObfuscatedTransferObject;
import hr.foi.thesis.util.CipherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CityReceiver {

    private final SecurityPropertiesConfiguration securityPropertiesConfiguration;
    private final CityRepository cityRepository;

    @Autowired
    public CityReceiver(SecurityPropertiesConfiguration securityPropertiesConfiguration, CityRepository cityRepository) {
        this.securityPropertiesConfiguration = securityPropertiesConfiguration;
        this.cityRepository = cityRepository;
    }

    @JmsListener(destination = "city")
    public void receive(Message message) {
        try {
            System.out.println(message);
            ObfuscatedTransferObject obj = (ObfuscatedTransferObject) ((ObjectMessage) message).getObject();
            obj.unseal(CipherUtil.defaultCipher(securityPropertiesConfiguration.getCipherPassword().toCharArray(), Cipher.DECRYPT_MODE));
            
            Coordinates coord = new Coordinates();
            coord.setLat(Double.parseDouble((String) obj.get("citylat")));
            coord.setLon(Double.parseDouble((String) obj.get("citylon")));

            City city = new City();
            city.setId((Long) obj.get("cityid"));
            city.setName((String) obj.get("cityname"));
            city.setCoord(coord);
            city.setStatus(City.ProcessingStatus.RUNNING);
            
            cityRepository.save(city);
        } catch (Exception ex) {
            System.err.println("Failed when parsing JMS message: " + ex.getMessage());
        }
    }
}
