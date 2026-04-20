package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.model.Student;
import com.laplateforme.tracker.model.dao.StudentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("StudentController")
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock private StudentDAO mockDao;
    private StudentController ctrl;

    @BeforeEach
    void setUp() throws Exception {
        ctrl = new StudentController();
        var f = StudentController.class.getDeclaredField("dao");
        f.setAccessible(true);
        f.set(ctrl, mockDao);
    }

    @Test void addValide() throws SQLException {
        doNothing().when(mockDao).add(any());
        assertNull(ctrl.addStudent("Alice","Martin","21","15.5","alice@mail.com"));
        verify(mockDao).add(any());
    }

    @Test void addPrenomVide() throws SQLException {
        assertNotNull(ctrl.addStudent("","Martin","21","15",null));
        verify(mockDao,never()).add(any());
    }

    @Test void addAgeInvalide() throws SQLException {
        assertNotNull(ctrl.addStudent("Alice","Martin","abc","15",null));
        verify(mockDao,never()).add(any());
    }

    @Test void addNoteInvalide() throws SQLException {
        assertNotNull(ctrl.addStudent("Alice","Martin","21","25",null));
        verify(mockDao,never()).add(any());
    }

    @Test void addSQLException() throws SQLException {
        doThrow(new SQLException("erreur")).when(mockDao).add(any());
        assertNotNull(ctrl.addStudent("Alice","Martin","21","15",null));
    }

    @Test void updateValide() throws SQLException {
        doNothing().when(mockDao).update(any());
        assertNull(ctrl.updateStudent(1,"Alice","Martin","21","15.5",null));
        verify(mockDao).update(any());
    }

    @Test void updateInvalide() throws SQLException {
        assertNotNull(ctrl.updateStudent(1,"","Martin","21","15",null));
        verify(mockDao,never()).update(any());
    }

    @Test void deleteAppelleDaoAvecBonId() throws SQLException {
        doNothing().when(mockDao).delete(5);
        ctrl.deleteStudent(5);
        verify(mockDao).delete(5);
    }

    @Test void deleteSilencieuxSiException() throws SQLException {
        doThrow(new SQLException("err")).when(mockDao).delete(anyInt());
        assertDoesNotThrow(() -> ctrl.deleteStudent(99));
    }

    @Test void getAllStudents() throws SQLException {
        List<Student> exp = List.of(new Student("Alice","Martin",21,15.0));
        when(mockDao.findAll()).thenReturn(exp);
        assertEquals(1, ctrl.getAllStudents().size());
    }

    @Test void getAllStudentsException() throws SQLException {
        when(mockDao.findAll()).thenThrow(new SQLException("err"));
        assertTrue(ctrl.getAllStudents().isEmpty());
    }

    @Test void getTotalCount() throws SQLException {
        when(mockDao.countAll()).thenReturn(42);
        assertEquals(42, ctrl.getTotalCount());
    }

    @Test void getTotalCountException() throws SQLException {
        when(mockDao.countAll()).thenThrow(new SQLException("err"));
        assertEquals(0, ctrl.getTotalCount());
    }

    @Test void getPagePremiere() throws SQLException {
        when(mockDao.findPage(10,0)).thenReturn(List.of());
        ctrl.getPage(1,10);
        verify(mockDao).findPage(10,0);
    }

    @Test void getPageDeuxieme() throws SQLException {
        when(mockDao.findPage(10,10)).thenReturn(List.of());
        ctrl.getPage(2,10);
        verify(mockDao).findPage(10,10);
    }
}