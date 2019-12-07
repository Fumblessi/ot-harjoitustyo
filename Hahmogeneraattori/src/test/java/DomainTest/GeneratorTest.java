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
import hahmogeneraattori.domain.Generator;
import hahmogeneraattori.domain.Settings;
import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.domain.Stats;

/**
 *
 * @author sampo
 */
public class GeneratorTest {
    
    private FileSettingsDao settingsDao;
    private Settings settings;
    private Generator generator;
    private Stats stats;
    
    public GeneratorTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.settingsDao = new FileSettingsDao("testSettings.txt");
        this.settings = new Settings(this.settingsDao);
        this.generator = new Generator(this.settings, null);
        this.stats = this.generator.getStats();
    }

    @Test
    public void statRandomizationSumIsCorrect() {
        this.generator.createRandomStats();
        int sum = this.stats.getSum();
        int min = this.settings.getStatPool() - this.settings.getStatVar();
        int max = this.settings.getStatPool() + this.settings.getStatVar();
        
        assertTrue(sum >= min + 3 && sum <= max + 3);
    }
    
    @Test
    public void statRandomizationMinLimitWorks() {
        this.generator.createRandomStats();
        assertTrue(this.stats.getMin() >= this.settings.getStatMin());
    }
    
    @Test
    public void statRandomizationMaxLimitWorks() {
        this.generator.createRandomStats();
        assertTrue(this.stats.getMax() <= this.settings.getStatMax() + 2);
    }
}
