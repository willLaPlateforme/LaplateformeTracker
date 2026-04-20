package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import com.laplateforme.tracker.model.dao.StudentDAOImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * SearchController — aucune requete SQL ici.
 * Toute la logique SQL est dans StudentDAOImpl.advancedSearch().
 * Ce controller valide les parametres puis delegue au DAO.
 */
public class SearchController {

    private final StudentDAO dao = new StudentDAOImpl();

    /**
     * Recherche avancee avec tous les criteres optionnels.
     *
     * @param id        ID exact (null = ignore)
     * @param firstName Prenom (recherche partielle, null = ignore)
     * @param lastName  Nom    (recherche partielle, null = ignore)
     * @param email     Email  (recherche partielle, null = ignore)
     * @param minAge    Age minimum (null = ignore)
     * @param maxAge    Age maximum (null = ignore)
     * @param minGrade  Note minimum (null = ignore)
     * @param maxGrade  Note maximum (null = ignore)
     */
    public List<Student> advancedSearch(Integer id,
                                         String firstName, String lastName, String email,
                                         Integer minAge, Integer maxAge,
                                         Double minGrade, Double maxGrade) {
        try {
            return dao.advancedSearch(id, firstName, lastName, email,
                                      minAge, maxAge, minGrade, maxGrade);
        } catch (SQLException e) {
            System.err.println("Erreur recherche avancee : " + e.getMessage());
            return List.of();
        }
    }

    public int countAdvancedSearch(Integer id,
                                    String firstName, String lastName, String email,
                                    Integer minAge, Integer maxAge,
                                    Double minGrade, Double maxGrade) {
        try {
            return dao.countAdvancedSearch(id, firstName, lastName, email,
                                           minAge, maxAge, minGrade, maxGrade);
        } catch (SQLException e) { return 0; }
    }
}
