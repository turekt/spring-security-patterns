package hr.foi.thesis.controller;

import hr.foi.thesis.communication.CitySender;
import hr.foi.thesis.model.City;
import hr.foi.thesis.repository.CityRepository;
import hr.foi.thesis.security.ObfuscatedTransferObject;
import hr.foi.thesis.security.RequestContext;
import hr.foi.thesis.security.SecureBaseAction;
import hr.foi.thesis.service.WebApiClient;
import hr.foi.thesis.util.CipherUtil;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.crypto.Cipher;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    
    private final WebApiClient client;
    private final SecureBaseAction secureBaseAction;
    private final CitySender sender;
    private final CityRepository cityRepository;
    
    @Autowired
    public AdminController(WebApiClient client, SecureBaseAction secureBaseAction, CitySender sender, CityRepository cityRepository) {
        this.client = client;
        this.secureBaseAction = secureBaseAction;
        this.sender = sender;
        this.cityRepository = cityRepository;
    }
    
    @GetMapping(value = "/search")
    public String cities(@ModelAttribute City city, RequestContext context) throws Exception {
        return "admin";
    }
    
    @PostMapping(value = "/search")
    public String search(@ModelAttribute City city, RequestContext context) throws Exception {
        context.setData(city);
        Set<ConstraintViolation<City>> violations = secureBaseAction.request(context);
        
        if(violations.isEmpty()) {
            List<City> cities = client.fetchCities(city.getName());
            List<City> processedCities = client.fetchProcessingCities();

            for(City c : cities) {
                for(City p : processedCities) {
                    if(Objects.equals(p.getId(), c.getId())) {
                        c.setProcessed(true);
                        break;
                    }
                }
            }

            context.getModelMap().addAttribute("cities", cities);
        }
        
        context.getModelMap().addAttribute("violations", violations);
        return "admin";
    }
    
    @PostMapping(value = "/state")
    public String search(@ModelAttribute City city, @RequestParam(name = "cityid") Long id,
            @RequestParam(name = "cityname") String name, @RequestParam(name = "citylat") String lat,
            @RequestParam(name = "citylon") String lon, RequestContext context) throws Exception {
        if(checkCity(id, name, lat, lon)) {
            ObfuscatedTransferObject oto = new ObfuscatedTransferObject();
            oto.set("cityid", id);
            oto.set("cityname", name);
            oto.set("citylat", lat);
            oto.set("citylon", lon);
            oto.seal(CipherUtil.defaultCipher("S3cur1ty-P4tt3rns".toCharArray(), Cipher.ENCRYPT_MODE), "id", "city");
            sender.send(oto);

            context.getModelMap().addAttribute("state", Boolean.TRUE);
            context.getModelMap().addAttribute("name", name);
            
        } else {
            context.getModelMap().addAttribute("error", true);
        }
        
        return "admin";
    }
    
    @GetMapping(value = "/process")
    public String process(RequestContext context) {
        List<City> processedCities = client.fetchProcessingCities();
        context.getModelMap().addAttribute("cities", processedCities);
        return "process";
    }
    
    @PostMapping(value = "/process")
    public String process(@RequestParam(name = "cityid") Long id, RequestContext context) {
        ResponseEntity response = client.changeStatus(id);
        if(response.getStatusCode().is4xxClientError()) {
            context.getModelMap().addAttribute("success", false);
        } else {
            List<City> processedCities = client.fetchProcessingCities();
            context.getModelMap().addAttribute("cities", processedCities);
            context.getModelMap().addAttribute("success", true);
        }
        return "process";
    }
    
    private boolean checkCity(Long id, String... others) {
        City cityOriginal = client.fetchCity(id);
        String name = others[0];
        boolean coords = true;
        if(others.length > 1) {
            coords = checkCityCoords(cityOriginal, others[1], others[2]);
        }
        return Objects.equals(cityOriginal.getId(), id) && Objects.equals(cityOriginal.getName(), name) && coords;
    }
    
    private boolean checkCityCoords(City city, String lat, String lon) {
        return Objects.equals(String.valueOf(city.getCoord().getLat()), lat)
                && Objects.equals(String.valueOf(city.getCoord().getLon()), lon);
    }
}
