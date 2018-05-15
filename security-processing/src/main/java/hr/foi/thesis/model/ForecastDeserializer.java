package hr.foi.thesis.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ForecastDeserializer extends JsonDeserializer<Forecast> {

    @Override
    public Forecast deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        Forecast forecast = new Forecast();
        forecast.setId(node.get("dt").asLong());
        forecast.setHumidity(node.get("main").get("humidity").asInt());
        forecast.setPressure(node.get("main").get("pressure").asDouble());
        forecast.setTemp(node.get("main").get("temp").asDouble());
        forecast.setTempMax(node.get("main").get("temp_max").asDouble());
        forecast.setTempMin(node.get("main").get("temp_min").asDouble());
        forecast.setTime(LocalDateTime.ofEpochSecond(node.get("dt").asLong(), 0, ZoneOffset.UTC).toLocalDate());
        return forecast;
    }
}
