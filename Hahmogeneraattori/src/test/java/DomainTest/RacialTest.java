/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import hahmogeneraattori.domain.Racial;
import hahmogeneraattori.domain.Proficiency;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author sampo
 */
public class RacialTest {
    
    Racial racial;
    
    public RacialTest() {
    }
    /*
    @Before
    public void setUp() {
        this.racial = new Racial(-1, "testiRasiaali", -1, true);
        this.racial.addRacialProf(new Proficiency("prof1", "type1"));
        this.racial.addRacialProf(new Proficiency("prof2", "type2"));
        this.racial.addRacialProf(new Proficiency("prof3", "type3"));
    }
    
    @Test
    public void setIndexIfNotYetSet() {
        this.racial.setId(42);
        assertEquals(42, this.racial.getId());
    }
    
    @Test
    public void doNotSetIndexIfAlreadySet() {
        this.racial.setId(666);
        this.racial.setId(42);
        assertEquals(666, this.racial.getId());
    }
    
    @Test
    public void nameCanBeSet() {
        this.racial.setName("uusiNimi");
        assertEquals("uusiNimi", this.racial.getName());
    }
    
    @Test
    public void statsCanBeSet() {
        this.racial.setStats(6);
        assertEquals(6, this.racial.getStats());
    }
    
    @Test
    public void featCanBeSet() {
        this.racial.setFeat(false);
        assertFalse(this.racial.getFeat());
    }
    
    @Test
    public void proficienciesCanBeSet() {
        List<Proficiency> newProfs = new ArrayList<>();
        this.racial.setRacialProfs(newProfs);
        assertEquals(newProfs, this.racial.getRacialProfs());
    }
    
    @Test
    public void proficienciesCanBeAdded() {
        Proficiency prof = new Proficiency("kukkuu", "404");
        this.racial.addRacialProf(prof);
        assertTrue(this.racial.getRacialProfs().contains(prof));
    }
    
    @Test
    public void twoRacialsAreSameIfNamesAreSame() {
        Racial otherRacial = new Racial(73, "testiRasiaali", 16, false);
        assertTrue(this.racial.equals(otherRacial));
    }
    
    @Test
    public void twoRacialsAreNotSameIfNamesAreDifferent() {
        Racial otherRacial = new Racial(-1, "testRasiaali2", -1, true);
        assertFalse(this.racial.equals(otherRacial));
    }*/
}
