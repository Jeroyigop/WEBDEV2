package com.corales.webdev2;

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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "course", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Enrollment> enrollments = new ArrayList<>();

    public Course() {
    }

    public Course(String title) {
        this.title = title;
    }

    void addEnrollment(Enrollment enrollment) {
        if (!enrollments.contains(enrollment)) {
            enrollments.add(enrollment);
        }
    }

    void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
}
