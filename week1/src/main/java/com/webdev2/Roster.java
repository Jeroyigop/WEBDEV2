package com.webdev2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Roster {

    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void printAll() {
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public List<String> printAdultReport() {
        List<String> adultNames = students.stream()
                .filter(s -> s.getAge() >= 18)
                .map(Student::getName)
                .collect(Collectors.toList());

        System.out.println("=== Adult Student Report  (age >= 18) ===");
        adultNames.forEach(name -> System.out.println(" - " + name));
        System.out.println("Total qualifying students: " + adultNames.size());

        return adultNames;
    }
}
