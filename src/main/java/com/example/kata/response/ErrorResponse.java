package com.example.kata.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * Objet réponse en cas d'erreur
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;
}
