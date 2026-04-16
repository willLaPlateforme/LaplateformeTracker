package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import com.laplateforme.tracker.model.dao.StudentDAOImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * SearchController — aucune requête SQL ici.
 * Toute la logique SQL est dans StudentDAOImpl.advancedSearch().
 * Ce controller se contente de valider les paramètres et déléguer au DAO.
 */
public class SearchController {

    private final StudentDAO dao = new StudentDAOImpl();

    public List<Student> advancedSearch(Integer minAge, Integer maxAge,
                                        Double minGrade, Double maxGrade) {
        try {
            return dao.advancedSearch(minAge, maxAge, minGrade, maxGrade);
        } catch (SQLException e) {
            System.err.println("Erreur recherche avancée : " + e.getMessage());
            return List.of();
        }
    }
}