package hr.foi.thesis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "cityService")
public class CityServiceConfiguration {

    private String secret;
    private String serviceUrl;
    private String authUrl;
    private String cityUrl;
    private String citySearchUrl;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getAuthUrl() {
        return serviceUrl + authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getCityUrl() {
        return serviceUrl + cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public String getCitySearchUrl() {
        return serviceUrl + cityUrl + citySearchUrl;
    }

    public void setCitySearchUrl(String citySearchUrl) {
        this.citySearchUrl = citySearchUrl;
    }
}
