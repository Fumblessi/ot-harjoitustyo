/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import hahmogeneraattori.domain.Feat;

/**
 *
 * @author sampo
 */
public class FeatTest {
    
    Feat feat;
    
    public FeatTest() {
    }

    @Before
    public void setUp() {
        this.feat = new Feat(-1, "testiFiitti");
    }
    
    @Test
    public void setIndexIfNotYetSet() {
        this.feat.setId(42);
        assertEquals(42, this.feat.getId());
    }
    
    @Test
    public void doNotSetIndexIfAlreadySet() {
        this.feat.setId(666);
        this.feat.setId(42);
        assertEquals(666, this.feat.getId());
    }

    @Test
    public void statsCanBeSetFromString() {
        this.feat.setStats("DEX/WIS/CHA");
        assertEquals("DEX/WIS/CHA", this.feat.getStats());
    }
}
