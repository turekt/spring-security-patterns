package hr.foi.thesis.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class Coordinates implements Serializable {
    
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
