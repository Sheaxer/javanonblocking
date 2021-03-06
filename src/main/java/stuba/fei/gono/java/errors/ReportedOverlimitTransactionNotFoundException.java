package stuba.fei.gono.java.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * <div class="en">Custom Exception used when ReportedOverlimitTransaction entity is not found.</div>
 * <div class="sk">Vlastná výnimka vyvolaná keď entita nebola nájdená.</div>
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@ResponseBody
public class ReportedOverlimitTransactionNotFoundException extends RuntimeException{

    public ReportedOverlimitTransactionNotFoundException(String message)
    {
        super( message);
    }
}
