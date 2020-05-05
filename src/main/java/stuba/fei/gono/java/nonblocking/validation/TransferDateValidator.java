package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * <div class="en">Class implementing Validator interface -
 * validation for transferDate field in ReportedOverlimitTransaction.
 * The date must not be the past
 * or on the same day as the day of creating the ReportedOverlimitTransaction or the validator rejects transferDate
 * with error code "INVALID_DATE_IN_PAST". The date also must be at least cDays number of days
 * in the future from the day of creating ReportedOverlimitTransaction or the validator rejects transferDate
 * with error code "FIELD_INVALID_TOO_NEAR_IN_FUTURE".</div>
 * <div class="sk">Trieda implementuje Validator rozhranie - validáciu objektu triedy Date uložený v
 * transferDate atribútu inštancií triedy
 * ReportedOverlimitTransaction. Dátum nesmie byť v minulosti, inak validátor zamietne
 * validáciu s chybovým kódom "INVALID_DATE_IN_PAST". Dátum musí byť taktiež
 * minimále hodnotu cDays počet dní v budúcnosti, inak validátor validáciu zamietne s chybovým kódom
 * "FIELD_INVALID_TOO_NEAR_IN_FUTURE". </div>
 */
@Component
public class TransferDateValidator implements Validator {
    /***
     * <div class="en">Minimal number of days from day of creating ReportedOverlimitTransaction to transfer day.
     * Retrieved from property
     * reportedOverlimitTransaction.daysBefore, default 3.</div>
     * <div class="sk">Minimálny počet dní od dátumu vytvorenia inštancie triedy ReportedOverlimitTransaction
     * do dátumu uvevenom v transferDate atribúte. Hodnota získaná z atribútu
     * reportedOverlimitTransaction.daysBefore s predvolenou hodnotou 3.</div>
     */
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
