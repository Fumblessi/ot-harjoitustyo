/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import hahmogeneraattori.dao.SettingsDao;
import java.util.List;

/**
 * Luokka hoitaa asetusten asettamisen ja hakemisen
 * 
 * @author sampo
 */
public class Settings {
    
    private SettingsDao settingsDao;
    
    /**
     * @see hahmogeneraattori.dao.SettingsDao
     * 
     * @param settingsDao yhteys tallennettuihin asetuksiin
     */
    public Settings(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }
    
    /**
     * Aseta uusi arvottavien piirteiden kokonaissumman arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param pool uusi arvo
     */
    public void setStatPool(int pool) {
        this.settingsDao.setValue("StatPool", pool);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden minimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param min uusi arvo
     */
    public void setStatMin(int min) {
        this.settingsDao.setValue("StatMin", min);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden maksimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param max uusi arvo
     */
    public void setStatMax(int max) {
        this.settingsDao.setValue("StatMax", max);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden varianssin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param var uusi arvo
     */
    public void setStatVar(int var) {
        this.settingsDao.setValue("StatVar", var);
    }
    
    /**
     * Aseta uusi asetus sille, lisätäänkö arvottuihin piirteisiin
     * rodusta peräisen olevat bonus-piirteet
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param value uusi arvo
     */
    public void setRacialBonus(boolean value) {
        if (value) {
            this.settingsDao.setValue("RacialBonus", 1);
        } else {
            this.settingsDao.setValue("RacialBonus", 0);
        }
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden kokonaissumman arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatPool() {
        return this.settingsDao.getValue("StatPool");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden minimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatMin() {
        return this.settingsDao.getValue("StatMin");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden maksimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatMax() {
        return this.settingsDao.getValue("StatMax");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden varianssin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatVar() {
        return this.settingsDao.getValue("StatVar");
    }
    
    /**
     * Hae asetuksista, lisätäänkö arvottuihin piirteisin rodusta peräisin olevat
     * bonus-piirteet. Lisäksi metodi muuttaa tiedostossa olevan 0 tai 1 arvon 
     * booleaniksi
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public boolean getRacialBonus() {
        int value = this.settingsDao.getValue("RacialBonus");
        
        if (value > 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Päivitetään nykyiset asetukset asetus-tiedostoon
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#update()
     * 
     * @throws Exception 
     */
    public void update() throws Exception {
        this.settingsDao.update();
    }
}
