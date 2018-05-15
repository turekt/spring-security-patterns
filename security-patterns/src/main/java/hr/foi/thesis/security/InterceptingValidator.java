package hr.foi.thesis.security;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class InterceptingValidator {

    public <T> Set<ConstraintViolation<T>> validate(T object) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return validator.validate(object);
    }
}
