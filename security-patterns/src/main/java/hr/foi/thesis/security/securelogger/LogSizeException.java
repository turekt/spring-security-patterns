package hr.foi.thesis.security.securelogger;

import java.io.IOException;

public class LogSizeException extends IOException {

    public LogSizeException() {
    }

    public LogSizeException(String message) {
        super(message);
    }

    public LogSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogSizeException(Throwable cause) {
        super(cause);
    }
}
