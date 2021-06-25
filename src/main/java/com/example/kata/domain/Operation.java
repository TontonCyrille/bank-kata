package com.example.kata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 *
 * Classe matérialisant une opération de dépôt ou de retrait
 */
@Entity
@Table(name = "operations")
@Data
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private Integer id;
    @Column
    private OperationType operationType;
    @Column
    private Double amount;
    @Column
    private LocalDate date = LocalDate.now();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Operation(OperationType operationType, Double amount, Account account) {
        this.operationType = operationType;
        this.amount = amount;
        this.account = account;
    }
}
