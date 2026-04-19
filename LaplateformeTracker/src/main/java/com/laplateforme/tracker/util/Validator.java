package com.laplateforme.tracker.util;

import java.util.List;

public class Validator {

    public static String validateStudent(String firstName, String lastName, String ageStr, String gradeStr, String email) {

        if (firstName == null || firstName.isBlank())
            return "Le prénom est obligatoire.";

        // Prénom : pas de chiffres ni de symboles (seulement lettres, espaces, tirets, apostrophes)
        if (!firstName.matches("[a-zA-ZÀ-ÿ\\s\\-']+"))
            return "Le prénom ne peut pas contenir de chiffres ou de symboles.";

        if (lastName == null || lastName.isBlank())
            return "Le nom est obligatoire.";

        // Nom : même règle que le prénom
        if (!lastName.matches("[a-zA-ZÀ-ÿ\\s\\-']+"))
            return "Le nom ne peut pas contenir de chiffres ou de symboles.";

        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 150)
                return "Âge invalide.";
        } catch (NumberFormatException e) {
            return "Âge invalide.";
        }

        try {
            double grade = Double.parseDouble(gradeStr.replace(",", "."));
            if (grade < 0 || grade > 20)
                return "Note invalide.";
        } catch (NumberFormatException e) {
            return "Note invalide.";
        }

        // Email optionnel → on valide seulement si rempli
        if (email != null && !email.isBlank()) {
            if (!email.contains("@") || !email.contains("."))
                return "Email invalide.";
        }

        return null; // OK
    }

    // Vérifie si un étudiant identique existe déjà dans la liste
    public static boolean isDuplicate(String firstName, String lastName, String ageStr, String gradeStr, String email, List<String[]> existingStudents) {
        for (String[] s : existingStudents) {
            // s = [firstName, lastName, age, grade, email]
            if (s[0].equalsIgnoreCase(firstName)  &&
                s[1].equalsIgnoreCase(lastName)    &&
                s[2].equals(ageStr)                &&
                s[3].equals(gradeStr)              &&
                s[4].equalsIgnoreCase(email))
                return true;
        }
        return false;
    }
}