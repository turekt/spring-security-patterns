package hr.foi.thesis.controller;

import hr.foi.thesis.model.City;
import hr.foi.thesis.model.CityStore;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/city")
public class CityController {
    
    private CityStore cityStore;

    @Autowired
    public CityController(CityStore cityStore) {
        this.cityStore = cityStore;
    }
    
    @GetMapping(value = "/search/{cityName}")
    public List<City> cities(@PathVariable String cityName) {
        return cityStore.search(cityName);
    }
    
    @GetMapping(value = "/{cityId}")
    public City city(@PathVariable Long cityId) {
        return cityStore.findById(cityId);
    }
}
