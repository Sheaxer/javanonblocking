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
 * Class that implements custom error handling.
 */
@RestControllerAdvice
public class ErrorHandler {
    /***
     * Handles ReportedOverlimitTransactionNotFoundException  by returning the error code and sending HTTP code
     * NOT_FOUND - 404.
     * @param ex caught exception.
     * @return Mono emitting the list containing the error message of ex.
     */
    @ExceptionHandler(ReportedOverlimitTransactionNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<List<String>> springHandleNotFound(Exception ex) {
        return Mono.just(new ArrayList<>(Collections.singleton(ex.getMessage())));
    }
    /***
     * Handles ReportedOverlimitTransactionBadRequestException by returning the error code and sending HTTP code
     * BAD_REQUEST - 400.
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
     * @param e exception containing errors that were discovered during validation
     * @return Mono from List of errors discovered during validation
     *
     */
    @ExceptionHandler(ReportedOverlimitTransactionValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> handleReportedOverlimitTransactionValidationException(ReportedOverlimitTransactionValidationException e)
    {
        return Mono.just(
                        e.getErrors()
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
