package com.corales.webdev2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Override
    @EntityGraph(attributePaths = "employees")
    java.util.List<Department> findAll();
}
