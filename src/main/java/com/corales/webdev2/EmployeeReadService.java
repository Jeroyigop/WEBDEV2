package com.corales.webdev2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeReadService {

    private final EmployeeRepository employeeRepository;

    public EmployeeReadService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee loadWithoutDepartmentFetch(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    @Transactional(readOnly = true)
    public String readDepartmentNameTransactionally(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        return employee.getDepartment().getName();
    }

    public Employee loadWithDepartmentFetch(Long id) {
        return employeeRepository.findWithDepartmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }
}
