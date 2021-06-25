package com.example.kata.service;

import com.example.kata.KataApplication;
import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import com.example.kata.domain.Operation;
import com.example.kata.domain.OperationType;
import com.example.kata.repository.AccountRepository;
import com.example.kata.repository.CustomerRepository;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KataApplication.class})
@TestPropertySource(locations="classpath:application-test.properties")
public class OperationServiceShould {

    @Autowired
    private OperationService operationService;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected AccountRepository accountRepository;

    private Account account;

    @Before
    public void setUp() {
        Customer customer = new Customer("test", "test", 12, "test@gmail.com");
        customerRepository.save(customer);
        account = new Account(141189, customer);
        accountRepository.save(account);

        operationService.getOperationRepository().save(new Operation(OperationType.DEPOSIT, 500d, account));
        operationService.getOperationRepository().save(new Operation(OperationType.DEPOSIT, 100d, account));
        operationService.getOperationRepository().save(new Operation(OperationType.WITHDRAWAL, 250d, account));
    }

    @Test
    public void return_all_operations() {
        List<Operation> list = operationService.getAllOperationForAccount(new Date(), account.getAccountNumber(), 0, 10);
        Assertions.assertThat(list).isNotEmpty();
        Map<OperationType, Long> counted = list.stream().map(Operation::getOperationType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Assertions.assertThat(counted).containsEntry(OperationType.DEPOSIT, 2L);
        Assertions.assertThat(counted).containsEntry(OperationType.WITHDRAWAL, 1L);

    }
}
