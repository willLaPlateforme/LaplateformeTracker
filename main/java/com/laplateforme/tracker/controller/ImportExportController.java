package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.service.ImportExportService;

import java.io.IOException;
import java.util.List;

public class ImportExportController {

    private final ImportExportService service = new ImportExportService();

    public String exportCSV(List<Student> students, String path) {
        try {
            service.exportCSV(students, path);
            return null;
        } catch (IOException e) {
            return "Erreur export CSV : " + e.getMessage();
        }
    }

    public String exportJSON(List<Student> students, String path) {
        try {
            service.exportJSON(students, path);
            return null;
        } catch (IOException e) {
            return "Erreur export JSON : " + e.getMessage();
        }
    }

    public List<Student> importCSV(String path) {
        try {
            return service.importCSV(path);
        } catch (IOException e) {
            System.err.println("Erreur import CSV : " + e.getMessage());
            return List.of();
        }
    }

    public List<Student> importJSON(String path) {
        try {
            return service.importJSON(path);
        } catch (IOException e) {
            System.err.println("Erreur import JSON : " + e.getMessage());
            return List.of();
        }
    }
}
