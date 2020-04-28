package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Employee;

@Component
public class EmployeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username","USERNAME_INVALID");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","PASSWORD_INVALID");
    }
}
