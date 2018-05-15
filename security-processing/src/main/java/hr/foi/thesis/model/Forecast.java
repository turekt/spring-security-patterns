package hr.foi.thesis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ForecastDeserializer.class)
public class Forecast implements Serializable {

    private final long serialVersionUID = 1L;

    private int appendedForecasts;
    private Long id;
    private LocalDate time;
    private Double temp;
    private Double tempMax;
    private Double tempMin;
    private Double pressure;
    private Integer humidity;

    public Forecast() {
        temp = 0D;
        tempMax = 0D;
        tempMin = 0D;
        pressure = 0D;
        humidity = 0;
        appendedForecasts = 0;
    }

    public Forecast(LocalDate date, Long cityId) {
        this();
        id = date.toEpochDay() + cityId;
        time = date;
    }

    public void append(Forecast forecast) {
        this.temp += forecast.getTemp();
        this.tempMin += forecast.getTempMin();
        this.tempMax += forecast.getTempMax();
        this.humidity += forecast.getHumidity();
        this.pressure += forecast.getPressure();
        this.appendedForecasts++;
    }

    public void average() {
        this.temp /= appendedForecasts;
        this.tempMin /= appendedForecasts;
        this.tempMax /= appendedForecasts;
        this.humidity /= appendedForecasts;
        this.pressure /= appendedForecasts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", temp=" + temp +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}
