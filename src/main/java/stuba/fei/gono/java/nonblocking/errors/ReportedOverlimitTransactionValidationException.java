package stuba.fei.gono.java.nonblocking.errors;

import java.util.ArrayList;
import java.util.List;

/***
 * Custom Exception class to be thrown during validation of ReportedOverlimitTransaction entity payload of PUT and
 * POST REST methods.
 * @see stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService
 */
public class ReportedOverlimitTransactionValidationException extends RuntimeException {

    private List<String> errors;

    /***
     * Constructor that creates the exception with list of error messages
     * @param errors list of error messages
     */
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
