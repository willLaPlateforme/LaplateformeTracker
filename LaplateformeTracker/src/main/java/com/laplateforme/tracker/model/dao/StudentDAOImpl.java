package com.laplateforme.tracker.model.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Toutes les requêtes SQL sont ici dans le Model (DAO).
 * Aucune requête SQL ne doit apparaître dans les Controller ou les View.
 */
public class StudentDAOImpl implements StudentDAO {

    private final Connection conn = DatabaseConnection.getInstance();

    @Override
    public void add(Student s) throws SQLException {
        String sql = "INSERT INTO student(first_name, last_name, age, grade, email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, s.getFirstName());
        stmt.setString(2, s.getLastName());
        stmt.setInt   (3, s.getAge());
        stmt.setDouble(4, s.getGrade());
        if (s.getEmail() == null || s.getEmail().isBlank())
            stmt.setNull(5, Types.VARCHAR);
        else
            stmt.setString(5, s.getEmail());
        stmt.executeUpdate();
    }

    @Override
    public void update(Student s) throws SQLException {
        String sql = "UPDATE student SET first_name=?, last_name=?, age=?, grade=?, email=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, s.getFirstName());
        stmt.setString(2, s.getLastName());
        stmt.setInt   (3, s.getAge());
        stmt.setDouble(4, s.getGrade());
        if (s.getEmail() == null || s.getEmail().isBlank())
            stmt.setNull(5, Types.VARCHAR);
        else
            stmt.setString(5, s.getEmail());
        stmt.setInt(6, s.getId());
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM student WHERE id=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    @Override
    public Student findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student WHERE id=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return map(rs);
        return null;
    }

    @Override
    public List<Student> findAll() throws SQLException {
        ResultSet rs = conn.createStatement()
                .executeQuery("SELECT * FROM student ORDER BY last_name, first_name");
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public List<Student> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM student WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        String p = "%" + keyword.toLowerCase() + "%";
        stmt.setString(1, p);
        stmt.setString(2, p);
        ResultSet rs = stmt.executeQuery();
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public int countSearch(String keyword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        String p = "%" + keyword.toLowerCase() + "%";
        stmt.setString(1, p);
        stmt.setString(2, p);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    @Override
    public List<Student> findAllSorted(String column, String order) throws SQLException {
        // Whitelist stricte — jamais de concaténation directe de saisie utilisateur
        List<String> allowed = List.of("first_name", "last_name", "age", "grade", "email");
        if (!allowed.contains(column)) column = "last_name";
        if (!order.equalsIgnoreCase("ASC") && !order.equalsIgnoreCase("DESC")) order = "ASC";

        String sql = "SELECT * FROM student ORDER BY " + column + " " + order;
        ResultSet rs = conn.createStatement().executeQuery(sql);
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public List<Student> findPage(int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM student ORDER BY last_name, first_name LIMIT ? OFFSET ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, limit);
        stmt.setInt(2, offset);
        ResultSet rs = stmt.executeQuery();
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public int countAll() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM student");
        rs.next();
        return rs.getInt(1);
    }

    /**
     * Recherche avancée avec critères optionnels.
     * Toute la logique SQL est ici dans le DAO — pas dans le controller.
     * Les paramètres null sont ignorés dans la requête.
     */
    @Override
    public List<Student> advancedSearch(Integer minAge, Integer maxAge,
                                        Double minGrade, Double maxGrade) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (minAge   != null) { sql.append(" AND age >= ?");   params.add(minAge); }
        if (maxAge   != null) { sql.append(" AND age <= ?");   params.add(maxAge); }
        if (minGrade != null) { sql.append(" AND grade >= ?"); params.add(minGrade); }
        if (maxGrade != null) { sql.append(" AND grade <= ?"); params.add(maxGrade); }

        sql.append(" ORDER BY last_name, first_name");

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof Integer) stmt.setInt   (i + 1, (Integer) p);
            else if (p instanceof Double) stmt.setDouble(i + 1, (Double)  p);
        }

        ResultSet rs = stmt.executeQuery();
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    // Convertit une ligne SQL en objet Student
    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt   ("age"),
                rs.getDouble("grade"),
                rs.getString("email")
        );
        s.setId(rs.getInt("id"));
        return s;
    }
}