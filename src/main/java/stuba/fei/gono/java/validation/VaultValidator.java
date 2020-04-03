package stuba.fei.gono.java.validation;

import stuba.fei.gono.java.pojo.Money;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.pojo.Vault;
import stuba.fei.gono.java.validation.annotations.ValidVault;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VaultValidator implements ConstraintValidator<ValidVault,ReportedOverlimitTransaction> {
    @Override
    public void initialize(ValidVault constraintAnnotation) {

    }

    @Override
    public boolean isValid(ReportedOverlimitTransaction reportedOverlimitTransaction, ConstraintValidatorContext constraintValidatorContext) {

       Money m  = reportedOverlimitTransaction.getAmount();
       double value=0.0;
       for(Vault v: reportedOverlimitTransaction.getVault())
       {
           value+=v.getNominalValue();
       }
        return value == m.getAmount();
    }
}
