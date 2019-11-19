/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.domain;

import Hahmogeneraattori.dao.FileSettingsDao;
/**
 *
 * @author sampo
 */
public class Settings {
    
    private FileSettingsDao settingsDao;
    
    public Settings(FileSettingsDao settingsDao) {
        this.settingsDao = settingsDao;
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
    
    public int getStatPool() {
        return this.settingsDao.getValue("StatPool");
    }
    
    public int getStatMin() {
        return this.settingsDao.getValue("StatMin");
    }
    
    public int getStatMax() {
        return this.settingsDao.getValue("StatMax");
    }
    
    public void update() throws Exception {
        this.settingsDao.update();
    }
}
