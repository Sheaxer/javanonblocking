package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;

/***
 * <div class="en">Class implementing Validator interface - checks if date is a valid banking date.
 * Currently any date that is not on weekend is valid.</div>
 * <div class="sk">Trieda ktorá implementuje rozhranie Validator - kontroluje, či
 * dátum je platný bankovný deň. V súčasnosi každý deň ktorý nie je víkendový, je platný.</div>
 */
@Component
public class BankingDayValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Date.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Date d = (Date)o;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_WEEK);
        if((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY))
            errors.reject("INVALID_DATE");
    }
}
