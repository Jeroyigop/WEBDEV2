package com.webdev2;

public class Student implements Gradable {

    private String name;
    private int age;
    private String course;

    public Student(String name, int age, String course) {
        this.name = name;
        setAge(age);
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCourse() {
        return course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new InvalidAgeException(age);
        }
        this.age = age;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String computeStanding() {
        if (age >= 18) {
            return name + " is classified as an adult learner.";
        } else {
            return name + " is classified as a minor learner.";
        }
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", course='" + course + "'}";
    }
}
