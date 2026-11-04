package com.corales;

import java.util.ArrayList;
import java.util.List;

public class Roster {

    private final List<Student> students;

    public Roster() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }
}   