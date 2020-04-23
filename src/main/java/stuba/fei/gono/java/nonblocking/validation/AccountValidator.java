package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.AccountNO;

/***
 * Class implementing validation of AccounNO - must have either a IBAN or Local Account Number, otherwise the validator
 * rejects the AccountNO with error message "INVALID_ACCOUNT".
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
            if((account.getBic() == null) || (account.getBic().isEmpty())
                    || (account.getLocalAccountNumber() == null) || (account.getLocalAccountNumber().isEmpty()))
            {
                errors.reject("INVALID_ACCOUNT");
            }

        }
    }
}
