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
    
    @Test
    public void statSumIsCorrect() {
        this.stats.setStat("str", 11);
        assertEquals(61, this.stats.getSum());
    }
    
    @Test
    public void statMinIsCorrect() {
        this.stats.setStat("wis", 7);
        assertEquals(7, this.stats.getMin());
    }
    
    @Test
    public void statMaxIsCorrect() {
        this.stats.setStat("con", 39);
        assertEquals(39, this.stats.getMax());
    }
}
