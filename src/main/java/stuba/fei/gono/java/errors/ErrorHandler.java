package stuba.fei.gono.java.errors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

/***
 * Class that implements custom error handling
 */
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ReportedOverlimitTransactionException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<String> springHandleNotFound(Exception ex) {
        return new ArrayList<>(Collections.singleton(ex.getMessage()));
    }


    /***
     * Transforms validation errors into JSON array
     * @param ex caught validation exception
     * @return List of validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ReportedOverlimitTransactionValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleReportedOverlimitTransactionValidationException(ReportedOverlimitTransactionValidationException e)
    {
        return e.getErrors();
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
