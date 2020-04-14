package stuba.fei.gono.java.nonblocking.errors;

import java.util.ArrayList;
import java.util.List;

public class ReportedOverlimitTransactionValidationException extends RuntimeException {

    private List<String> errors;

    public ReportedOverlimitTransactionValidationException(List<String> errors)
    {
        this.errors=errors;
    }

    public ReportedOverlimitTransactionValidationException()
    {
        this.errors = new ArrayList<>();
    }

    public void addError(String error)
    {
        this.errors.add(error);
    }

    public List<String> getErrors()
    {
        return this.errors;
    }

}
