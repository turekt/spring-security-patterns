package hr.foi.thesis;

import hr.foi.thesis.communication.ForecastScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class App {
    
    public static void main(String... args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    ForecastScheduledTask task;

    @Bean
    public CommandLineRunner runner() {
        return args -> task.fetchForecast();
    }
}
