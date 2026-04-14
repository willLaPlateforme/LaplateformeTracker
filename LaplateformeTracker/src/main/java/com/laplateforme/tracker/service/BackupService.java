package com.laplateforme.tracker.service;

import com.google.gson.Gson;
import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import com.laplateforme.tracker.model.dao.StudentDAOImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BackupService {

    private final StudentDAO dao = new StudentDAOImpl();
    private final Gson gson = new Gson();

    public void autoBackup() {
        try {
            List<Student> students = dao.findAll();

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String fileName = "backups/backup_" + timestamp + ".json";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(gson.toJson(students));
            }

            System.out.println("Sauvegarde : " + fileName);

        } catch (IOException | RuntimeException e) {
            System.err.println("Erreur BackupService : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur DAO Backup : " + e.getMessage());
        }
    }
}
