package hr.foi.thesis.security.messageinspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MessageHandler {
    
    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
