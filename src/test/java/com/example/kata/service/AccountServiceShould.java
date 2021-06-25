package com.example.kata.service;

import com.example.kata.KataApplication;
import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import com.example.kata.domain.Operation;
import com.example.kata.domain.OperationType;
import com.example.kata.repository.AccountRepository;
import com.example.kata.repository.CustomerRepository;
import com.example.kata.repository.OperationRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KataApplication.class})
@TestPropertySource(locations="classpath:application-test.properties")
public class AccountServiceShould {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected AccountRepository accountRepository;

    private Account account;

    @Before
    public void setUp(){
        Customer customer = new Customer("test", "test", 12, "test@gmail.com");
        customerRepository.save(customer);
        account = new Account(14118, customer);
        accountRepository.save(account);

        operationRepository.save(new Operation(OperationType.DEPOSIT, 500d, account));
        operationRepository.save(new Operation(OperationType.DEPOSIT, 100d, account));
        operationRepository.save(new Operation(OperationType.WITHDRAWAL, 250d, account));

    }

    @Test
    public void return_account_balance(){
        Double balance = accountService.getBalance(new Date(), account.getAccountNumber());
        Assertions.assertThat(balance).isEqualTo(350d);
    }
}
