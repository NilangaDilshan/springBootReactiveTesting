package com.dilshan.springboot;

import com.dilshan.springboot.dto.EmployeeDto;
import com.dilshan.springboot.repository.EmployeeRepository;
import com.dilshan.springboot.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class EmployeeControllerITests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void beforeEach() {
        this.employeeRepository.deleteAll().block();
    }

    //Integration test for save employee  rest api
    @DisplayName("Integration test for save employee  rest api")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("666@gmail.com")
                .build();

        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class).exchange();

        //then -verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    //Integration test for get employee rest api
    @DisplayName("Integration test for get employee rest api")
    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("666@gmail.com")
                .build();
        EmployeeDto savedEmployee = this.employeeService.saveEmployee(employeeDto).block();
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/id/{id}", savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        //then -verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .jsonPath("$.firstName").isEqualTo(savedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

    //Integration test for get all employees api
    @DisplayName("Integration test for get all employees api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenListOfEmployees() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("666@gmail.com")
                .build();
        EmployeeDto employeeDto_2 = EmployeeDto.builder()
                .firstName("Slayer")
                .lastName("Araya")
                .email("slayer@gmail.com")
                .build();
        EmployeeDto employeeDto_3 = EmployeeDto.builder()
                .firstName("Testament")
                .lastName("Billy")
                .email("chuck@gmail.com")
                .build();
        this.employeeService.saveEmployee(employeeDto).block();
        this.employeeService.saveEmployee(employeeDto_2).block();
        this.employeeService.saveEmployee(employeeDto_3).block();
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/all")
                .accept(MediaType.APPLICATION_JSON).exchange();
        //then -verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .hasSize(3);
    }

    //Integration test for update employee api
    @DisplayName("Integration test for update employee api")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("dimmu@gmail.com")
                .build();
        EmployeeDto updatedEmployeeDto = EmployeeDto.builder()
                .firstName("Slayer")
                .lastName("Araya")
                .email("slayer@gmail.com")
                .build();
        EmployeeDto savedEmployeeDto = this.employeeService.saveEmployee(employeeDto).block();

        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/id/{id}", Collections.singletonMap("id", savedEmployeeDto.getId()))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployeeDto), EmployeeDto.class).exchange();

        //then -verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployeeDto.getEmail());
    }

    //Integration test for delete employee api
    @DisplayName("Integration test for delete employee api")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessStatusCode() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("dimmu@gmail.com")
                .build();
        EmployeeDto savedEmployee = this.employeeService.saveEmployee(employeeDto).block();
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/api/employees/id/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .exchange();
        //then -verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()));
    }
}
