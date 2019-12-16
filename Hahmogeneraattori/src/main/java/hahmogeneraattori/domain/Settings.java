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
        this.settingsDao.setIntValue("StatPool", pool);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden minimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param min uusi arvo
     */
    public void setStatMin(int min) {
        this.settingsDao.setIntValue("StatMin", min);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden maksimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param max uusi arvo
     */
    public void setStatMax(int max) {
        this.settingsDao.setIntValue("StatMax", max);
    }
    
    /**
     * Aseta uusi arvottavien piirteiden varianssin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#setValue(String, int)
     * 
     * @param var uusi arvo
     */
    public void setStatVar(int var) {
        this.settingsDao.setIntValue("StatVar", var);
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
            this.settingsDao.setIntValue("RacialBonus", 1);
        } else {
            this.settingsDao.setIntValue("RacialBonus", 0);
        }
    }
    
    public void setRacialAmount(int amount) {
        this.settingsDao.setIntValue("RacialAmount", amount);
    }
    
    public void setBgSkillsAmount(int amount) {
        this.settingsDao.setIntValue("BgSkillsAmount", amount);
    }
    
    public void setBgOtherAmount(int amount) {
        this.settingsDao.setIntValue("BgOtherAmount", amount);
    }
    
    public void setBgToolChance(double value) {
        this.settingsDao.setDoubleValue("BgToolChance", value);
    }
    
    public void setBgLangChance(double value) {
        this.settingsDao.setDoubleValue("BgLangChance", value);
    }
    
    public void setBgArtisanChance(double value) {
        this.settingsDao.setDoubleValue("BgArtisanChance", value);
    }
    
    public void setBgGamingSetChance(double value) {
        this.settingsDao.setDoubleValue("BgGamingSetChance", value);
    }
    
    public void setBgInstrumentChance(double value) {
        this.settingsDao.setDoubleValue("BgInstrumentChance", value);
    }
    
    public void setLanguageAmount(int amount) {
        this.settingsDao.setIntValue("LanguageAmount", amount);
    }
    
    public void setCommonChance(double value) {
        this.settingsDao.setDoubleValue("CommonChance", value);
    }
    
    public void setRareChance(double value) {
        this.settingsDao.setDoubleValue("RareChance", value);
    }
    
    public void setLegendaryChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryChance", value);
    }
    
    public void setCommonFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonFTC", value);
    }
    
    public void setCommonSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonSTC", value);
    }
    
    public void setCommonThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonTTC", value);
    }
    
    public void setRareFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("RareFTC", value);
    }
    
    public void setRareSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("RareSTC", value);
    }
    
    public void setRareThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("RareTTC", value);
    }
    
    public void setLegendaryFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryFTC", value);
    }
    
    public void setLegendarySecondTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendarySTC", value);
    }
    
    public void setLegendaryThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryTTC", value);
    }
    
    public void setMotherLanguage(boolean value) {
        if (value) {
            this.settingsDao.setIntValue("MotherLanguage", 1);
        } else {
            this.settingsDao.setIntValue("MotherLanguage", 0);
        }
    }
    
    public void setMotherLanguageType(int value) {
        this.settingsDao.setIntValue("MotherLanguageType", value);
    }
    
    public void setMotherLanguageSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("MotherLanguageSTC", value);
    }
    
    public void setMotherLanguageThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("MotherLanguageTTC", value);
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden kokonaissumman arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatPool() {
        return this.settingsDao.getIntValue("StatPool");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden minimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatMin() {
        return this.settingsDao.getIntValue("StatMin");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden maksimin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatMax() {
        return this.settingsDao.getIntValue("StatMax");
    }
    
    /**
     * Hae asetuksista arvottavien piirteiden varianssin arvo
     * 
     * @see hahmogeneraattori.dao.FileSettingsDao#getValue(String)
     * 
     * @return arvo
     */
    public int getStatVar() {
        return this.settingsDao.getIntValue("StatVar");
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
        int value = this.settingsDao.getIntValue("RacialBonus");
        
        if (value > 0) {
            return true;
        }
        return false;
    }
    
    public int getRacialAmount() {
        return this.settingsDao.getIntValue("RacialAmount");
    }
    
    public int getBgSkillsAmount() {
        return this.settingsDao.getIntValue("BgSkillsAmount");
    }
    
    public int getBgOtherAmount() {
        return this.settingsDao.getIntValue("BgOtherAmount");
    }
    
    public double getBgToolChance() {
        return this.settingsDao.getDoubleValue("BgToolChance");
    }
    
    public double getBgLangChance() {
        return this.settingsDao.getDoubleValue("BgLangChance");
    }
    
    public double getBgArtisanChance() {
        return this.settingsDao.getDoubleValue("BgArtisanChance");
    }
    
    public double getBgGamingSetChance() {
        return this.settingsDao.getDoubleValue("BgGamingSetChance");
    }
    
    public double getBgInstrumentChance() {
        return this.settingsDao.getDoubleValue("BgInstrumentChance");
    }
    
    public int getLanguageAmount() {
        return this.settingsDao.getIntValue("LanguageAmount");
    }
    
    public double getCommonChance() {
        return this.settingsDao.getDoubleValue("CommonChance");
    }
    
    public double getRareChance() {
        return this.settingsDao.getDoubleValue("RareChance");
    }
    
    public double getLegendaryChance() {
        return this.settingsDao.getDoubleValue("LegendaryChance");
    }
    
    public double getCommonFirstTierChance() {
        return this.settingsDao.getDoubleValue("CommonFTC");
    }
    
    public double getCommonSecondTierChance() {
        return this.settingsDao.getDoubleValue("CommonSTC");
    }
    
    public double getCommonThirdTierChance() {
        return this.settingsDao.getDoubleValue("CommonTTC");
    }
    
    public double getRareFirstTierChance() {
        return this.settingsDao.getDoubleValue("RareFTC");
    }
    
    public double getRareSecondTierChance() {
        return this.settingsDao.getDoubleValue("RareSTC");
    }
    
    public double getRareThirdTierChance() {
        return this.settingsDao.getDoubleValue("RareTTC");
    }
    
    public double getLegendaryFirstTierChance() {
        return this.settingsDao.getDoubleValue("LegendaryFTC");
    }
    
    public double getLegendarySecondTierChance() {
        return this.settingsDao.getDoubleValue("LegendarySTC");
    }
    
    public double getLegendaryThirdTierChance() {
        return this.settingsDao.getDoubleValue("LegendaryTTC");
    }
    
    public boolean getMotherLanguage() {
        int value = this.settingsDao.getIntValue("MotherLanguage");
        
        if (value > 0) {
            return true;
        }
        return false;
    }
    
    public int getMotherLanguageType() {
        return this.settingsDao.getIntValue("MotherLanguageType");
    }
    
    public double getMotherLanguageSecondTierChance() {
        return this.settingsDao.getDoubleValue("MotherLanguageSTC");
    }
    
    public double getMotherLanguageThirdTierChance() {
        return this.settingsDao.getDoubleValue("MotherLanguageTTC");
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
