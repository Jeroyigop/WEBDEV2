package com.corales.webdev2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "student", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE }, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Enrollment enrollIn(Course course, LocalDate enrollmentDate, String grade) {
        Enrollment enrollment = new Enrollment(this, course, enrollmentDate, grade);
        enrollments.add(enrollment);
        course.addEnrollment(enrollment);
        return enrollment;
    }

    public void removeEnrollment(Enrollment enrollment) {
        if (enrollments.remove(enrollment)) {
            enrollment.getCourse().removeEnrollment(enrollment);
            enrollment.setStudent(null);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
}
