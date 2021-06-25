package com.example.kata.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * DTO pour déposer ou retirer de l'argent sur un compte
 */
@Data
@AllArgsConstructor
public class OperationRequest {

    @NotNull
    @ApiModelProperty(value = "Account number")
    private Integer accountNumber;
    @NotNull
    @ApiModelProperty(value = "Operation amount")
    private Double operationAmount;
}
