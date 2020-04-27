package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Money;

/***
 * Class implementing the validation of Amount of money on ReportedOverlimitTransaction. Amount must be greater than
 * 0 and less than maxAmount or validator rejects with error code "FIELD_INVALID", and
 * the amount must be less than limit or validator rejects with error code "LIMIT_EXCEEDED"
 *
 */
@Component
public class MoneyValidator implements Validator {
    /***
     * Limit of amount in ReportedOverlimitTransaction, property
     * - reportedOverlimitTransaction.limit, default 999999999.99
     */
    @Value("${reportedOverlimitTransaction.limit:999999999.99}")
    private double limit;
    /***
     * Max amount used in ReportedOverlimitTransaction, property
     * - reportedOverlimitTransaction.maxAmount, default 999999999.99
     */
    @Value("${reportedOverlimitTransaction.maxAmount:999999999.99}")
    private double maxAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return Money.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Money m = (Money)o;
        if(m.getCurrency() == null)
            errors.reject("CURRENCY_INVALID");
        if(m.getAmount()<=0 || m.getAmount()> this.maxAmount)
            errors.reject("FIELD_INVALID");
        else if(m.getAmount()> this.limit)
            errors.reject("LIMIT_EXCEEDED");
    }
}
