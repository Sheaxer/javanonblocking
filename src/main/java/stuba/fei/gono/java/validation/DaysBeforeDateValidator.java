package stuba.fei.gono.java.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import stuba.fei.gono.java.validation.annotations.DaysBeforeDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Slf4j
public class DaysBeforeDateValidator implements ConstraintValidator<DaysBeforeDate, Date> {

    private Date today;
    private long days;
    @Value("${reportedOverlimitTransaction.daysBefore:3}")
    private long cDays;

    @Override
    public void initialize(DaysBeforeDate constraintAnnotation) {
        this.today = new Date();
        days = (constraintAnnotation.days() == 0 ? cDays : constraintAnnotation.days());
        log.info(String.valueOf(days));
        /*if(days==0)
            days = cDays;*/
        log.info(String.valueOf(days));
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        long diff = date.getTime() - today.getTime();
        //System.out.println(diff);
        log.info(String.valueOf(diff));
        if(diff <0)
            return true;
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= days;
    }
}
