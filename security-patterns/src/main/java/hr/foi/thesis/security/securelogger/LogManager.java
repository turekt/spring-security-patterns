package hr.foi.thesis.security.securelogger;

import java.io.IOException;

public class LogManager {
    
    private static final Logger logger = new Logger();
    
    public static void log(String message) {
        try {
            logger.log(message);
        } catch (IOException e) {
            throw new RuntimeException("Failure when closing the log");
        }
    }
}
