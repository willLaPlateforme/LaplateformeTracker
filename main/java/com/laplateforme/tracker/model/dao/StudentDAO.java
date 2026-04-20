package com.laplateforme.tracker.model.dao;

import com.laplateforme.tracker.model.Student;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO definissant le contrat d'acces aux donnees Student.
 * Toutes les requetes SQL sont UNIQUEMENT dans StudentDAOImpl.
 */
public interface StudentDAO {

    // CRUD de base
    void add(Student s) throws SQLException;
    void update(Student s) throws SQLException;
    void delete(int id) throws SQLException;
    Student findById(int id) throws SQLException;
    List<Student> findAll() throws SQLException;

    // Recherche simple (par nom/prenom)
    List<Student> search(String keyword) throws SQLException;
    int countSearch(String keyword) throws SQLException;

    // Tri et pagination
    List<Student> findAllSorted(String column, String order) throws SQLException;
    List<Student> findPage(int limit, int offset) throws SQLException;
    int countAll() throws SQLException;

    /**
     * Recherche avancee multi-criteres.
     * Tous les parametres sont optionnels (null = ignore).
     * Couvre : ID, prenom, nom, email, age min/max, note min/max.
     */
    List<Student> advancedSearch(Integer id,
                                  String firstName, String lastName, String email,
                                  Integer minAge, Integer maxAge,
                                  Double minGrade, Double maxGrade) throws SQLException;

    int countAdvancedSearch(Integer id,
                             String firstName, String lastName, String email,
                             Integer minAge, Integer maxAge,
                             Double minGrade, Double maxGrade) throws SQLException;

    // Anti-doublons
    boolean existsByFullName(String firstName, String lastName) throws SQLException;
    boolean existsByFullNameExcludeId(String firstName, String lastName, int excludeId) throws SQLException;
}
