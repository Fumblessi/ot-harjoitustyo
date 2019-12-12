/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import hahmogeneraattori.domain.Proficiency;

/**
 *
 * @author sampo
 */
public class ProficiencyTest {
    
    Proficiency prof;
    
    public ProficiencyTest() {
    }
    /*
    @Before
    public void setUp() {
        this.prof = new Proficiency(-1, "testiProf", "Tool");
    }

    @Test
    public void setIndexIfNotYetSet() {
        this.prof.setId(42);
        assertEquals(42, this.prof.getId());
    }
    
    @Test
    public void doNotSetIndexIfAlreadySet() {
        this.prof.setId(666);
        this.prof.setId(42);
        assertEquals(666, this.prof.getId());
    }
    
    @Test
    public void nameCanBeSet() {
        this.prof.setName("pum");
        assertEquals("pum", this.prof.getName());
    }
    
    @Test
    public void typeCanBeSet() {
        this.prof.setType("paistinpannu");
        assertEquals("paistinpannu", this.prof.getType());
    }
    
    @Test
    public void twoProficienciesAreSameIfNamesMatch() {
        Proficiency otherProf = new Proficiency(58, "testiProf", "Skill");
        assertTrue(this.prof.equals(otherProf));
    }
    
    @Test
    public void twoProficienciesAreNotSameIfNamesMismatch() {
        Proficiency otherProf = new Proficiency(-1, "testiPuf", "Tool");
        assertFalse(this.prof.equals(otherProf));
    }*/
}
