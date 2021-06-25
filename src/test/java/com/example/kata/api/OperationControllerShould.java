package com.example.kata.api;

import com.example.kata.domain.Account;
import com.example.kata.domain.Customer;
import com.example.kata.domain.Operation;
import com.example.kata.domain.OperationType;
import com.example.kata.exception.AccountNotFoundException;
import com.example.kata.exception.SoldeInsuffisantException;
import com.example.kata.repository.AccountRepository;
import com.example.kata.repository.CustomerRepository;
import com.example.kata.repository.OperationRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvcBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(value = SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperationControllerShould {

    @Autowired
    protected MockMvcBuilder mockMvcBuilder;

    @MockBean
    private CustomerRepository mockCustomerRepository;
    @MockBean
    private AccountRepository mockAccountRepository;
    @MockBean
    private OperationRepository mockOperationRepository;

    @Before
    @SneakyThrows
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(mockMvcBuilder);

    }

    @Test
    public void throw_Exception_When_Account_NotFound() throws Exception {

        doReturn(Optional.empty()).when(mockAccountRepository).findByAccountNumber(any());

        mockMvcBuilder.build().perform(post("/v1/operations?operationType=WITHDRAWAL")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"accountNumber\": 1001, \"operationAmount\": 50}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException))
                .andExpect(result -> assertEquals("Account with number 1001 not found", result.getResolvedException().getMessage()));

    }

    @Test
    public void throw_Exception_When_Operation_Amount_IsLess_OrEqual_ToZero() throws Exception {

        doReturn(Optional.of(new Account(1001, new Customer("test", "test", 12, "test@gmail.com"))))
                .when(mockAccountRepository).findByAccountNumber(any());

        mockMvcBuilder.build().perform(post("/v1/operations?operationType=WITHDRAWAL")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"accountNumber\": 1001, \"operationAmount\": 50}"))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SoldeInsuffisantException))
                .andExpect(result -> assertEquals("Insufficient balance !!", result.getResolvedException().getMessage()));

    }

    @Test
    public void throwExceptionWhenWithrawalAmountGreaterThanAccountBalance() throws Exception {

        doReturn(Optional.of(new Account(1001, new Customer("test", "test", 12, "test@gmail.com"))))
                .when(mockAccountRepository).findByAccountNumber(any());

        mockMvcBuilder.build().perform(post("/v1/operations?operationType=WITHDRAWAL")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"accountNumber\": 1001, \"operationAmount\": 300}"))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SoldeInsuffisantException))
                .andExpect(result -> assertEquals("Insufficient balance !!", result.getResolvedException().getMessage()));

    }

    @Test
    public void returnSuccesfullDepositOperation() throws JSONException {

        doReturn(Optional.of(new Account(1,2001,
                new Customer(1,"test", "test", 12, "test@gmail.com",null), null)))
                .when(mockAccountRepository).findByAccountNumber(any());

        // given:
        MockMvcRequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body("{ \"accountNumber\": 2001, \"operationAmount\": 50}");

        //when
        ResponseOptions response = given().spec(request)
                .post("/v1/operations?operationType=DEPOSIT");

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        DocumentContext jsonBody = JsonPath.parse(response.getBody().asString());
        JSONObject jsonObject = new JSONObject((LinkedHashMap)jsonBody.json());
        assertThat(jsonObject).hasToString("{\"operationMessage\":\"Operation (DEPOSIT) of 50.0 on account 2001\"}");
    }

    @Test
    public void returnSuccesfullWithrawalOperation() {

        Account account = new Account(1,2001,
                new Customer(1,"test", "test", 12, "test@gmail.com",null), null);
        doReturn(Optional.of(account)).when(mockAccountRepository).findByAccountNumber(any());

        doReturn(Collections.singletonList(new Operation(OperationType.DEPOSIT, 500d, account)))
                .when(mockOperationRepository).findByOperationTypeAndAccountNumber(any(LocalDate.class),
                eq(OperationType.DEPOSIT), eq(2001));

        // given:
        MockMvcRequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body("{ \"accountNumber\": 2001, \"operationAmount\": 100}");

        //When
        ResponseOptions response = given().spec(request)
                .post("/v1/operations?operationType=WITHDRAWAL");

        // then:
        assertThat(response.statusCode()).isEqualTo(201);
        DocumentContext jsonBody = JsonPath.parse(response.getBody().asString());
        JSONObject jsonObject = new JSONObject((LinkedHashMap)jsonBody.json());
        assertThat(jsonObject).hasToString("{\"operationMessage\":\"Operation (WITHDRAWAL) of 100.0 on account 2001\"}");
    }

    @Test
    public void returnAllOperations() throws JSONException {

        Account account = new Account(1,2001,
                new Customer(1,"test", "test", 12, "test@gmail.com",null), null);
        doReturn(Optional.of(account)).when(mockAccountRepository).findByAccountNumber(any());

        doReturn(Collections.singletonList(new Operation(OperationType.DEPOSIT, 500d, account)))
                .when(mockOperationRepository).findByOperationTypeAndAccountNumber(any(LocalDate.class),
                eq(OperationType.DEPOSIT), eq(2001));

        doReturn(Collections.singletonList(new Operation(OperationType.DEPOSIT, 100d, account)))
                .when(mockOperationRepository).findByOperationTypeAndAccountNumber(any(LocalDate.class),
                eq(OperationType.WITHDRAWAL), eq(2001));

        Operation operationDeposit1 = new Operation(OperationType.DEPOSIT, 500d, account);
        Operation operationDeposit2= new Operation(OperationType.DEPOSIT, 100d, account);
        Operation operationWithdrawal = new Operation(OperationType.WITHDRAWAL, 300d, account);

        doReturn(Arrays.asList(operationDeposit1, operationDeposit2, operationWithdrawal))
                .when(mockOperationRepository).findAllByAccountNumber(any(LocalDate.class), eq(2001), any(Pageable.class));

        ResponseOptions response = given().get("/v1/operations/2001?page=0&size=10");

        // then:
        assertThat(response.statusCode()).isEqualTo(200);
        DocumentContext jsonBody = JsonPath.parse(response.getBody().asString());
        JSONObject jsonObject = new JSONObject((LinkedHashMap)jsonBody.json());
        assertThat(jsonObject.get("operationMessage")).hasToString("Balance on 2021-06-25 = 400.0 for accountNumber : 2001");
        assertThat(jsonObject.get("body")).hasToString("[{\"date\":\"2021-06-25\",\"amount\":500,\"operationType\":\"DEPOSIT\"},{\"date\":\"2021-06-25\",\"amount\":100,\"operationType\":\"DEPOSIT\"},{\"date\":\"2021-06-25\",\"amount\":300,\"operationType\":\"WITHDRAWAL\"}]");

    }

}
