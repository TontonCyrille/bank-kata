package com.example.kata;

import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import com.example.kata.repository.AccountRepository;
import com.example.kata.repository.CustomerRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@SpringBootApplication
public class KataApplication {

    public static void main(String[] args) {
        SpringApplication.run(KataApplication.class, args);
    }

    @Profile("!test")
    @Bean
    public ApplicationRunner initializer(AccountRepository accountRepository, CustomerRepository customerRepository) {
        return args -> {
            customerRepository.saveAll(Arrays.asList(new Customer("test1", "test1", 30, "test1@gmail.com")
                    , new Customer("test2", "test2", 35, "test2@gmail.com")));
            accountRepository.saveAll(Arrays.asList(
                    new Account(1001, customerRepository.findById(1).get()),
                    new Account(1002, customerRepository.findById(1).get()),
                    new Account(1003, customerRepository.findById(2).get())));
        };
    }
}
