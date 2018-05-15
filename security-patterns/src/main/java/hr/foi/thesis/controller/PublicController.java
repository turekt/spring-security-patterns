package hr.foi.thesis.controller;

import hr.foi.thesis.model.Credentials;
import hr.foi.thesis.model.Person;
import hr.foi.thesis.repository.PersonRepository;
import hr.foi.thesis.security.RequestContext;
import hr.foi.thesis.security.SecureBaseAction;
import hr.foi.thesis.util.PasswordUtil;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import java.lang.annotation.ElementType;
import java.security.PrivilegedActionException;
import java.util.Set;

import static hr.foi.thesis.Consts.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class PublicController {

    private final SecureBaseAction secureBaseAction;
    private final PersonRepository personRepository;

    @Autowired
    public PublicController(SecureBaseAction secureBaseAction, PersonRepository personRepository) {
        this.secureBaseAction = secureBaseAction;
        this.personRepository = personRepository;
    }

    @GetMapping(value = "/login")
    public String login(RequestContext context) throws LoginException, PrivilegedActionException {
        if(authenticated()) {
            return "redirect:/welcome";
        }
        
        return "login";
    }

    @PostMapping(value =  "/login")
    public String login(@RequestParam(required = false) String username,
                        @RequestParam(required = false) String password,
                        RequestContext context) throws PrivilegedActionException, LoginException {
        ModelMap map = context.getModelMap();
        map.addAttribute("username", username);
        map.addAttribute("password", password);
        secureBaseAction.request(context);

        return "redirect:/welcome";
    }
    
    @PostMapping(value = "/register")
    public String register(@ModelAttribute Person person, RequestContext<Person> context) throws LoginException, PrivilegedActionException {
        context.setData(person);
        Set<ConstraintViolation<Person>> violations = secureBaseAction.request(context);
        
        if(violations.isEmpty()) {
            // Check if user exists
            Credentials credentials = person.getCredentials();
            Person p = personRepository.findByCredentialsUsername(credentials.getUsername());
            
            if(p == null) {
                // Handle the password
                byte[] salt = PasswordUtil.generateSalt();
                String password = credentials.getPassword();
                credentials.setPassword(PasswordUtil.getEncryptedPassword(password, salt, ITERATION_COUNT, KEY_LENGTH));
                credentials.setSalt(salt);

                personRepository.save(person);
                return "redirect:/register?success";
                
            } else {
                ConstraintViolation<Person> violation = ConstraintViolationImpl.forBeanValidation("Username already exists.",
                        null, "Username already exists.", Person.class, person, null, null, null, null, ElementType.FIELD, null);
                violations.add(violation);
            }
        }
        
        context.getModelMap().addAttribute("violations", violations);
        return "register";
    }
    
    @GetMapping(value = "/register")
    public String registerGet(Person person, RequestContext context) throws LoginException, PrivilegedActionException {
        if(authenticated()) {
            return "redirect:/welcome";
        }
        
        return "register";
    }
    
    private boolean authenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
