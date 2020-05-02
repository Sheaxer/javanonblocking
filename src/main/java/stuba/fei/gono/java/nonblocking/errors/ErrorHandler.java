package stuba.fei.gono.java.nonblocking.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;

import java.util.*;

/***
 * <div class="en">Class that implements custom error handling.</div>
 * <div class="sk">Trieda ktorá implementuje vlastné spravocanie výnimiek.</div>
 */
@RestControllerAdvice
public class ErrorHandler {
    /***
     * <div class="en">Handles ReportedOverlimitTransactionNotFoundException
     * by returning the error code and sending HTTP code NOT_FOUND - 404.</div>
     * <div class="sk">Spracováva ReportedOverlimitTransactionNotFoundException výnimku
     * vrátením HTTP kódu 404 Not Found a chybovým kódom v tele správy.</div>
     * @param ex <div class="en">caught exception.</div>
     *           <div class="sk">odchytená výnimka.</div>
     * @return <div class="en">Mono emitting the list containing the error message of ex.</div>
     * <div class="sk">Mono emitujúce zoznam chybných kódov v odchytenej výnimke.</div>
     */
    @ExceptionHandler(ReportedOverlimitTransactionNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<List<String>> springHandleNotFound(Exception ex) {
        return Mono.just(new ArrayList<>(Collections.singleton(ex.getMessage())));
    }
    /***
     * <div class="en">Handles ReportedOverlimitTransactionBadRequestException by returning
     * the error code and sending HTTP code BAD_REQUEST - 400.</div>
     * <div class="sk"></div>
     * @param ex caught exception.
     * @return Mono emitting the list containing the error message of ex.
     */
    @ExceptionHandler(ReportedOverlimitTransactionBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> handleBadRequest(ReportedOverlimitTransactionBadRequestException ex)
    {
        return Mono.just(new ArrayList<>(Collections.singleton(ex.getMessage())));
    }

   /* /***
     * Transforms validation errors into JSON array
     * @param ex caught validation exception
     * @return List of validation error messages
     */
   /* @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return Mono.just(
                ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.toList())
               );
    }*/
/*
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> handleMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return Mono.just(ex.getMessage());
    }*/

    /***
     * Method that handles validation errors during put and post REST methods
     * @see ReportedOverlimitTransactionValidationException
     * @see stuba.fei.gono.java.nonblocking.validation.ReportedOverlimitTransactionValidator
     * @param ex exception containing errors that were discovered during validation
     * @return Mono from List of errors discovered during validation
     *
     */
    @ExceptionHandler(ReportedOverlimitTransactionValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> handleReportedOverlimitTransactionValidationException
    (ReportedOverlimitTransactionValidationException ex)
    {
        return Mono.just(
                        ex.getErrors()
        );
        //return e.getErrors();
    }

    /*@ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleMethonNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException e)
    {
        return "METHOD_NOT_ALLOWED";
    }*/

    /*@ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeNotSupported(org.springframework.web.HttpMediaTypeNotSupportedException e)
    {
        return "MEDIATYPE_INVALID";
    }*/

    /*@ExceptionHandler(java.lang.IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(java.lang.IllegalArgumentException e) {
        return "ILLEGAL_ARGUMENT";
    }*/
}
