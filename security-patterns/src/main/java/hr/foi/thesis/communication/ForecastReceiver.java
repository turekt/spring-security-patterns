package hr.foi.thesis.communication;

import hr.foi.thesis.model.City;
import hr.foi.thesis.model.Forecast;
import hr.foi.thesis.repository.CityRepository;
import hr.foi.thesis.security.ObfuscatedTransferObject;
import hr.foi.thesis.util.CipherUtil;
import javax.crypto.Cipher;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import hr.foi.thesis.repository.ForecastRepository;

@Component
public class ForecastReceiver {
    
    private final CityRepository cityRepository;
    private final ForecastRepository forecastDaoRepository;

    @Autowired
    public ForecastReceiver(CityRepository cityRepository, ForecastRepository forecastDaoRepository) {
        this.cityRepository = cityRepository;
        this.forecastDaoRepository = forecastDaoRepository;
    }

    @JmsListener(destination = "forecast")
    public void receiveMessage(Message message) {
        try {
            System.out.println(message);
            ObfuscatedTransferObject obj = (ObfuscatedTransferObject) ((ObjectMessage) message).getObject();
            obj.unseal(CipherUtil.defaultCipher("S3cur1ty-P4tt3rns".toCharArray(), Cipher.DECRYPT_MODE));
            
            Forecast forecast = new Forecast();
            forecast.setId((Long) obj.get("forecastid"));
            forecast.setPressure((Double) obj.get("pressure"));
            forecast.setHumidity((Integer) obj.get("humidity"));
            forecast.setTemp((Double) obj.get("temp"));
            forecast.setTempMax((Double) obj.get("tempmax"));
            forecast.setTempMin((Double) obj.get("tempmin"));
            forecast.setForecastTime((String) obj.get("time"));
            
            System.out.println(forecast);
            City city = this.cityRepository.findOne((Long) obj.get("cityid"));
            
            if(city != null) {
                forecast.setCity(city);
                forecastDaoRepository.save(forecast);
            } else {
                System.out.println("City is null. Ignored.");
            }
        } catch (Exception ex) {
            System.err.println("Failed when parsing JMS message: " + ex.getMessage());
        }
    }
}
 
