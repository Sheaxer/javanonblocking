package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Money;

/***
 * <div class="en">Class implementing the validation of Amount of money on ReportedOverlimitTransaction.
 * Property amount must be greater than
 * 0 and less than maxAmount or validator rejects with error code "FIELD_INVALID", and
 * the amount must be less than limit or validator rejects with error code "LIMIT_EXCEEDED". </div>
 * <div class="sk">Trieda implementuje Validator rozhranie a validáciu inštancií triedy Money.
 * Premenná amount musí byť viac než 0 a menej než hodnota maxAmount - inak validátor zamietne inštanciu s chybovým
 * kódom "FIELD_INVALID" a amount musí byť menej než hodnota limit - inak validátor
 * zamietne inštanciu s chybovám kódom "LIMIT_EXCEEDED".</div>
 */
@Component
public class MoneyValidator implements Validator {
    /***
     * <div class="en">Limit of amount in ReportedOverlimitTransaction, retrieved from property
     * - reportedOverlimitTransaction.limit, default 999999999.99</div>
     * <div class="sk">Limit množstva peňazí v ReportedOverlimitTransaction, získané z atribút
     *  reportedOverlimitTransaction.limit, predvolená hodnota 999999999.99.</div>
     */
    @Value("${reportedOverlimitTransaction.limit:999999999.99}")
    private double limit;

    /***
     * <div class="en">Max amount used in ReportedOverlimitTransaction, retrieved from  property
     * - reportedOverlimitTransaction.maxAmount, default 999999999.99</div>
     * <div class="sk">Maximálna možná hodnota peňazí v objekte triedy ReportedOverlimitTransaction,
     * získané z atribútú reportedOverlimitTransaction.maxAmount s predvolenou hodnotou 999999999.99.</div>
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
