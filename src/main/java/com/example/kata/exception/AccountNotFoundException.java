package com.example.kata.exception;

/**
 *
 * Custom class exception pour un compte non trouvé
 */
public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(String message) {
        super(message);
    }
}
