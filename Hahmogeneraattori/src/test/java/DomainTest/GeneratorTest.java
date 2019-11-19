/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Hahmogeneraattori.domain.Generator;
import Hahmogeneraattori.domain.Settings;
import Hahmogeneraattori.dao.FileSettingsDao;

/**
 *
 * @author sampo
 */
public class GeneratorTest {
    
    private FileSettingsDao settingsDao;
    private Settings settings;
    private Generator generator;
    
    public GeneratorTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.settingsDao = new FileSettingsDao("testSettings.txt");
        this.settings = new Settings(this.settingsDao);
        this.generator = new Generator(this.settings);
    }

    @Test
    public void statRandomizationSumIsCorrect() {
        int[] stats = this.generator.createRandomStats();
        int sum = 0;
        for (int i = 0; i < stats.length; i++) {
            sum += stats[i];
        }
        assertEquals(sum, this.settings.getStatPool());
    }
    
    @Test
    public void statRandomizationMinLimitWorks() {
        int[] stats = this.generator.createRandomStats();
        boolean allStatsAreAboveMin = true;
        for (int i = 0; i < stats.length; i++) {
            if (stats[i] < this.settings.getStatMin()) {
                allStatsAreAboveMin = false;
            }
        }
        assertTrue(allStatsAreAboveMin);
    }
    
    @Test
    public void statRandomizationMaxLimitWorks() {
        int[] stats = this.generator.createRandomStats();
        boolean allStatsAreBelowMax = true;
        for (int i = 0; i < stats.length; i++) {
            if (stats[i] > this.settings.getStatMax()) {
                allStatsAreBelowMax = false;
            }
        }
        assertTrue(allStatsAreBelowMax);
    }
}