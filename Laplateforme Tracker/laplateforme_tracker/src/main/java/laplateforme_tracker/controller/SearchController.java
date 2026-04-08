package controller;

import util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelel.Student;
import modelel.dao.StudentDAOImpl;

/**
 * Recherche avancée sur les étudiants.
 * Permet de filtrer par âge minimum/maximum et par note minimum/maximum.
 */
public class SearchController {

    /**
     * Recherche avancée avec plusieurs critères optionnels.
     * Tous les paramètres peuvent être null → ignorés dans la requête.
     *
     * @param minAge   âge minimum (ou null pour ignorer)
     * @param maxAge   âge maximum (ou null pour ignorer)
     * @param minGrade note minimum (ou null pour ignorer)
     * @param maxGrade note maximum (ou null pour ignorer)
     */
    public List<Student> advancedSearch(Integer minAge, Integer maxAge,
                                        Double minGrade, Double maxGrade) {
        List<Student> results = new ArrayList<>();

        // On construit la requête dynamiquement selon les critères fournis
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (minAge != null)   { sql.append(" AND age >= ?");   params.add(minAge); }
        if (maxAge != null)   { sql.append(" AND age <= ?");   params.add(maxAge); }
        if (minGrade != null) { sql.append(" AND grade >= ?"); params.add(minGrade); }
        if (maxGrade != null) { sql.append(" AND grade <= ?"); params.add(maxGrade); }

        sql.append(" ORDER BY last_name, first_name");

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .prepareStatement(sql.toString())) {

            // Injecter les paramètres dans l'ordre
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Integer) stmt.setInt(i + 1, (Integer) param);
                else if (param instanceof Double) stmt.setDouble(i + 1, (Double) param);
            }

            ResultSet rs = stmt.executeQuery();
            StudentDAOImpl dao = new StudentDAOImpl();

            while (rs.next()) {
                // On réutilise mapRow via une astuce : on relit depuis le DAO
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setFirstName(rs.getString("first_name"));
                s.setLastName(rs.getString("last_name"));
                s.setAge(rs.getInt("age"));
                s.setGrade(rs.getDouble("grade"));
                results.add(s);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur de recherche avancée : " + e.getMessage());
        }

        return results;
    }
}