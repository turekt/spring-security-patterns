package hr.foi.thesis.controller;

import hr.foi.thesis.model.City;
import hr.foi.thesis.model.Coordinates;
import hr.foi.thesis.model.Forecast;
import hr.foi.thesis.model.Person;
import hr.foi.thesis.repository.CityRepository;
import hr.foi.thesis.repository.PersonRepository;
import hr.foi.thesis.security.RequestContext;
import hr.foi.thesis.service.WebApiClient;
import java.security.PrivilegedActionException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.security.auth.login.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hr.foi.thesis.repository.ForecastRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    
    private final WebApiClient client;
    private final CityRepository cityRepository;
    private final PersonRepository personRepository;
    private final ForecastRepository forecastDaoRepository;
    
    @Autowired
    public UserController(WebApiClient client, CityRepository cityRepository, PersonRepository personRepository,
            ForecastRepository forecastDaoRepository) {
        this.client = client;
        this.cityRepository = cityRepository;
        this.personRepository = personRepository;
        this.forecastDaoRepository = forecastDaoRepository;
    }
    
    @RequestMapping(value = "/welcome")
    public String welcome(@ModelAttribute City city, RequestContext context) throws LoginException, PrivilegedActionException {
        List<City> cities = cityRepository.findByPersonUsername(getUser());
        if(cities != null && !cities.isEmpty()) {
            city = city.getId() != null ? city : cities.get(0);
            List<Forecast> forecasts = forecastDaoRepository.findByCity(city);
            context.getModelMap().addAttribute("cities", cities);
            context.getModelMap().addAttribute("forecasts", forecasts);
        }
        return "hello";
    }
    
    @GetMapping(value = "/profile")
    public String profile(RequestContext context) throws Exception {
        String username = getUser();
        List<City> cities = cityRepository.findByPersonUsername(username);
        context.getModelMap().addAttribute("cities", cities);
        context.getModelMap().addAttribute("username", username);
        return "profile";
    }
    
    @GetMapping(value = "/cities")
    public String cities(@ModelAttribute City city, RequestContext context) throws Exception {
        processCities(context);
        return "cities";
    }
    
    @PostMapping(value = "/add")
    public String add(@RequestParam(name = "cityid") Long id, @RequestParam(name = "cityname") String name,
            @RequestParam(name = "citylat") double lat, @RequestParam(name = "citylon") double lon, 
            RequestContext context) {
        Coordinates coord = new Coordinates();
        coord.setLat(lat);
        coord.setLon(lon);
        
        City city = new City();
        city.setCoord(coord);
        city.setName(name);
        city.setId(id);
        
        Person p = personRepository.findByCredentialsUsername(getUser());
        p.getCities().add(city);
        personRepository.save(p);
        context.getModelMap().addAttribute("success", true);
        processCities(context);
        return "cities";
    }
    
    private String getUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return (String) a.getPrincipal();
    }
    
    private void processCities(RequestContext context) {
        List<City> processedCities = client.fetchProcessingCities();
        List<City> cities = cityRepository.findByPersonUsername(getUser());
    
        Iterator<City> processIterator = processedCities.iterator();
        while(processIterator.hasNext()) {
            City pc = processIterator.next();
            
            for(City c : cities) {
                if(Objects.equals(pc.getId(), c.getId())) {
                    processIterator.remove();
                    break;
                }
            }
        }
        
        context.getModelMap().addAttribute("cities", processedCities);
    }
}
