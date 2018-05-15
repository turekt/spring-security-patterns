package hr.foi.thesis.service;

import static hr.foi.thesis.Consts.SECRET_PARAMETER;
import static hr.foi.thesis.Consts.TOKEN_PARAMETER;
import hr.foi.thesis.configuration.CityServiceConfiguration;
import hr.foi.thesis.configuration.ProcessingServiceConfiguration;
import hr.foi.thesis.model.City;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WebApiClient {
    
    private final ProcessingServiceConfiguration processingServiceConfiguration;
    private final CityServiceConfiguration cityServiceConfiguration;
    private final RestTemplate restTemplate;
    private String token;

    @Autowired
    public WebApiClient(CityServiceConfiguration cityServiceConfiguration, ProcessingServiceConfiguration processingServiceConfiguration) {
        this.cityServiceConfiguration = cityServiceConfiguration;
        this.processingServiceConfiguration = processingServiceConfiguration;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
    }

    private void startOrRefreshToken() {
        try {
            ModelMap map = new ModelMap(SECRET_PARAMETER, cityServiceConfiguration.getSecret());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity entity = new HttpEntity(map, headers);
            ResponseEntity<ModelMap> response = restTemplate.exchange(cityServiceConfiguration.getAuthUrl(), 
                    HttpMethod.POST, entity, ModelMap.class);
            System.out.println(response); 
            
            token = (String) response.getBody().get(TOKEN_PARAMETER);
            
        } catch(HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Authentication with web service failed: " + e.getResponseBodyAsString());
        }
    }
    
    private <T> T performApiRequest(String url, HttpMethod method, ParameterizedTypeReference<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<T> response = restTemplate.exchange(url, method, entity, type);
        System.out.println(response);
        token = (String) response.getHeaders().get(TOKEN_PARAMETER).get(0);
        return response.getBody();
    }
    
    public List<City> fetchCities(String name) {
        if(token == null) {
            startOrRefreshToken();
        }
        
        for(int i = 0; i < 3; i++) {
            try {
                String url = String.format("%s/%s", cityServiceConfiguration.getCitySearchUrl(), name);
                return performApiRequest(url, HttpMethod.GET, new ParameterizedTypeReference<List<City>>() {});
            } catch(HttpClientErrorException | HttpServerErrorException e) {
                if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) startOrRefreshToken();
                else throw new RuntimeException("Service is dead. " + e.getMessage());
            }
        }
        
        return null;
    }
    
    public City fetchCity(Long id) {
        if(token == null) {
            startOrRefreshToken();
        }
        
        for(int i = 0; i < 3; i++) {
            try {
                String url = String.format("%s/%d", cityServiceConfiguration.getCityUrl(), id);
                return performApiRequest(url, HttpMethod.GET, new ParameterizedTypeReference<City>() {});
            } catch(HttpClientErrorException | HttpServerErrorException e) {
                if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) startOrRefreshToken();
                else throw new RuntimeException("Service is dead. " + e.getMessage());
            }
        }
        return null;
    }
    
    private HttpEntity basicAuthentication() {
        String format = String.format("%s:%s", processingServiceConfiguration.getUsername(), processingServiceConfiguration.getPassword());
        String b64password = Base64.getEncoder().encodeToString(format.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s", b64password));
        return new HttpEntity(headers);
    }
    
    public ResponseEntity changeStatus(Long id) {
        RestTemplate template = new RestTemplate();
        String url = String.format("%s/%d", processingServiceConfiguration.getStatusUrl(), id);
        ResponseEntity response = template.exchange(url, HttpMethod.GET,
                basicAuthentication(), String.class);
        System.out.println(response);
        
        return response;
    }
    
    public List<City> fetchProcessingCities() {        
        ResponseEntity<List<City>> response = restTemplate.exchange(processingServiceConfiguration.getCityUrl(), HttpMethod.GET,
                basicAuthentication(), new ParameterizedTypeReference<List<City>>() {});
        System.out.println(response);
        
        return response.getBody();
    }
}
