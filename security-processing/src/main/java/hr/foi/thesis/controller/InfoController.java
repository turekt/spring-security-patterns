package hr.foi.thesis.controller;

import hr.foi.thesis.model.City;
import hr.foi.thesis.repositories.CityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hr.foi.thesis.model.City.ProcessingStatus.RUNNING;
import static hr.foi.thesis.model.City.ProcessingStatus.STOPPED;

@RestController
@RequestMapping(value = "/info")
public class InfoController {
    
    private final CityRepository cityRepository;

    @Autowired
    public InfoController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    
    @GetMapping(value = "/city")
    public List<City> cityProcessing() {
        return (List<City>) this.cityRepository.findAll();
    }
    
    @GetMapping(value = "/city/{id}")
    public City checkProcessing(@PathVariable Long id) {
        return this.cityRepository.findById(id);
    }

    @GetMapping(value = "/status/{cityId}")
    public ResponseEntity<City> cityProcessing(@PathVariable(value = "cityId") Long cityId) {
        City city = this.cityRepository.findById(cityId);

        if(city == null) {
            return new ResponseEntity<>(city, HttpStatus.NOT_FOUND);
        }

        City.ProcessingStatus status = city.getStatus();
        city.setStatus(status == RUNNING ? STOPPED : RUNNING);
        cityRepository.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }
}
