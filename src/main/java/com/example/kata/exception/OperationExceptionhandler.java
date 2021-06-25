package com.example.kata.exception;

import com.example.kata.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * Exception handler pour retourner des messages personnalisés par rapport aux exceptions
 *
 */
@ControllerAdvice(basePackages ="com.example.kata")
@Slf4j
public class OperationExceptionhandler {

    @ExceptionHandler(SoldeInsuffisantException.class)
    public ResponseEntity handleSoldeInsuffisantException(final SoldeInsuffisantException ex) {
        log.error("Error occured {}", ex.getMessage());
        return new ResponseEntity(new ErrorResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity handleAccountNotFoundException(final AccountNotFoundException ex) {
        log.error("Error occured {}", ex.getMessage());
        return new ResponseEntity(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(final IllegalArgumentException ex) {
        log.error("Error occured {}", ex.getMessage());
        return new ResponseEntity(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
