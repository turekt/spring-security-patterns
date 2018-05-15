package hr.foi.thesis.repository;

import hr.foi.thesis.model.City;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends CrudRepository<City, Long> {
    
    @Query(value = "SELECT c FROM Person p JOIN p.cities c WHERE p.credentials.username = :username")
    List<City> findByPersonUsername(@Param(value = "username") String username);
}
