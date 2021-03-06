package stuba.fei.gono.java.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * <div class="en">Custom exception when cannot perform operation on ReportedOverlimitTransaction entity.</div>
 * <div class="sk">Vlastná výnimka vyvolaná v prípade, že nebolo možné vykonať operáciu nad entitou.</div>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public class ReportedOverlimitTransactionBadRequestException extends RuntimeException {
    public ReportedOverlimitTransactionBadRequestException(String message)
    {
        super(message);
    }
}