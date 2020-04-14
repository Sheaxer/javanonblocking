package stuba.fei.gono.java.nonblocking.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.Account;
@Component
public class FluxAccountValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Account account = (Account)o;
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
