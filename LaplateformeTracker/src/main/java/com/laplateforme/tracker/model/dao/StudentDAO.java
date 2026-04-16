package com.laplateforme.tracker.model.dao;

import com.laplateforme.tracker.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {

    void add(Student s) throws SQLException;
    void update(Student s) throws SQLException;
    void delete(int id) throws SQLException;

    Student findById(int id) throws SQLException;
    List<Student> findAll() throws SQLException;

    List<Student> search(String keyword) throws SQLException;
    int countSearch(String keyword) throws SQLException;

    List<Student> findAllSorted(String column, String order) throws SQLException;

    List<Student> findPage(int limit, int offset) throws SQLException;
    int countAll() throws SQLException;

    // Recherche avancée — déplacée ici depuis SearchController
    List<Student> advancedSearch(Integer minAge, Integer maxAge,
                                 Double minGrade, Double maxGrade) throws SQLException;
}