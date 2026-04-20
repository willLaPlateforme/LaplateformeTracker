package com.laplateforme.tracker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import com.laplateforme.tracker.model.dao.StudentDAOImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * BackupService — sauvegarde automatique et manuelle des etudiants en JSON.
 *
 * NOUVEAUTES :
 * - Cree le dossier backups/ automatiquement si absent
 * - backupNow() declenchable depuis le menu (sauvegarde manuelle)
 * - getLastBackupInfo() affiche la date de la derniere sauvegarde
 */
public class BackupService {

    private final StudentDAO dao   = new StudentDAOImpl();
    private final Gson       gson  = new GsonBuilder().setPrettyPrinting().create();
    private static final String BACKUP_DIR = "backups";
    private String lastBackupPath = null;

    /**
     * Declenche une sauvegarde.
     * Retourne le chemin du fichier cree, ou null si echec.
     */
    public String backupNow() {
        try {
            // Creer le dossier backups/ si absent
            File dir = new File(BACKUP_DIR);
            if (!dir.exists()) dir.mkdirs();

            List<Student> students = dao.findAll();
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = BACKUP_DIR + "/backup_" + timestamp + ".json";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(gson.toJson(students));
            }

            lastBackupPath = fileName;
            System.out.println("[BackupService] Sauvegarde OK : " + fileName
                    + " (" + students.size() + " etudiants)");
            return fileName;

        } catch (IOException | RuntimeException e) {
            System.err.println("[BackupService] Erreur : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("[BackupService] Erreur DAO : " + e.getMessage());
            return null;
        }
    }

    /** Retourne le chemin de la derniere sauvegarde, ou null si aucune. */
    public String getLastBackupPath() { return lastBackupPath; }
}
