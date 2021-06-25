package com.example.kata.repository;

import com.example.kata.KataApplication;
import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KataApplication.class})
@TestPropertySource(locations="classpath:application-test.properties")
public class AccountRepositoryShould {

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected AccountRepository accountRepository;

    protected Account account;

    @Before
    public void setUp() {
        Customer customer = new Customer("test", "test", 12, "test@gmail.com");
        customerRepository.save(customer);
        account = new Account(123, customer);
        accountRepository.save(account);
    }

    @Test
    public void returnAccountWithAccountNumber() {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(123);
        Assert.assertTrue(optionalAccount.isPresent());
        Assertions.assertThat(optionalAccount.get()).as("returned account with number 1234").isEqualTo(account);
    }

}
