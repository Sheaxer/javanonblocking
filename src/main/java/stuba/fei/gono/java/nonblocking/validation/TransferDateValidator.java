package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Component
public class TransferDateValidator implements Validator {

    @Value("${reportedOverlimitTransaction.daysBefore:3}")
    private long cDays;

    @Override
    public boolean supports(Class<?> aClass) {
        return Date.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Date today = new Date();
        Date date = (Date) o;
        if(date.before(today))
            errors.reject("INVALID_DATE_IN_PAST");
        Instant i1 = today.toInstant().truncatedTo(ChronoUnit.DAYS);
        Instant i2 = date.toInstant().truncatedTo(ChronoUnit.DAYS);
        if(i1.equals(i2))
            errors.reject("INVALID_DATE_IN_PAST");
        long diff = date.getTime() - today.getTime();
        if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < cDays)
            errors.reject("FIELD_INVALID_TOO_NEAR_IN_FUTURE");
    }
}
