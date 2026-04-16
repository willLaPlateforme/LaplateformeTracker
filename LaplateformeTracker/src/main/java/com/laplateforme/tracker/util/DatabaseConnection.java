package com.laplateforme.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection instance;

    private static final String URL = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private DatabaseConnection() {}

    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion PostgreSQL établie.");
            } catch (SQLException e) {
                System.err.println("❌ ERREUR : Impossible de se connecter à PostgreSQL");
                System.err.println("Message : " + e.getMessage());
                throw new RuntimeException("Connexion DB impossible", e);
            }
        }
        return instance;
    }
}
