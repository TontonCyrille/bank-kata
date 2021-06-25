package com.example.kata.service;

import com.example.kata.domain.Account;
import com.example.kata.domain.Operation;
import com.example.kata.domain.OperationType;
import com.example.kata.repository.AccountRepository;
import com.example.kata.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 *
 * Classe service où sera implémentée la logique métier d'un compte
 */
@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public Optional<Account> getAccount(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Double getBalance(Date date, int accountNumber){

        double totalDeposit = operationRepository.findByOperationTypeAndAccountNumber(
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                OperationType.DEPOSIT, accountNumber)
                .stream().mapToDouble(Operation::getAmount).sum();

        double totalWithdrawal = operationRepository.findByOperationTypeAndAccountNumber(
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                OperationType.WITHDRAWAL, accountNumber)
                .stream().mapToDouble(Operation::getAmount).sum();

        return totalDeposit - totalWithdrawal;
    }
}
