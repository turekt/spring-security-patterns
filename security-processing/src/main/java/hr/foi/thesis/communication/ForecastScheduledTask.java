package hr.foi.thesis.communication;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.foi.thesis.configuration.OpenWeatherMapConfiguration;
import hr.foi.thesis.model.City;
import hr.foi.thesis.model.Forecast;
import hr.foi.thesis.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ForecastScheduledTask {

    private final OpenWeatherMapConfiguration configuration;
    private final CityRepository cityRepository;
    private final RestTemplate restTemplate;
    private final Map<String, String> uriVariables;
    private final ForecastSender sender;

    @Autowired
    public ForecastScheduledTask(OpenWeatherMapConfiguration configuration, CityRepository cityRepository, ForecastSender sender) {
        this.configuration = configuration;
        this.cityRepository = cityRepository;
        this.sender = sender;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));

        uriVariables = new HashMap<>();
        uriVariables.put("APPID", configuration.getApiKey());
        uriVariables.put("units", configuration.getUnits());
        uriVariables.put("lang", configuration.getLang());
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void fetchForecast() throws Exception {
        Iterable<City> cities = cityRepository.findAll();
        for(City city : cities) {
            if(city.getStatus() == City.ProcessingStatus.RUNNING) {
                uriVariables.put("id", String.valueOf(city.getId()));
                System.out.println("Polling for " + city.getId() + " " + city.getName());
                ResponseEntity<JsonNode> response = this.restTemplate.exchange(configuration.getUrl(), HttpMethod.GET,
                        HttpEntity.EMPTY, JsonNode.class, uriVariables);

                JsonNode topLevel = response.getBody();
                JsonNode list = topLevel.get("list");

                ObjectMapper objectMapper = new ObjectMapper();
                JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, Forecast.class);
                List<Forecast> forecasts = objectMapper.readValue(list.toString(), customClassCollection);

                LocalDate tomorrow = LocalDateTime.now().plusDays(1).toLocalDate();
                List<Forecast> relevantForecasts = forecasts.stream()
                        .filter(forecast -> forecast.getTime().isEqual(tomorrow))
                        .collect(Collectors.toList());

                Forecast averageForecast = new Forecast(tomorrow, city.getId());
                relevantForecasts.forEach(forecast -> averageForecast.append(forecast));
                averageForecast.average();

                sender.send(averageForecast, city.getId());
            } else {
                System.out.println("Ignoring " + city.getId() + " " + city.getName());
            }
        }

    }
}
