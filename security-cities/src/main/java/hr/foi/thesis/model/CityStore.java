package hr.foi.thesis.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CityStore {
    
    private static final String CITY_FILE_PATH = "src/main/resources/city.list.json";
    
    private final List<City> cities;
    
    public CityStore() {
        cities = readCities(CITY_FILE_PATH);
    }

    private List<City> readCities(String path) {
        try {
            InputStream stream = new FileInputStream(new File(path));
            TypeReference<List<City>> type = new TypeReference<List<City>>() {};
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(stream, type);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read cities: " + ex.getMessage());
        }
    }
    
    public List<City> search(String name) {
        return cities.stream()
                     .filter((City t) -> { return t.getName().toLowerCase().startsWith(name.toLowerCase()); })
                     .collect(Collectors.toList());
    }

    public City findById(Long cityId) {
        return cities.stream()
                     .filter((City t) -> { return Objects.equals(t.getId(), cityId); })
                     .findFirst()
                     .get();
    }
}
