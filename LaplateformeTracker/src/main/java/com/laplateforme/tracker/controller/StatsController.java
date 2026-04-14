package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import java.util.*;

public class StatsController {

    public double getClassAverage(List<Student> students) {
        return students.stream().mapToDouble(Student::getGrade).average().orElse(0.0);
    }

    public double getMaxGrade(List<Student> students) {
        return students.stream().mapToDouble(Student::getGrade).max().orElse(0.0);
    }

    public double getMinGrade(List<Student> students) {
        return students.stream().mapToDouble(Student::getGrade).min().orElse(0.0);
    }

    public Map<String, Long> getCountByAgeGroup(List<Student> students) {
        Map<String, Long> r = new LinkedHashMap<>();
        r.put("< 18 ans", students.stream().filter(s -> s.getAge() < 18).count());
        r.put("18-20 ans", students.stream().filter(s -> s.getAge() >= 18 && s.getAge() <= 20).count());
        r.put("21-23 ans", students.stream().filter(s -> s.getAge() >= 21 && s.getAge() <= 23).count());
        r.put("> 23 ans", students.stream().filter(s -> s.getAge() > 23).count());
        return r;
    }

    public Map<String, Long> getCountByMention(List<Student> students) {
        Map<String, Long> r = new LinkedHashMap<>();
        r.put("Très bien (>=16)", students.stream().filter(s -> s.getGrade() >= 16).count());
        r.put("Bien (14-15.99)", students.stream().filter(s -> s.getGrade() >= 14 && s.getGrade() < 16).count());
        r.put("Assez bien (12-13.99)", students.stream().filter(s -> s.getGrade() >= 12 && s.getGrade() < 14).count());
        r.put("Passable (10-11.99)", students.stream().filter(s -> s.getGrade() >= 10 && s.getGrade() < 12).count());
        r.put("Échec (<10)", students.stream().filter(s -> s.getGrade() < 10).count());
        return r;
    }
}
