package com.laplateforme.tracker.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");

    public static String validatePassword(String password) {
        if (password == null || password.length() < 8)
            return "Mot de passe : 8 caracteres minimum.";
        if (!UPPER.matcher(password).find())
            return "Mot de passe : au moins une MAJUSCULE requise.";
        if (!LOWER.matcher(password).find())
            return "Mot de passe : au moins une minuscule requise.";
        if (!DIGIT.matcher(password).find())
            return "Mot de passe : au moins un chiffre requis.";
        return null;
    }

    public static String validateStudent(String firstName, String lastName,
                                         String ageStr, String gradeStr, String email) {
        if (firstName == null || firstName.isBlank())
            return "Le prenom est obligatoire.";
        if (lastName == null || lastName.isBlank())
            return "Le nom est obligatoire.";
        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 150) return "Age invalide (1-150).";
        } catch (NumberFormatException e) { return "Age invalide."; }
        try {
            double grade = Double.parseDouble(gradeStr.replace(",", "."));
            if (grade < 0 || grade > 20) return "Note invalide (0-20).";
        } catch (NumberFormatException e) { return "Note invalide."; }
        if (email != null && !email.isBlank())
            if (!email.contains("@") || !email.contains(".")) return "Email invalide.";
        return null;
    }
}
