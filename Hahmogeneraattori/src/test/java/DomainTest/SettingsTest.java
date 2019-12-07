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
import hahmogeneraattori.domain.Settings;
import hahmogeneraattori.dao.FileSettingsDao;

/**
 *
 * @author sampo
 */
public class SettingsTest {
    
    Settings settings;
    FileSettingsDao settingsDao;
    
    public SettingsTest() {
    }
    
    
    @Before
    public void setUp() throws Exception {
        this.settingsDao = new FileSettingsDao("testSettings.txt");
        this.settings = new Settings(this.settingsDao);
    }

    @Test
    public void statPoolCanBeModified() {
        this.settings.setStatPool(50);
        assertEquals(50, this.settings.getStatPool());
    }
    
    @Test
    public void statVarCanBeModified() {
        this.settings.setStatVar(7);
        assertEquals(7, this.settings.getStatVar());
    }
    
    @Test
    public void statMinCanBeModified() {
        this.settings.setStatMin(2);
        assertEquals(2, this.settings.getStatMin());
    }
    
    @Test
    public void statMaxCanBeModified() {
        this.settings.setStatMax(15);
        assertEquals(15, this.settings.getStatMax());
    }
    
    @Test
    public void modificationsAreSaved() throws Exception {
        this.settings.setStatPool(62);
        this.settings.update();
        FileSettingsDao newSettingsDao = new FileSettingsDao("testSettings.txt");
        Settings newSettings = new Settings(newSettingsDao);
        assertEquals(62, newSettings.getStatPool());
        this.settings.setStatPool(75);
        this.settings.update();
    }
}
