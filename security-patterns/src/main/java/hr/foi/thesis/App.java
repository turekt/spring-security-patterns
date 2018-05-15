package hr.foi.thesis;

import hr.foi.thesis.security.securelogger.SecureLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Scanner;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class App {

    private static char[] readPassword(String message) {
        if(System.console() == null) {
            // IDE purposes
            System.out.println(message);
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine().toCharArray();

        } else {
            return System.console().readPassword(message);
        }
    }

    public static void main(String... args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public SecureLogger getSecureLogger() {
//        SecureLogger secureLogger = new SecureLogger(readPassword("Input encryption key:"),
//                readPassword("Input keystore password:"));
        SecureLogger secureLogger = new SecureLogger("3ncrypt3d".toCharArray(), "Very hard password".toCharArray());
        return secureLogger;
    }
}
