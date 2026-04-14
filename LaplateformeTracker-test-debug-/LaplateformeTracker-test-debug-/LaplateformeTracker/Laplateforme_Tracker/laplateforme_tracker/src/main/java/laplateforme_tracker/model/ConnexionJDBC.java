// ConnexionJDBC gère la connexion à la base de données PostgreSQL.
// Cette classe fournit une méthode statique pour obtenir une connexion
// et permet de tester si la connexion fonctionne correctement.


package laplateforme_tracker.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnexionJDBC {

    private static final String URL = "jdbc:postgresql://localhost:5432/TrackerPlateforme";
    private static final String USER = "postgres";
    private static final String PASSWORD = "WiE97bK84mA!?";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}
