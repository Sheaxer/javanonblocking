package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Employee;

/***
 * <div class="en">Class implementing the Validator interface - checks if the Employee instance contains
 * both user name and password. Rejects the object with error code "USERNAME_INVALID" if the user name is missing
 * and "PASSWORD_INVALID" if password is missing.</div>
 * <div class="sK">Trieda implementuje rozhranie Validator - skontroluje, či inštancia triedy Employee
 * obsahuje zároveň používateľské meno a heslo. Odmietne inštanciu s chybovým kódom "USERNAME_INVALID"
 * ak inštancia neobsahuje používateľské meno a kód "PASSWORD_INVALID" ak chýba heslo.</div>
 */
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
