package stuba.fei.gono.java.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * Custom Exception used when ReportedOverlimitTransaction entity is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@ResponseBody
public class ReportedOverlimitTransactionNotFoundException extends RuntimeException{

    public ReportedOverlimitTransactionNotFoundException(String message)
    {
        super( message);
    }
}
