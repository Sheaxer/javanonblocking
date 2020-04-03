package stuba.fei.gono.java.validation;

import stuba.fei.gono.java.validation.annotations.BankingDay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;
public class BankingDayValidator implements ConstraintValidator<BankingDay, Date> {
    @Override
    public void initialize(BankingDay constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {

       Calendar c = Calendar.getInstance();
       c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        if((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY))
            return false;
        return true;
    }
}
