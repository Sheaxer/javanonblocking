package stuba.fei.gono.java.nonblocking.errors;

import java.util.ArrayList;
import java.util.List;

/***
 * <div class="en">Custom Exception class to be thrown during validation of
 * entity payload of PUT and POST REST methods.</div>
 * <div class="sk">Vlastná výnimka ktorá sa vyvoláva počas validácie entity ktorá je dátovým obsahom
 * požiadavky pre PUT A POST REST metód.</div>
 * @see stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService
 */
public class ReportedOverlimitTransactionValidationException extends RuntimeException {

    private List<String> errors;

    /***
     * <div class="en">Constructor that creates the exception with list of error messages.</div>
     * <div class="sk">Konštruktor ktorý vytvára výnimku pomocou listu chybných hlášok.</div>
     * @param errors <div class="en">list of error messages.</div>
     *               <div class="sk">list chybných hlášok.</div>
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
