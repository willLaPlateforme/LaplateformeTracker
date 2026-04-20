package com.laplateforme.tracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student POJO")
class StudentTest {

    @Test
    void constructeurVide() {
        Student s = new Student();
        assertEquals(0, s.getId());
        assertNull(s.getFirstName());
        assertNull(s.getEmail());
    }

    @Test
    void constructeurSansEmail() {
        Student s = new Student("Alice", "Martin", 21, 15.5);
        assertEquals("Alice", s.getFirstName());
        assertEquals("Martin", s.getLastName());
        assertEquals(21, s.getAge());
        assertEquals(15.5, s.getGrade(), 0.001);
        assertNull(s.getEmail());
    }

    @Test
    void constructeurAvecEmail() {
        Student s = new Student("Bob", "Dupont", 19, 12.0, "bob@mail.com");
        assertEquals("bob@mail.com", s.getEmail());
    }

    @Test
    void constructeurEmailNull() {
        Student s = new Student("Clara", "Bernard", 22, 18.0, null);
        assertNull(s.getEmail());
    }

    @Test void setId()        { Student s = new Student(); s.setId(42);             assertEquals(42, s.getId()); }
    @Test void setFirstName() { Student s = new Student(); s.setFirstName("David"); assertEquals("David", s.getFirstName()); }
    @Test void setLastName()  { Student s = new Student(); s.setLastName("Leroy");  assertEquals("Leroy", s.getLastName()); }
    @Test void setAge()       { Student s = new Student(); s.setAge(25);            assertEquals(25, s.getAge()); }
    @Test void setGrade()     { Student s = new Student(); s.setGrade(17.5);        assertEquals(17.5, s.getGrade(), 0.001); }
    @Test void setEmail()     { Student s = new Student(); s.setEmail("t@t.com");   assertEquals("t@t.com", s.getEmail()); }

    @Test
    void setEmailNull() {
        Student s = new Student("Alice", "Martin", 21, 15.0, "alice@mail.com");
        s.setEmail(null);
        assertNull(s.getEmail());
    }

    @Test
    void deuxStudentsMemesDonnees() {
        Student s1 = new Student("Alice", "Martin", 21, 15.5, "alice@mail.com");
        Student s2 = new Student("Alice", "Martin", 21, 15.5, "alice@mail.com");
        assertEquals(s1.getFirstName(), s2.getFirstName());
        assertEquals(s1.getAge(),       s2.getAge());
        assertEquals(s1.getGrade(),     s2.getGrade(), 0.001);
    }
}