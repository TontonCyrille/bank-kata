package com.example.kata.repository;

import com.example.kata.KataApplication;
import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import com.example.kata.domain.Operation;
import com.example.kata.domain.OperationType;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KataApplication.class})
@TestPropertySource(locations="classpath:application-test.properties")
public class OperationRepositoryShould {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected AccountRepository accountRepository;

    protected Account account;

    @Test
    public void returnAllDeposit(){
        Customer customer = new Customer("test", "test", 12, "test@gmail.com");
        customerRepository.save(customer);
        account = new Account(1234, customer);
        accountRepository.save(account);

        operationRepository.save(new Operation(OperationType.DEPOSIT, 500d, account));
        operationRepository.save(new Operation(OperationType.DEPOSIT, 100d, account));
        List<Operation> list = operationRepository.findByOperationTypeAndAccountNumber(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                OperationType.DEPOSIT, account.getAccountNumber());
        Assert.assertFalse(list.isEmpty());
        Assertions.assertThat(list.size()).isEqualTo(2);
        list.forEach(x -> Assertions.assertThat(x.getOperationType()).isEqualTo(OperationType.DEPOSIT));

    }

    @Test
    public void returnAllWithdrawal(){
        Customer customer = new Customer("test", "test", 12, "test@gmail.com");
        customerRepository.save(customer);
        account = new Account(123456, customer);
        accountRepository.save(account);

        operationRepository.save(new Operation(OperationType.WITHDRAWAL, 50d, account));
        operationRepository.save(new Operation(OperationType.WITHDRAWAL, 100d, account));
        List<Operation> list = operationRepository.findByOperationTypeAndAccountNumber(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                OperationType.WITHDRAWAL, account.getAccountNumber());
        Assert.assertFalse(list.isEmpty());
        Assertions.assertThat(list.size()).isEqualTo(2);
        list.forEach(x -> Assertions.assertThat(x.getOperationType()).isEqualTo(OperationType.WITHDRAWAL));

    }
}
