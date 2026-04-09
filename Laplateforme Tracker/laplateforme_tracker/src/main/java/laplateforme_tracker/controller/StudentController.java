package controller;

import java.sql.SQLException;
import java.util.List;

import modelel.Student;
import modelel.dao.StudentDAO;
import modelel.dao.StudentDAOImpl;

/**
 * Contrôleur principal : relie la Vue au DAO.
 * La vue appelle ces méthodes, qui appellent le DAO.
 * Ce fichier contient aussi la validation des données.
 */
public class StudentController {

    private final StudentDAO dao = new StudentDAOImpl();

    // ── Ajouter un étudiant ──────────────────────────────────────────────────
    public void addStudent(String firstName, String lastName, String ageStr, String gradeStr) {
        try {
            // Validation
            if (firstName.isBlank() || lastName.isBlank()) {
                throw new IllegalArgumentException("Le nom et le prénom sont obligatoires.");
            }
            int age = Integer.parseInt(ageStr);
            double grade = Double.parseDouble(gradeStr.replace(",", "."));

            if (age <= 0 || age >= 150) throw new IllegalArgumentException("Âge invalide (1-149).");
            if (grade < 0 || grade > 20) throw new IllegalArgumentException("Note invalide (0-20).");

            Student s = new Student(firstName.trim(), lastName.trim(), age, grade);
            dao.add(s);

        } catch (NumberFormatException e) {
            System.err.println(" Âge ou note invalide : veuillez entrer un nombre.");
        } catch (IllegalArgumentException e) {
            System.err.println(" " + e.getMessage());
        } catch (SQLException e) {
            System.err.println(" Erreur base de données : " + e.getMessage());
        }
    }

    // ── Modifier un étudiant ─────────────────────────────────────────────────
    public void updateStudent(int id, String firstName, String lastName, String ageStr, String gradeStr) {
        try {
            int age = Integer.parseInt(ageStr);
            double grade = Double.parseDouble(gradeStr.replace(",", "."));

            Student s = new Student(firstName.trim(), lastName.trim(), age, grade);
            s.setId(id);
            dao.update(s);

        } catch (NumberFormatException e) {
            System.err.println(" Âge ou note invalide.");
        } catch (SQLException e) {
            System.err.println(" Erreur base de données : " + e.getMessage());
        }
    }

    // ── Supprimer un étudiant ────────────────────────────────────────────────
    public void deleteStudent(int id) {
        try {
            dao.delete(id);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ── Récupérer tous les étudiants ─────────────────────────────────────────
    public List<Student> getAllStudents() {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            System.err.println(" Impossible de charger les étudiants : " + e.getMessage());
            return List.of();  // liste vide en cas d'erreur
        }
    }

    // ── Trouver par id ───────────────────────────────────────────────────────
    public Student getStudentById(int id) {
        try {
            return dao.findById(id);
        } catch (SQLException e) {
            System.err.println(" Erreur : " + e.getMessage());
            return null;
        }
    }

    // ── Recherche ────────────────────────────────────────────────────────────
    public List<Student> searchStudents(String keyword) {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            System.err.println(" Erreur de recherche : " + e.getMessage());
            return List.of();
        }
    }

    // ── Tri ──────────────────────────────────────────────────────────────────
    public List<Student> getSortedStudents(String column, String order) {
        try {
            return dao.findAllSorted(column, order);
        } catch (SQLException e) {
            System.err.println(" Erreur de tri : " + e.getMessage());
            return List.of();
        }
    }

    // ── Pagination ───────────────────────────────────────────────────────────
    public List<Student> getPage(int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            return dao.findPage(pageSize, offset);
        } catch (SQLException e) {
            System.err.println(" Erreur de pagination : " + e.getMessage());
            return List.of();
        }
    }

    public int getTotalCount() {
        try {
            return dao.countAll();
        } catch (SQLException e) {
            return 0;
        }
    }
}