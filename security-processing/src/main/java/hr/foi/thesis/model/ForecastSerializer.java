package hr.foi.thesis.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ForecastSerializer extends JsonSerializer<Forecast> {

    @Override
    public void serialize(Forecast forecast, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeNumber(forecast.getId());
        jsonGenerator.writeNumber(forecast.getTemp());
        jsonGenerator.writeNumber(forecast.getTempMax());
        jsonGenerator.writeNumber(forecast.getTempMin());
        jsonGenerator.writeNumber(forecast.getHumidity());
        jsonGenerator.writeNumber(forecast.getPressure());
        jsonGenerator.writeString(forecast.getTime().toString());
    }
}
