package stuba.fei.gono.java.validation;

import stuba.fei.gono.java.pojo.Account;
import stuba.fei.gono.java.validation.annotations.ValidAccount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountValidator implements ConstraintValidator<ValidAccount, Account> {

    @Override
    public void initialize(ValidAccount constraintAnnotation) {

    }

    @Override
    public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {

        if((account.getIban() == null) || (account.getIban().isEmpty()))
        {
                if((account.getBic() == null) || (account.getBic().isEmpty())
                || (account.getLocalAccountNumber() == null) || (account.getLocalAccountNumber().isEmpty()))
                {
                    return false;
                }
                return true;
        }
        return true;

    }
}
