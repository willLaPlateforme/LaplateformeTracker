// Student représente un seul étudiant sous forme d'objet (POJO).
// Cette classe contient uniquement les données d'un étudiant : id, nom, âge, etc.
// Elle ne contient aucune logique métier ni accès à la base de données.


package laplateforme_tracker.model;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String grade;
    private String email;
    private String createdAt;

    // Constructeur vide
    public Student() {}

    // Constructeur complet
    public Student(int id, String firstName, String lastName, int age, String grade, String email, String createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.grade = grade;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}
