/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import org.junit.Before;
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
    public void booleanValueCanBeModified() {
        this.settings.setRacialBonus(false);
        assertFalse(this.settings.getRacialBonus());
    }
    
    @Test
    public void intValueCanBeModified() {
        this.settings.setLanguageAmount(6);
        assertEquals(6, this.settings.getLanguageAmount());
    }
    
    @Test
    public void doubleValueCanBeModified() {
        this.settings.setCommonChance(12.0);
        assertEquals(12.0, this.settings.getCommonChance(), 0);
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
