package hr.foi.thesis.security.auditinterceptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

public class AuditLog {
    
    private static final String AUDIT_FORMAT = "%-30s %-30s %-30s %-30s %-30s %-30s\n";
    private FileOutputStream stream;
    
    public AuditLog() {
        try {
            File file = new File("audit.log");
            if(!file.exists()) {
                this.stream = new FileOutputStream(file, true);
                this.log(String.format(AUDIT_FORMAT, "Type", "Method", "URI",
                        "Remote address", "Query", "Port/Status"));
            }
            this.stream = new FileOutputStream(file, true);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Cannot create AuditLog writer: " + ex.getMessage());
        }
    }
    
    public final void log(String message) {
        try {
            stream.write(message.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot write to AuditLog: " + ex.getMessage());
        }
    }

    public void log(String request, String method, String uri, String remoteAddr, String queryString,
            int value) {
        log(String.format(AUDIT_FORMAT, request, method, uri, remoteAddr, queryString,
                String.valueOf(value)));
    }
}
