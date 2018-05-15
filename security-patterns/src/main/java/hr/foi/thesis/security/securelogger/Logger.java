package hr.foi.thesis.security.securelogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

    private LogOutputStream logOutputStream;

    public Logger() {
        createWriter();
    }
    
    public void log(String message) throws IOException {
        try {
            logOutputStream.write((message + System.lineSeparator()).getBytes());
            logOutputStream.flush();
        } catch(LogSizeException e) {
            logOutputStream.close();
            createWriter();
        } catch(IOException e) {
            logOutputStream.close();
            throw new RuntimeException("Failure writing to log " + e.getMessage());
        }
    }

    private void createWriter() {
        try {
            this.logOutputStream = new LogOutputStream();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot initiate logger: " + e.getMessage());
        }
    }
}
