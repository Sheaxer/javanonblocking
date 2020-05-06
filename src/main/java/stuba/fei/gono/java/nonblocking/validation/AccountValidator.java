package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.AccountNO;

/***
 * <div class="en">Class implementing validation of AccountNO - must have either a IBAN or Local Account Number,
 * otherwise the validator rejects the AccountNO with error message "INVALID_ACCOUNT".</div>
 * <div class="sk">Trieda implementuje validáciu objektu triedy AccountNO - musí obsahovať
 * buď IBAN alebo lokálne číslo účtu, inak validátor zamietne objekt s chybovou správou "INVALID_ACCOUNT".</div>
 */
@Component
public class AccountValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return AccountNO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountNO account = (AccountNO)o;
        if((account.getIban() == null) || (account.getIban().isEmpty()))
        {
            if((account.getLocalAccountNumber() == null) || (account.getLocalAccountNumber().isEmpty()))
            {
                errors.reject("SOURCEACCOUNT_INVALID");
            }

        }
    }
}
