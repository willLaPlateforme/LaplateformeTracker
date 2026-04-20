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
import static org.mockito.Mockito.*;

@DisplayName("SearchController")
@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock private StudentDAO mockDao;
    private SearchController ctrl;

    @BeforeEach
    void setUp() throws Exception {
        ctrl = new SearchController();
        var f = SearchController.class.getDeclaredField("dao");
        f.setAccessible(true);
        f.set(ctrl, mockDao);
    }

    @Test
    void transmetParams() throws SQLException {
        when(mockDao.advancedSearch(
                null, null, null, null,
                18, 25,
                10.0, 20.0
        )).thenReturn(List.of());

        ctrl.advancedSearch(null, null, null, null, 18, 25, 10.0, 20.0);

        verify(mockDao).advancedSearch(
                null, null, null, null,
                18, 25,
                10.0, 20.0
        );
    }

    @Test
    void transmetParamsNull() throws SQLException {
        when(mockDao.advancedSearch(
                null, null, null, null,
                null, null,
                null, null
        )).thenReturn(List.of());

        ctrl.advancedSearch(null, null, null, null, null, null, null, null);

        verify(mockDao).advancedSearch(
                null, null, null, null,
                null, null,
                null, null
        );
    }

    @Test
    void retourneResultats() throws SQLException {
        List<Student> exp = List.of(new Student("Alice","Martin",20,15.0,"alice@mail.com"));

        when(mockDao.advancedSearch(
                null, null, null, null,
                18, 23,
                14.0, null
        )).thenReturn(exp);

        assertEquals(1, ctrl.advancedSearch(
                null, null, null, null,
                18, 23,
                14.0, null
        ).size());
    }

    @Test
    void retourneVideSiException() throws SQLException {
        when(mockDao.advancedSearch(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new SQLException("err"));

        assertTrue(ctrl.advancedSearch(
                null, null, null, null,
                18, 25,
                null, null
        ).isEmpty());
    }

    @Test
    void seulementMinAge() throws SQLException {
        when(mockDao.advancedSearch(
                null, null, null, null,
                20, null,
                null, null
        )).thenReturn(List.of());

        ctrl.advancedSearch(null, null, null, null, 20, null, null, null);

        verify(mockDao).advancedSearch(
                null, null, null, null,
                20, null,
                null, null
        );
    }
}
