package hr.foi.thesis.repository;

import hr.foi.thesis.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    
    Person findByCredentialsUsername(String username);
}
