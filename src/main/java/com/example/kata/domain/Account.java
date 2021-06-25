package com.example.kata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *
 * Classe matérialisant un compte
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;
    @Column(unique = true)
    @NotNull
    private Integer accountNumber;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column
    @JsonIgnore
    private LocalDate createdAt = LocalDate.now();

    public Account(@NotNull Integer accountNumber, @NotNull Customer customer) {
        this.accountNumber = accountNumber;
        this.customer = customer;
    }
}
