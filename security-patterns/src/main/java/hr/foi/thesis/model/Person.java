package hr.foi.thesis.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    @Valid
    private Credentials credentials;

    @NotNull
    @Pattern(message = "User role should be ROLE_ADMIN or ROLE_USER", regexp = "(ROLE_ADMIN|ROLE_USER)")
    private String role;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="person_cities",
            joinColumns={ @JoinColumn(name="cityId") },
            inverseJoinColumns={ @JoinColumn(name="personId") })
    private List<City> cities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
