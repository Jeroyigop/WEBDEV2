package com.webdev2;

public class Main {

    public static void main(String[] args) {

        Roster roster = new Roster();

        roster.addStudent(new Student("Ana Reyes", 20, "BS Computer Science"));
        roster.addStudent(new Student("Mark Santos", 17, "BS Information Technology"));
        roster.addStudent(new Student("Liza Cruz", 22, "BS Computer Science"));
        roster.addStudent(new Student("Jomar Dela Cruz", 16, "BS Information Technology"));
        roster.addStudent(new Student("Kim Bautista", 19, "BS Computer Science"));
        roster.addStudent(new Student("Nico Fernandez", 21, "BS Information Technology"));

        try {
            Student invalidStudent = new Student("Broken Record", -5, "BS Computer Science");
            roster.addStudent(invalidStudent);
        } catch (InvalidAgeException e) {
            System.out.println("[ERROR] Could not create student: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Full Roster (" + roster.getAllStudents().size() + " students) ===");
        roster.printAll();

        System.out.println();
        for (Student s : roster.getAllStudents()) {
            System.out.println(s.computeStanding());
        }

        System.out.println();
        roster.printAdultReport();

        System.out.println();
        System.out.println("Program completed successfully — execution continued after the caught exception.");
    }
}
