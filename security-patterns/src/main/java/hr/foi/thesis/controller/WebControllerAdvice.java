package hr.foi.thesis.controller;

import java.security.PrivilegedActionException;
import javax.security.auth.login.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebControllerAdvice {
    
    @ExceptionHandler(value = LoginException.class)
    public String loginError() {
        return "redirect:/login?error";
    }

    @ExceptionHandler(value = PrivilegedActionException.class)
    public String privilegedError() {
        return "redirect:/login";
    }
}
