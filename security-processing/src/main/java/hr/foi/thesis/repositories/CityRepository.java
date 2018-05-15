package hr.foi.thesis.repositories;

import hr.foi.thesis.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {
    
    City findById(Long id);
}
