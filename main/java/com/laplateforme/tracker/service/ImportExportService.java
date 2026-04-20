package com.laplateforme.tracker.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laplateforme.tracker.model.Student;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImportExportService {

    // EXPORT CSV
    public void exportCSV(List<Student> students, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id;first_name;last_name;age;grade;email\n");

            for (Student s : students) {
                writer.write(
                        s.getId() + ";" +
                        s.getFirstName() + ";" +
                        s.getLastName() + ";" +
                        s.getAge() + ";" +
                        s.getGrade() + ";" +
                        (s.getEmail() == null ? "" : s.getEmail()) +
                        "\n"
                );
            }
        }
    }

    // IMPORT CSV
    public List<Student> importCSV(String filePath) throws IOException {
        List<Student> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(";");

                String first = p[1];
                String last = p[2];
                int age = Integer.parseInt(p[3]);
                double grade = Double.parseDouble(p[4]);
                String email = p.length > 5 && !p[5].isBlank() ? p[5] : null;

                Student s = new Student(first, last, age, grade, email);
                list.add(s);
            }
        }
        return list;
    }

    // EXPORT JSON
    public void exportJSON(List<Student> students, String filePath) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(students);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }

    // IMPORT JSON
    public List<Student> importJSON(String filePath) throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Student>>() {}.getType();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(reader, listType);
        }
    }
}
