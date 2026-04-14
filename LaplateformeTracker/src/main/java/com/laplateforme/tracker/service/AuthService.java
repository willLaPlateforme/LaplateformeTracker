package com.laplateforme.tracker.service;

import com.laplateforme.tracker.model.User;
import com.laplateforme.tracker.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    private final Connection conn = DatabaseConnection.getInstance();

    public boolean authenticate(String username, String password) {
        try {
            String sql = "SELECT password_hash FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return false;

            String hash = rs.getString("password_hash");
            return BCrypt.checkpw(password, hash);

        } catch (SQLException e) {
            System.err.println("Erreur AuthService : " + e.getMessage());
            return false;
        }
    }

    public boolean register(String username, String password) {
        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());

            String sql = "INSERT INTO users(username, password_hash) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hash);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur register : " + e.getMessage());
            return false;
        }
    }
}
