package stuba.fei.gono.java.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;

@Component
public class ReportedOverlimitTransactionValidator implements Validator {

    final
    FluxAccountValidator accountValidator;
    final
    TransferDateValidator transferDateValidator;

    public ReportedOverlimitTransactionValidator(FluxAccountValidator accountValidator, TransferDateValidator transferDateValidator) {
        this.accountValidator = accountValidator;
        this.transferDateValidator = transferDateValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ReportedOverlimitTransaction.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors,"clientId", "CLIENTID_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"orderCategory","ORDERCATEGORY_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"sourceAccount","SOURCEACCOUNT_INVALID");
        if(((ReportedOverlimitTransaction)o).getSourceAccount() != null)
            ValidationUtils.invokeValidator(accountValidator,((ReportedOverlimitTransaction) o).getSourceAccount(),errors);
        ValidationUtils.rejectIfEmpty(errors,"clientId","CLIENTID_NOT_VALID");
        ValidationUtils.rejectIfEmpty(errors,"amount","FIELD_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"vault","VAULT_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"transferDate","INVALID_DATE");
        if(((ReportedOverlimitTransaction)o).getTransferDate()!=null)
            ValidationUtils.invokeValidator(transferDateValidator,((ReportedOverlimitTransaction) o).getTransferDate(),errors);
        ValidationUtils.rejectIfEmpty(errors,"createdBy","CREATEDBY_NOT_VALID");
        ValidationUtils.rejectIfEmpty(errors,"organisationUnitID","ORGANISATIONUNITID_NOT_VALID");
    }
}
