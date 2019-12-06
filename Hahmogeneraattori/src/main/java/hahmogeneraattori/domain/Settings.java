/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import hahmogeneraattori.dao.SettingsDao;
import hahmogeneraattori.dao.GeneratorDatabaseDao;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author sampo
 */
public class Settings {
    
    private SettingsDao settingsDao;
    private GeneratorDatabaseDao skillDao;
    
    public Settings(SettingsDao settingsDao, GeneratorDatabaseDao skillDao) {
        this.settingsDao = settingsDao;
        this.skillDao = skillDao;
    }
    
    public void setStatPool(int pool) {
        this.settingsDao.setValue("StatPool", pool);
    }
    
    public void setStatMin(int min) {
        this.settingsDao.setValue("StatMin", min);
    }
    
    public void setStatMax(int max) {
        this.settingsDao.setValue("StatMax", max);
    }
    
    public void setStatVar(int var) {
        this.settingsDao.setValue("StatVar", var);
    }
    
    public void setRacialBonus(boolean value) {
        if (value) {
            this.settingsDao.setValue("RacialBonus", 1);
        } else {
            this.settingsDao.setValue("RacialBonus", 0);
        }
    }
    
    public int getStatPool() {
        return this.settingsDao.getValue("StatPool");
    }
    
    public int getStatMin() {
        return this.settingsDao.getValue("StatMin");
    }
    
    public int getStatMax() {
        return this.settingsDao.getValue("StatMax");
    }
    
    public int getStatVar() {
        return this.settingsDao.getValue("StatVar");
    }
    
    public boolean getRacialBonus() {
        int value = this.settingsDao.getValue("RacialBonus");
        
        if (value > 0) {
            return true;
        }
        return false;
    }
    
    public void addNewProfToDB(Proficiency prof) throws SQLException {
        this.skillDao.create(prof);
    }
    
    public List<Proficiency> listAllProfs() {
        return this.skillDao.list();
    }
    
    public void update() throws Exception {
        this.settingsDao.update();
    }
}
