package com.laplateforme.tracker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validator")
class ValidatorTest {

    @Test void valideComplet()    { assertNull(Validator.validateStudent("Alice","Martin","21","15.5","alice@mail.com")); }
    @Test void valideSansEmail()  { assertNull(Validator.validateStudent("Bob","Dupont","18","12.0","")); }
    @Test void valideEmailNull()  { assertNull(Validator.validateStudent("Clara","Bernard","25","20.0",null)); }
    @Test void noteVirgule()      { assertNull(Validator.validateStudent("D","L","22","14,5",null)); }
    @Test void noteZero()         { assertNull(Validator.validateStudent("A","B","19","0",null)); }
    @Test void noteVingt()        { assertNull(Validator.validateStudent("A","B","19","20",null)); }
    @Test void ageUn()            { assertNull(Validator.validateStudent("A","B","1","10",null)); }
    @Test void ageCentCinquante() { assertNull(Validator.validateStudent("A","B","150","10",null)); }

    @Test void prenomVide()       { assertNotNull(Validator.validateStudent("","Martin","21","15",null)); }
    @Test void prenomNull()       { assertNotNull(Validator.validateStudent(null,"Martin","21","15",null)); }
    @Test void prenomEspaces()    { assertNotNull(Validator.validateStudent("   ","Martin","21","15",null)); }
    @Test void nomVide()          { assertNotNull(Validator.validateStudent("Alice","","21","15",null)); }
    @Test void nomNull()          { assertNotNull(Validator.validateStudent("Alice",null,"21","15",null)); }

    @Test void ageZero()          { assertNotNull(Validator.validateStudent("A","B","0","15",null)); }
    @Test void ageNegatif()       { assertNotNull(Validator.validateStudent("A","B","-5","15",null)); }
    @Test void ageTropGrand()     { assertNotNull(Validator.validateStudent("A","B","151","15",null)); }
    @Test void ageTexte()         { assertNotNull(Validator.validateStudent("A","B","abc","15",null)); }
    @Test void ageDecimal()       { assertNotNull(Validator.validateStudent("A","B","21.5","15",null)); }
    @Test void ageVide()          { assertNotNull(Validator.validateStudent("A","B","","15",null)); }

    @Test void noteNegative()     { assertNotNull(Validator.validateStudent("A","B","21","-1",null)); }
    @Test void noteTropGrande()   { assertNotNull(Validator.validateStudent("A","B","21","21",null)); }
    @Test void noteTexte()        { assertNotNull(Validator.validateStudent("A","B","21","super",null)); }
    @Test void noteVide()         { assertNotNull(Validator.validateStudent("A","B","21","",null)); }

    @Test void emailSansArobase() { assertNotNull(Validator.validateStudent("A","B","21","15","alicemail.com")); }
    @Test void emailSansPoint()   { assertNotNull(Validator.validateStudent("A","B","21","15","alice@mailcom")); }
    @Test void emailVideOk()      { assertNull(Validator.validateStudent("Alice","Martin","21","15","")); }

    @Test
    void ordreValidation() {
        String r = Validator.validateStudent("","Martin","0","15",null);
        assertNotNull(r);
        assertTrue(r.toLowerCase().contains("pr"));
    }
}