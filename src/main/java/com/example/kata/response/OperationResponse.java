package com.example.kata.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * Objet réponse pour une opération réussie
 */
@Data
@AllArgsConstructor
public class OperationResponse {

    private String operationMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object body;

    public OperationResponse(String operationMessage) {
        this.operationMessage = operationMessage;
    }
}
