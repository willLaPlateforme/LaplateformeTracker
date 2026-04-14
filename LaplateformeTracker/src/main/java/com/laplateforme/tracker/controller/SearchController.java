package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    public List<Student> advancedSearch(Integer minAge, Integer maxAge,
                                        Double minGrade, Double maxGrade) {

        List<Student> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (minAge != null) { sql.append(" AND age >= ?"); params.add(minAge); }
        if (maxAge != null) { sql.append(" AND age <= ?"); params.add(maxAge); }
        if (minGrade != null) { sql.append(" AND grade >= ?"); params.add(minGrade); }
        if (maxGrade != null) { sql.append(" AND grade <= ?"); params.add(maxGrade); }

        sql.append(" ORDER BY last_name, first_name");

        try (PreparedStatement stmt = DatabaseConnection.getInstance().prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) stmt.setInt(i + 1, (Integer) p);
                else if (p instanceof Double) stmt.setDouble(i + 1, (Double) p);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student s = new Student(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDouble("grade"),
                        rs.getString("email")
                );
                s.setId(rs.getInt("id"));
                results.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche : " + e.getMessage());
        }

        return results;
    }
}
