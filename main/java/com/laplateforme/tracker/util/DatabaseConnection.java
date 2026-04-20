package com.laplateforme.tracker.util;

import java.sql.*;

/**
 * Singleton gerant la connexion JDBC a PostgreSQL.
 *
 * Contient les 3 methodes demandees par le prof :
 *   - getInstance()     : connexion (ouverture)
 *   - closeConnection() : fermeture propre
 *   - executeQuery()    : execution de requetes SELECT
 */
public class DatabaseConnection {

    private static Connection instance;

    private static final String URL      = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "postgres";

    private DatabaseConnection() {}

    /** Retourne la connexion unique, la cree si necessaire. */
    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion PostgreSQL etablie.");
            }
        } catch (SQLException e) {
            System.err.println("ERREUR : Impossible de se connecter a PostgreSQL");
            System.err.println("Cause  : " + e.getMessage());
            throw new RuntimeException("Connexion DB impossible", e);
        }
        return instance;
    }

    /** Ferme proprement la connexion a la base de donnees. */
    public static void closeConnection() {
        if (instance != null) {
            try {
                if (!instance.isClosed()) {
                    instance.close();
                    System.out.println("Connexion PostgreSQL fermee proprement.");
                }
                instance = null;
            } catch (SQLException e) {
                System.err.println("Erreur fermeture BDD : " + e.getMessage());
            }
        }
    }

    /**
     * Methode generique pour executer une requete SELECT.
     * Retourne le ResultSet — l'appelant est responsable de le fermer.
     */
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement stmt = getInstance().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Integer)    stmt.setInt(i + 1, (Integer) params[i]);
            else if (params[i] instanceof Double) stmt.setDouble(i + 1, (Double) params[i]);
            else                                  stmt.setString(i + 1, String.valueOf(params[i]));
        }
        return stmt.executeQuery();
    }

    /** Retourne true si la connexion est active et ouverte. */
    public static boolean isConnected() {
        try { return instance != null && !instance.isClosed(); }
        catch (SQLException e) { return false; }
    }
}
