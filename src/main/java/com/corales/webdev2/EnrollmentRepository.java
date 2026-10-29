package com.corales.webdev2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    long countByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    long countByCourse_Id(Long courseId);
}
