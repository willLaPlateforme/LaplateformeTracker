package com.laplateforme.tracker.model;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private double grade;
    private String email; // optionnel → peut être null

    public Student() {}

    // Constructeur sans email (email = null par défaut)
    public Student(String firstName, String lastName, int age, double grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.grade = grade;
        this.email = null;
    }

    // Constructeur avec email optionnel
    public Student(String firstName, String lastName, int age, double grade, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.grade = grade;
        this.email = email; // peut être null
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
