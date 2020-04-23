package stuba.fei.gono.java.nonblocking.validation;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.pojo.Currency;
import stuba.fei.gono.java.pojo.OrderCategory;

/***
 * Class implementing validaiton of ReportedOverlimitTransaction according to
 * FENiX - New FrontEnd solution API definition (version 0.1)
 * @see ReportedOverlimitTransaction
 */
@Component
public class ReportedOverlimitTransactionValidator implements Validator {

    private final AccountValidator accountValidator;
    private final TransferDateValidator transferDateValidator;
    private final MoneyValidator moneyValidator;

    public ReportedOverlimitTransactionValidator(AccountValidator accountValidator, TransferDateValidator transferDateValidator,
                                                 MoneyValidator moneyValidator) {
        this.accountValidator = accountValidator;
        this.transferDateValidator = transferDateValidator;
        this.moneyValidator = moneyValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ReportedOverlimitTransaction.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReportedOverlimitTransaction transaction = (ReportedOverlimitTransaction)o;
        ValidationUtils.rejectIfEmpty(errors,"clientId", "CLIENTID_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"orderCategory","ORDERCATEGORY_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"sourceAccount","SOURCEACCOUNT_INVALID");
        if(transaction.getSourceAccount() != null)
            ValidationUtils.invokeValidator(accountValidator,transaction.getSourceAccount(),errors);
        ValidationUtils.rejectIfEmpty(errors,"amount","FIELD_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"vault","VAULT_INVALID");
        ValidationUtils.rejectIfEmpty(errors,"transferDate","INVALID_DATE");
        if(transaction.getTransferDate()!=null)
            ValidationUtils.invokeValidator(transferDateValidator,transaction.getTransferDate(),errors);
        ValidationUtils.rejectIfEmpty(errors,"createdBy","CREATEDBY_NOT_VALID");
        ValidationUtils.rejectIfEmpty(errors,"organisationUnitID","ORGANISATIONUNITID_NOT_VALID");
        if(transaction.getAmount() != null)
            ValidationUtils.invokeValidator(moneyValidator,transaction.getAmount(),errors);
        if(transaction.getOrderCategory() != null && transaction.getAmount() != null && transaction.getAmount().getCurrency() != null)
        {
            if((transaction.getOrderCategory().equals(OrderCategory.FX) && transaction.getAmount().getCurrency().equals(Currency.EUR))||
                    (transaction.getOrderCategory().equals(OrderCategory.DOMESTIC) && !transaction.getAmount().getCurrency().equals(Currency.EUR)))
                errors.reject("CATEGORY_INVALID");
        }



    }
}
