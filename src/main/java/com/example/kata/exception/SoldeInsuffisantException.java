package com.example.kata.exception;

/**
 *
 * Custom class exception pour un solde insuffisant en cas de retrait
 */
public class SoldeInsuffisantException extends RuntimeException {

    public SoldeInsuffisantException(String message) {
        super(message);
    }
}
