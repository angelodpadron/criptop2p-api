package ar.edu.unq.desapp.grupog.criptop2p.exception;

import ar.edu.unq.desapp.grupog.criptop2p.dto.ErrorMessageResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.InvalidConsultationDatesException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessageResponseBody handleInvalidRequestBody(MethodArgumentNotValidException exception) {

        String description = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(". "));

        return generateErrorMessageResponseBody(HttpStatus.BAD_REQUEST, description);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {
            EmailAlreadyTakenException.class,
            PriceExceedsOperationLimitException.class
    })
    public ErrorMessageResponseBody basicConflictStatusExceptionHandler(Exception exception) {
        return generateErrorMessageResponseBody(HttpStatus.CONFLICT, exception.getMessage());

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            ConstraintViolationException.class,
            MarketOrderException.class,
            TransactionOrderException.class,
            InvalidConsultationDatesException.class,
            SymbolNotFoundException.class
    })
    public ErrorMessageResponseBody basicBadRequestStatusExceptionHandler(Exception exception) {
        return generateErrorMessageResponseBody(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // Telecentro moment
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ConnectException.class)
    public ErrorMessageResponseBody handleConnectionException(ConnectException exception) {
        return generateErrorMessageResponseBody(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }

    private ErrorMessageResponseBody generateErrorMessageResponseBody(HttpStatus responseStatus, String details) {
        return new ErrorMessageResponseBody(responseStatus.value(), LocalDateTime.now(), responseStatus.getReasonPhrase(), details);
    }

}
