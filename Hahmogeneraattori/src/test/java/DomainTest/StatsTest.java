/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import hahmogeneraattori.domain.Stats;

/**
 *
 * @author sampo
 */
public class StatsTest {
    
    Stats stats;
    
    public StatsTest() {
    }
    
    @Before
    public void setUp() {
        int[] stats = new int[6];
        stats[0] = 10;
        stats[1] = 10;
        stats[2] = 10;
        stats[3] = 10;
        stats[4] = 10;
        stats[5] = 10;
        this.stats = new Stats(stats);
    }
    
    @Test
    public void strCanBeSet() {
        this.stats.setStat("str", 19);
        assertEquals(19, this.stats.getStat("str"));
    }
    
    @Test
    public void dexCanBeSet() {
        this.stats.setStat("dex", 20);
        assertEquals(20, this.stats.getStat("dex"));
    }
    
    @Test
    public void conCanBeSet() {
        this.stats.setStat("con", 21);
        assertEquals(21, this.stats.getStat("con"));
    }
    
    @Test
    public void intCanBeSet() {
        this.stats.setStat("inte", 22);
        assertEquals(22, this.stats.getStat("inte"));
    }
    
    @Test
    public void wisCanBeSet() {
        this.stats.setStat("wis", 23);
        assertEquals(23, this.stats.getStat("wis"));
    }
    
    @Test
    public void chaCanBeSet() {
        this.stats.setStat("cha", 24);
        assertEquals(24, this.stats.getStat("cha"));
    }
    
    @Test
    public void statsToStringIsCorrect() {
        this.stats.setStat("str", 1);
        this.stats.setStat("dex", 2);
        this.stats.setStat("con", 3);
        this.stats.setStat("inte", 4);
        this.stats.setStat("wis", 5);
        this.stats.setStat("cha", 6);
        assertEquals("Stats:\n\nSTR: 1\nDEX: 2\nCON: 3\nINT: 4\nWIS: 5\nCHA: 6", 
                this.stats.toString());
    }
}
