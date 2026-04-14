// StudentDAO gère toutes les opérations SQL liées à la table 'student'.
// Il permet :
// - de récupérer un ou plusieurs étudiants (création de listes)
// - d'insérer un nouvel étudiant
// - de mettre à jour un étudiant existant
// - de supprimer un étudiant
// Le DAO fait le lien entre la base de données et les objets Student.


package laplateforme_tracker.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM student";

        try (Connection conn = ConnexionJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getString("grade"),
                    rs.getString("email"),
                    rs.getString("created_at")
                );

                students.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}
