package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Money;
@Component
public class MoneyValidator implements Validator {
    @Value("${reportedOverlimitTransaction.limit:999999999.99}")
    private double limit;
    @Value("${reportedOverlimitTransaction.maxAmount:999999999.99}")
    private double maxAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return Money.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Money m = (Money)o;
        if(m.getAmount()<=0 || m.getAmount()> this.maxAmount)
            errors.reject("FIELD_INVALID");
        if(m.getAmount()> this.limit)
            errors.reject("LIMIT_EXCEEDED");
    }
}
