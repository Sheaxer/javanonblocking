package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;

/***
 * Class implementing validator of valid banking date. Currently any date that is not on weekend is valid.
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
