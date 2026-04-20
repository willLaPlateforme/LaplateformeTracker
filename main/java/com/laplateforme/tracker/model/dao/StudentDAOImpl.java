package com.laplateforme.tracker.model.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation de StudentDAO.
 * TOUTES les requetes SQL du projet sont ici — jamais dans les controllers ou views.
 *
 * Note sur LIKE : le prof a signale d'y faire attention.
 * On utilise LOWER(colonne) LIKE LOWER(?) pour une recherche insensible a la casse
 * ET on passe TOUJOURS le parametre via setString() pour eviter l'injection SQL.
 * On ne concatene JAMAIS de saisie utilisateur directement dans le SQL.
 */
public class StudentDAOImpl implements StudentDAO {

    private final Connection conn = DatabaseConnection.getInstance();

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public void add(Student s) throws SQLException {
        String sql = "INSERT INTO student(first_name, last_name, age, grade, email)"
                   + " VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getFirstName());
            stmt.setString(2, s.getLastName());
            stmt.setInt(3, s.getAge());
            stmt.setDouble(4, s.getGrade());
            if (s.getEmail() == null || s.getEmail().isBlank())
                stmt.setNull(5, Types.VARCHAR);
            else
                stmt.setString(5, s.getEmail());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Student s) throws SQLException {
        String sql = "UPDATE student SET first_name=?, last_name=?, age=?, grade=?, email=?"
                   + " WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getFirstName());
            stmt.setString(2, s.getLastName());
            stmt.setInt(3, s.getAge());
            stmt.setDouble(4, s.getGrade());
            if (s.getEmail() == null || s.getEmail().isBlank())
                stmt.setNull(5, Types.VARCHAR);
            else
                stmt.setString(5, s.getEmail());
            stmt.setInt(6, s.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM student WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Student findById(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM student WHERE id=?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM student ORDER BY last_name, first_name")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // ── Recherche simple (nom / prenom) ──────────────────────────────────────

    /**
     * Recherche par nom ou prenom.
     * LIKE utilise avec PreparedStatement pour prevenir l'injection SQL.
     * Le % est ajoute cote Java, pas dans la saisie utilisateur.
     */
    @Override
    public List<Student> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM student"
                   + " WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?"
                   + " ORDER BY last_name, first_name";
        String p = "%" + keyword.toLowerCase() + "%";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p);
            stmt.setString(2, p);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public int countSearch(String keyword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student"
                   + " WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        String p = "%" + keyword.toLowerCase() + "%";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p);
            stmt.setString(2, p);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    // ── Tri et pagination ─────────────────────────────────────────────────────

    @Override
    public List<Student> findAllSorted(String column, String order) throws SQLException {
        // Whitelist stricte : on ne concatene QUE des valeurs controlees
        List<String> allowedCols = List.of("id","first_name","last_name","age","grade","email");
        if (!allowedCols.contains(column)) column = "last_name";
        if (!order.equalsIgnoreCase("ASC") && !order.equalsIgnoreCase("DESC")) order = "ASC";
        List<Student> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM student ORDER BY " + column + " " + order)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public List<Student> findPage(int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM student ORDER BY last_name, first_name LIMIT ? OFFSET ?";
        List<Student> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    @Override
    public int countAll() throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM student")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // ── Recherche avancee multi-criteres ──────────────────────────────────────

    /**
     * Recherche avancee : tous les criteres sont optionnels (null = ignore).
     * Criteres disponibles : ID exact, prenom, nom, email, age min/max, note min/max.
     *
     * Construction dynamique de la requete avec PreparedStatement :
     * les valeurs sont TOUJOURS passees en parametre, jamais concatenees.
     */
    @Override
    public List<Student> advancedSearch(Integer id,
                                         String firstName, String lastName, String email,
                                         Integer minAge, Integer maxAge,
                                         Double minGrade, Double maxGrade) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (id        != null) { sql.append(" AND id = ?");                           params.add(id); }
        if (firstName != null && !firstName.isBlank()) {
            sql.append(" AND LOWER(first_name) LIKE ?");
            params.add("%" + firstName.trim().toLowerCase() + "%");
        }
        if (lastName  != null && !lastName.isBlank()) {
            sql.append(" AND LOWER(last_name) LIKE ?");
            params.add("%" + lastName.trim().toLowerCase() + "%");
        }
        if (email     != null && !email.isBlank()) {
            sql.append(" AND LOWER(email) LIKE ?");
            params.add("%" + email.trim().toLowerCase() + "%");
        }
        if (minAge    != null) { sql.append(" AND age >= ?");   params.add(minAge); }
        if (maxAge    != null) { sql.append(" AND age <= ?");   params.add(maxAge); }
        if (minGrade  != null) { sql.append(" AND grade >= ?"); params.add(minGrade); }
        if (maxGrade  != null) { sql.append(" AND grade <= ?"); params.add(maxGrade); }

        sql.append(" ORDER BY last_name, first_name");
        return executeSearchQuery(sql.toString(), params);
    }

    @Override
    public int countAdvancedSearch(Integer id,
                                    String firstName, String lastName, String email,
                                    Integer minAge, Integer maxAge,
                                    Double minGrade, Double maxGrade) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (id        != null) { sql.append(" AND id = ?");                           params.add(id); }
        if (firstName != null && !firstName.isBlank()) {
            sql.append(" AND LOWER(first_name) LIKE ?");
            params.add("%" + firstName.trim().toLowerCase() + "%");
        }
        if (lastName  != null && !lastName.isBlank()) {
            sql.append(" AND LOWER(last_name) LIKE ?");
            params.add("%" + lastName.trim().toLowerCase() + "%");
        }
        if (email     != null && !email.isBlank()) {
            sql.append(" AND LOWER(email) LIKE ?");
            params.add("%" + email.trim().toLowerCase() + "%");
        }
        if (minAge    != null) { sql.append(" AND age >= ?");   params.add(minAge); }
        if (maxAge    != null) { sql.append(" AND age <= ?");   params.add(maxAge); }
        if (minGrade  != null) { sql.append(" AND grade >= ?"); params.add(minGrade); }
        if (maxGrade  != null) { sql.append(" AND grade <= ?"); params.add(maxGrade); }

        try (PreparedStatement stmt = buildStmt(sql.toString(), params)) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    // ── Anti-doublons ─────────────────────────────────────────────────────────

    @Override
    public boolean existsByFullName(String firstName, String lastName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student"
                   + " WHERE LOWER(first_name)=? AND LOWER(last_name)=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstName.trim().toLowerCase());
            stmt.setString(2, lastName.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean existsByFullNameExcludeId(String firstName, String lastName,
                                              int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student"
                   + " WHERE LOWER(first_name)=? AND LOWER(last_name)=? AND id != ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstName.trim().toLowerCase());
            stmt.setString(2, lastName.trim().toLowerCase());
            stmt.setInt(3, excludeId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    // ── Helpers internes ──────────────────────────────────────────────────────

    private List<Student> executeSearchQuery(String sql, List<Object> params) throws SQLException {
        List<Student> list = new ArrayList<>();
        try (PreparedStatement stmt = buildStmt(sql, params)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private PreparedStatement buildStmt(String sql, List<Object> params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if      (p instanceof Integer) stmt.setInt(i + 1, (Integer) p);
            else if (p instanceof Double)  stmt.setDouble(i + 1, (Double) p);
            else                           stmt.setString(i + 1, String.valueOf(p));
        }
        return stmt;
    }

    /** Convertit une ligne SQL en objet Student. */
    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getDouble("grade"),
                rs.getString("email"));
        s.setId(rs.getInt("id"));
        return s;
    }
}
