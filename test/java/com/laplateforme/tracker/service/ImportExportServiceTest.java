package com.laplateforme.tracker.service;

import com.laplateforme.tracker.model.Student;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImportExportService")
class ImportExportServiceTest {

    private ImportExportService service;
    private List<Student> students;
    private File tmp;

    @BeforeEach
    void setUp() {
        service = new ImportExportService();
        students = List.of(
            s(1,"Alice","Martin",21,15.5,"alice@mail.com"),
            s(2,"Bob","Dupont",19,12.0,null),
            s(3,"Clara","Bernard",22,18.75,"clara@test.fr")
        );
    }

    @AfterEach
    void tearDown() { if (tmp != null && tmp.exists()) tmp.delete(); }

    private Student s(int id, String fn, String ln, int age, double grade, String email) {
        Student st = new Student(fn,ln,age,grade,email);
        st.setId(id);
        return st;
    }

    // CSV
    @Test void exportCsvCreeFichier() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertTrue(tmp.length() > 0);
    }

    @Test void exportImportCSVMemeNombre() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertEquals(students.size(), service.importCSV(tmp.getAbsolutePath()).size());
    }

    @Test void importCSVPrenomCorrect() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertEquals("Alice", service.importCSV(tmp.getAbsolutePath()).get(0).getFirstName());
    }

    @Test void importCSVAgeCorrect() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertEquals(21, service.importCSV(tmp.getAbsolutePath()).get(0).getAge());
    }

    @Test void importCSVGradeCorrect() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertEquals(15.5, service.importCSV(tmp.getAbsolutePath()).get(0).getGrade(), 0.001);
    }

    @Test void importCSVEmailNullResteNull() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertNull(service.importCSV(tmp.getAbsolutePath()).get(1).getEmail());
    }

    @Test void importCSVEmailPresent() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(students, tmp.getAbsolutePath());
        assertEquals("alice@mail.com", service.importCSV(tmp.getAbsolutePath()).get(0).getEmail());
    }

    @Test void exportCSVListeVide() throws IOException {
        tmp = File.createTempFile("test",".csv");
        service.exportCSV(List.of(), tmp.getAbsolutePath());
        assertTrue(service.importCSV(tmp.getAbsolutePath()).isEmpty());
    }

    // JSON
    @Test void exportJsonCreeFichier() throws IOException {
        tmp = File.createTempFile("test",".json");
        service.exportJSON(students, tmp.getAbsolutePath());
        assertTrue(tmp.length() > 0);
    }

    @Test void exportImportJSONMemeNombre() throws IOException {
        tmp = File.createTempFile("test",".json");
        service.exportJSON(students, tmp.getAbsolutePath());
        assertEquals(students.size(), service.importJSON(tmp.getAbsolutePath()).size());
    }

    @Test void importJSONPrenomCorrect() throws IOException {
        tmp = File.createTempFile("test",".json");
        service.exportJSON(students, tmp.getAbsolutePath());
        assertEquals("Alice", service.importJSON(tmp.getAbsolutePath()).get(0).getFirstName());
    }

    @Test void importJSONGradeCorrect() throws IOException {
        tmp = File.createTempFile("test",".json");
        service.exportJSON(students, tmp.getAbsolutePath());
        assertEquals(15.5, service.importJSON(tmp.getAbsolutePath()).get(0).getGrade(), 0.001);
    }

    @Test void csvEtJSONMemesDonnees() throws IOException {
        File csv  = File.createTempFile("test",".csv");
        File json = File.createTempFile("test",".json");
        try {
            service.exportCSV(students, csv.getAbsolutePath());
            service.exportJSON(students, json.getAbsolutePath());
            List<Student> fromCsv  = service.importCSV(csv.getAbsolutePath());
            List<Student> fromJson = service.importJSON(json.getAbsolutePath());
            assertEquals(fromCsv.size(), fromJson.size());
            for (int i = 0; i < fromCsv.size(); i++) {
                assertEquals(fromCsv.get(i).getFirstName(), fromJson.get(i).getFirstName());
                assertEquals(fromCsv.get(i).getGrade(), fromJson.get(i).getGrade(), 0.001);
            }
        } finally { csv.delete(); json.delete(); }
    }
}