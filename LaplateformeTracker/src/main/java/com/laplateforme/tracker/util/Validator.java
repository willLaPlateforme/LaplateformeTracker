package com.laplateforme.tracker.util;

public class Validator {

    public static String validateStudent(String firstName, String lastName, String ageStr, String gradeStr, String email) {

        if (firstName == null || firstName.isBlank())
            return "Le prénom est obligatoire.";

        if (lastName == null || lastName.isBlank())
            return "Le nom est obligatoire.";

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
}
