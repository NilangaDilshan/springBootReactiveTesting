package com.dilshan.springboot;

import com.dilshan.springboot.controller.EmployeeController;
import com.dilshan.springboot.dto.EmployeeDto;
import com.dilshan.springboot.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
@Slf4j
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    //Junit test for save employee  rest api
    @DisplayName("Junit test for save employee  rest api")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("666@gmail.com")
                .build();
        given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

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

    //JUnit test for get employee rest api
    @DisplayName("JUnit test for get employee rest api")
    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        String employeeId = "6609a4498e5ad3506be64dd4";
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Dimmu")
                .lastName("Borgir")
                .email("666@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Mono.just(employeeDto));
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/id/{id}", Collections.singletonMap("id", employeeId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        //then -verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    //JUnit test for get all employees api
    @DisplayName("JUnit test for get all employees api")
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
        Flux<EmployeeDto> employeesFlux = Flux.fromIterable(List.of(employeeDto, employeeDto_2, employeeDto_3));
        given(employeeService.getAllEmployees())
                .willReturn(employeesFlux);
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/all")
                .accept(MediaType.APPLICATION_JSON).exchange();
        //then -verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()))
                .hasSize(3);
    }

    //JUnit test for update employee api
    @DisplayName("JUnit test for update employee api")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        String employeeId = "6609a4498e5ad3506be64dd4";
        EmployeeDto updatedEmployeeDto = EmployeeDto.builder()
                .firstName("Slayer")
                .lastName("Araya")
                .email("slayer@gmail.com")
                .build();
        given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class), ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(updatedEmployeeDto));

        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/id/{id}", Collections.singletonMap("id", employeeId))
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

    //JUnit test for delete employee api
    @DisplayName("JUnit test for delete employee api")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessStatusCode() {

        //given - precondition or setup
        String employeeId = "6609a4498e5ad3506be64dd4";
        Mono<Void> emptyMono = Mono.empty();
        given(employeeService.deleteEmployee(employeeId))
                .willReturn(emptyMono);
        //when - action or the behaviour to be tested
        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/api/employees/id/{id}", Collections.singletonMap("id", employeeId))
                .exchange();
        //then -verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(entityExchangeResult -> log.info("Response Body: {}", entityExchangeResult.toString()));
    }
}
