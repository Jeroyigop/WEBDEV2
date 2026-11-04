package com.corales;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        System.out.println("===== WEBDEV 2 WEEK 1 =====");
        System.out.println();

        // TASK 3: Create a roster
        Roster roster = new Roster();

        // Add 6 students
        roster.addStudent(new Student(
        "Alex",
        18,
        "BS Information Technology"
));

roster.addStudent(new Student(
        "Ryan",
        21,
        "BS Computer Science"
));

roster.addStudent(new Student(
        "Chris",
        20,
        "BS Information Technology"
));

roster.addStudent(new Student(
        "Emma",
        19,
        "BS Information Technology"
));

roster.addStudent(new Student(
        "Lucas",
        22,
        "BS Information Technology"
));

roster.addStudent(new Student(
        "Sofia",
        18,
        "BS Information Technology"
));

        // Print all students
        System.out.println("===== STUDENT ROSTER =====");

        for (Student student : roster.getStudents()) {
            System.out.println(student);
        }

        System.out.println();

        // TASK 4: Exception handling
        System.out.println("===== EXCEPTION HANDLING =====");

        try {
            Student invalidStudent = new Student(
                    "Invalid Student",
                    -5,
                    "BS Information Technology"
            );

            roster.addStudent(invalidStudent);

        } catch (InvalidAgeException e) {
            System.out.println("Exception caught: " + e.getMessage());
            System.out.println("Program continues running...");
        }

        System.out.println();

        // TASK 5: Stream-based roster report
        System.out.println("===== STREAM-BASED ROSTER REPORT =====");

        List<String> qualifiedStudents = roster.getStudents()
                .stream()
                .filter(student -> student.getAge() >= 18)
                .map(Student::getName)
                .collect(Collectors.toList());

        System.out.println("Students aged 18 or older:");

        qualifiedStudents.forEach(name ->
                System.out.println("- " + name)
        );

        System.out.println();

        System.out.println(
                "Number of qualified students: "
                        + qualifiedStudents.size()
        );

        System.out.println();

        System.out.println("===== PROGRAM COMPLETED SUCCESSFULLY =====");
    }
}