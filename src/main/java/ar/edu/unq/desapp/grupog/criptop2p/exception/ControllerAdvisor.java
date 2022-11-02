package ar.edu.unq.desapp.grupog.criptop2p.exception;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.InvalidConsultationDatesException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestControllerAdvice
public class ControllerAdvisor {

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraints(ConstraintViolationException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MarketOrderException.class)
    public String handleInvalidMarketOrder(MarketOrderException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionOrderException.class)
    public String handleInvalidTransactionOrder(TransactionOrderException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidConsultationDatesException.class)
    public String handleInvalidConsultationDates(InvalidConsultationDatesException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PriceExceedsOperationLimitException.class)
    public String handlePriceExceedsOperationLimitException(PriceExceedsOperationLimitException exception) {
        return exception.getMessage();
    }

    // Telecentro moment
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ConnectException.class)
    public String handleConnectionException(ConnectException exception) {
        return exception.getMessage();
    }


}
