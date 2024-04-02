package com.dilshan.springboot.service.impl;

import com.dilshan.springboot.dto.EmployeeDto;
import com.dilshan.springboot.entity.Employee;
import com.dilshan.springboot.mapper.EmployeeMapper;
import com.dilshan.springboot.repository.EmployeeRepository;
import com.dilshan.springboot.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        log.info("Save Employee: {}", employeeDto.toString());
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto, new Employee());
        Mono<Employee> savedEmployee = this.employeeRepository.save(employee);
        return savedEmployee.map(employeeEntity -> EmployeeMapper.mapToEmployeeDto(employeeEntity, new EmployeeDto()));
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(String id) {
        log.info("Get Employee by id: {}", id);
        return this.employeeRepository.findById(id)
                .map(employeeEntity -> EmployeeMapper.mapToEmployeeDto(employeeEntity, new EmployeeDto()));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        log.info("Get All Employees...");
        return this.employeeRepository.findAll()
                .map(employee -> EmployeeMapper.mapToEmployeeDto(employee, new EmployeeDto()))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
        log.info("Update Employee. ID: {} Employee: {}", employeeId, employeeDto.toString());
        Mono<Employee> existingEmployeeMono = this.employeeRepository.findById(employeeId);
        Mono<Employee> updatedEmployeeMono = existingEmployeeMono.flatMap(existingEmployee -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());
            return this.employeeRepository.save(existingEmployee);
        });
        return updatedEmployeeMono.map(updateEmployee -> EmployeeMapper.mapToEmployeeDto(updateEmployee, new EmployeeDto()));
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        log.info("Delete Employee. ID: {}", employeeId);
        return this.employeeRepository.deleteById(employeeId);
    }
}
