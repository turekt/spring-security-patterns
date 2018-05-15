package hr.foi.thesis.repository;

import hr.foi.thesis.model.City;
import hr.foi.thesis.model.Forecast;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ForecastRepository extends CrudRepository<Forecast, Long>{
    
    List<Forecast> findByCity(City city);
}
