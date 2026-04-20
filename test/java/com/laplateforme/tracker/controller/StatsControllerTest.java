package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StatsController")
class StatsControllerTest {

    private StatsController stats;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        stats = new StatsController();
        students = new ArrayList<>();
        students.add(s(17,  8.0));
        students.add(s(19, 11.0));
        students.add(s(20, 13.0));
        students.add(s(21, 15.0));
        students.add(s(22, 17.0));
        students.add(s(25, 20.0));
    }

    private Student s(int age, double grade) {
        return new Student("T","T",age,grade);
    }

    @Test void moyenneCorrecte() {
        double exp = (8+11+13+15+17+20)/6.0;
        assertEquals(exp, stats.getClassAverage(students), 0.001);
    }
    @Test void moyenneListeVide()      { assertEquals(0.0, stats.getClassAverage(new ArrayList<>()), 0.001); }
    @Test void moyenneUnElement()      { assertEquals(16.5, stats.getClassAverage(List.of(s(20,16.5))), 0.001); }

    @Test void noteMaxCorrecte()       { assertEquals(20.0, stats.getMaxGrade(students), 0.001); }
    @Test void noteMaxListeVide()      { assertEquals(0.0,  stats.getMaxGrade(new ArrayList<>()), 0.001); }

    @Test void noteMinCorrecte()       { assertEquals(8.0,  stats.getMinGrade(students), 0.001); }
    @Test void noteMinListeVide()      { assertEquals(0.0,  stats.getMinGrade(new ArrayList<>()), 0.001); }

    @Test void ageGroupsContient4Cles() { assertEquals(4, stats.getCountByAgeGroup(students).size()); }
    @Test void ageMoins18()            { assertEquals(1L, stats.getCountByAgeGroup(students).get("< 18 ans")); }
    @Test void age1820()               { assertEquals(2L, stats.getCountByAgeGroup(students).get("18-20 ans")); }
    @Test void age2123()               { assertEquals(2L, stats.getCountByAgeGroup(students).get("21-23 ans")); }
    @Test void agePlus23()             { assertEquals(1L, stats.getCountByAgeGroup(students).get("> 23 ans")); }

    @Test void totalAgeGroupsEgalNombre() {
        long total = stats.getCountByAgeGroup(students).values().stream().mapToLong(Long::longValue).sum();
        assertEquals(students.size(), total);
    }

    @Test void mentionContient5Cles()  { assertEquals(5, stats.getCountByMention(students).size()); }

    @Test void totalMentionsEgalNombre() {
        long total = stats.getCountByMention(students).values().stream().mapToLong(Long::longValue).sum();
        assertEquals(students.size(), total);
    }

    @Test void listeVideGroupesAZero() {
        stats.getCountByAgeGroup(new ArrayList<>()).values()
             .forEach(v -> assertEquals(0L, v));
    }
}