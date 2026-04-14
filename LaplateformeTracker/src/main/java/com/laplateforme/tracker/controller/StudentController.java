package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import com.laplateforme.tracker.model.dao.StudentDAOImpl;
import com.laplateforme.tracker.util.Validator;

import java.sql.SQLException;
import java.util.List;

public class StudentController {

    private final StudentDAO dao = new StudentDAOImpl();

    public String addStudent(String firstName, String lastName, String ageStr, String gradeStr, String email) {
        String error = Validator.validateStudent(firstName, lastName, ageStr, gradeStr, email);
        if (error != null) return error;

        try {
            int age = Integer.parseInt(ageStr);
            double grade = Double.parseDouble(gradeStr.replace(",", "."));

            Student s = new Student(firstName.trim(), lastName.trim(), age, grade, email);
            dao.add(s);
            return null;

        } catch (SQLException e) {
            return "Erreur base de données : " + e.getMessage();
        }
    }

    public String updateStudent(int id, String firstName, String lastName, String ageStr, String gradeStr, String email) {
        String error = Validator.validateStudent(firstName, lastName, ageStr, gradeStr, email);
        if (error != null) return error;

        try {
            int age = Integer.parseInt(ageStr);
            double grade = Double.parseDouble(gradeStr.replace(",", "."));

            Student s = new Student(firstName.trim(), lastName.trim(), age, grade, email);
            s.setId(id);

            dao.update(s);
            return null;

        } catch (SQLException e) {
            return "Erreur base de données : " + e.getMessage();
        }
    }

    public void deleteStudent(int id) {
        try {
            dao.delete(id);
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Student getStudentById(int id) {
        try {
            return dao.findById(id);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Student> searchStudents(String keyword) {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Student> getSortedStudents(String column, String order) {
        try {
            return dao.findAllSorted(column, order);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Student> getPage(int page, int pageSize) {
        try {
            return dao.findPage(pageSize, (page - 1) * pageSize);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public int getTotalCount() {
        try {
            return dao.countAll();
        } catch (SQLException e) {
            return 0;
        }
    }
}
