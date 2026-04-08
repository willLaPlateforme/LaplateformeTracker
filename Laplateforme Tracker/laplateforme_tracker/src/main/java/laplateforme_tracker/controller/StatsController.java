package controller;

import java.util.*;
import java.util.stream.Collectors;

import model.Student;

/**
 * Calcule les statistiques sur la liste des étudiants.
 * Utilise les Streams Java (fonctions modernes de Java 8+).
 */
public class StatsController {

    /** Moyenne des notes de toute la classe */
    public double getClassAverage(List<Student> students) {
        return students.stream()
                .mapToDouble(Student::getGrade)
                .average()
                .orElse(0.0);
    }

    /** Note la plus haute */
    public double getMaxGrade(List<Student> students) {
        return students.stream()
                .mapToDouble(Student::getGrade)
                .max()
                .orElse(0.0);
    }

    /** Note la plus basse */
    public double getMinGrade(List<Student> students) {
        return students.stream()
                .mapToDouble(Student::getGrade)
                .min()
                .orElse(0.0);
    }

    /** Nombre d'étudiants par tranche d'âge (ex: "18-20", "21-23"...) */
    public Map<String, Long> getCountByAgeGroup(List<Student> students) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("< 18 ans",  students.stream().filter(s -> s.getAge() < 18).count());
        result.put("18-20 ans", students.stream().filter(s -> s.getAge() >= 18 && s.getAge() <= 20).count());
        result.put("21-23 ans", students.stream().filter(s -> s.getAge() >= 21 && s.getAge() <= 23).count());
        result.put("> 23 ans",  students.stream().filter(s -> s.getAge() > 23).count());
        return result;
    }

    /** Nombre d'étudiants par mention */
    public Map<String, Long> getCountByMention(List<Student> students) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Très bien (>=16)",      students.stream().filter(s -> s.getGrade() >= 16).count());
        result.put("Bien (14-15.99)",       students.stream().filter(s -> s.getGrade() >= 14 && s.getGrade() < 16).count());
        result.put("Assez bien (12-13.99)", students.stream().filter(s -> s.getGrade() >= 12 && s.getGrade() < 14).count());
        result.put("Passable (10-11.99)",   students.stream().filter(s -> s.getGrade() >= 10 && s.getGrade() < 12).count());
        result.put("Échec (<10)",           students.stream().filter(s -> s.getGrade() < 10).count());
        return result;
    }
}