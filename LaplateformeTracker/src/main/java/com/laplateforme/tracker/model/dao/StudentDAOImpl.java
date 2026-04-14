package com.laplateforme.tracker.model.dao;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private final Connection conn = DatabaseConnection.getInstance();

    @Override
    public void add(Student s) throws SQLException {
        String sql = "INSERT INTO student(first_name, last_name, age, grade, email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
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

    @Override
    public void update(Student s) throws SQLException {
        String sql = "UPDATE student SET first_name=?, last_name=?, age=?, grade=?, email=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

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
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM student ORDER BY last_name, first_name");
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public List<Student> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM student WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + keyword.toLowerCase() + "%");
        stmt.setString(2, "%" + keyword.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();

        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public int countSearch(String keyword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + keyword.toLowerCase() + "%");
        stmt.setString(2, "%" + keyword.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    @Override
    public List<Student> findAllSorted(String column, String order) throws SQLException {
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

    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getDouble("grade"),
                rs.getString("email") // peut être null
        );
        s.setId(rs.getInt("id"));
        return s;
    }
}
