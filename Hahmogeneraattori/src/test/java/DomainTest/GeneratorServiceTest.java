/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import hahmogeneraattori.domain.GeneratorService;
import hahmogeneraattori.domain.Settings;
import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.domain.Stats;

/**
 *
 * @author sampo
 */
public class GeneratorServiceTest {
    
    private FileSettingsDao settingsDao;
    private Settings settings;
    private GeneratorService generator;
    
    public GeneratorServiceTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.settingsDao = new FileSettingsDao("testSettings.txt");
        this.settings = new Settings(this.settingsDao);
        this.generator = new GeneratorService(this.settings, null);
    }   
}
