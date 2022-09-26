package ar.edu.unq.desapp.grupog.criptop2p.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleInvalidRequestBody(MethodArgumentNotValidException exception) {

        return exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(groupingBy(FieldError::getField, mapping(DefaultMessageSourceResolvable::getDefaultMessage, toList())));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyTakenException.class)
    public String handleEmailAlreadyTaken(EmailAlreadyTakenException exception) {
        return exception.getMessage();
    }

}
