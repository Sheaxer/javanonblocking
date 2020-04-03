package stuba.fei.gono.java.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@ResponseBody
public class ReportedOverlimitTransactionException extends RuntimeException{

    public ReportedOverlimitTransactionException(String message)
    {
        super( message);
    }
}
