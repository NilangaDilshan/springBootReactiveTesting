package com.dilshan.springboot.controller;

import com.dilshan.springboot.dto.EmployeeDto;
import com.dilshan.springboot.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        log.info("Save Employee: {}", employeeDto.toString());
        return this.employeeService.saveEmployee(employeeDto);
    }

    @GetMapping("/id/{id}")
    public Mono<EmployeeDto> getEmplooyeeById(@PathVariable("id") String id) {
        log.info("Get Employee by id: {}", id);
        return this.employeeService.getEmployeeById(id);
    }

    @GetMapping("/all")
    public Flux<EmployeeDto> getEmplooyeeById() {
        log.info("Get All Employees");
        return this.employeeService.getAllEmployees();
    }

    @PutMapping("/id/{id}")
    public Mono<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable("id") String id) {
        log.info("Update Employee. ID: {} Employee: {}", id, employeeDto.toString());
        return this.employeeService.updateEmployee(employeeDto, id);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployeeById(@PathVariable("id") String id) {
        log.info("Delete Employee by id: {}", id);
        return this.employeeService.deleteEmployee(id);
    }
}
