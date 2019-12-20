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
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param pool uusi arvo
     */
    public void setStatPool(int pool) {
        this.settingsDao.setIntValue("StatPool", pool);
    }

    /**
     * Aseta uusi arvottavien piirteiden minimin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param min uusi arvo
     */
    public void setStatMin(int min) {
        this.settingsDao.setIntValue("StatMin", min);
    }

    /**
     * Aseta uusi arvottavien piirteiden maksimin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param max uusi arvo
     */
    public void setStatMax(int max) {
        this.settingsDao.setIntValue("StatMax", max);
    }

    /**
     * Aseta uusi arvottavien piirteiden varianssin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param var uusi arvo
     */
    public void setStatVar(int var) {
        this.settingsDao.setIntValue("StatVar", var);
    }

    /**
     * Aseta uusi asetus sille, lisätäänkö arvottuihin piirteisiin rodusta
     * peräisen olevat bonus-piirteet
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
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

    /**
     * Aseta uusi asetus sille, montako rotuominaisuutta arvotaan
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param amount uusi arvo
     */
    public void setRacialAmount(int amount) {
        this.settingsDao.setIntValue("RacialAmount", amount);
    }

    /**
     * Aseta uusi asetus sille, montako taitoa hahmon tausta antaa
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param amount uusi arvo
     */
    public void setBgSkillsAmount(int amount) {
        this.settingsDao.setIntValue("BgSkillsAmount", amount);
    }

    /**
     * Aseta uusi asetus sille, montako muuta taitoa hahmon tausta antaa
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param amount uusi arvo
     */
    public void setBgOtherAmount(int amount) {
        this.settingsDao.setIntValue("BgOtherAmount", amount);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä tausta antaa työkalun
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setBgToolChance(double value) {
        this.settingsDao.setDoubleValue("BgToolChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä tausta antaa kielen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setBgLangChance(double value) {
        this.settingsDao.setDoubleValue("BgLangChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä tausta antaa työkalun
     * alatyypin 'Artisan's Tools'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setBgArtisanChance(double value) {
        this.settingsDao.setDoubleValue("BgArtisanChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä tausta antaa työkalun
     * alatyypin 'Gaming Set'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setBgGamingSetChance(double value) {
        this.settingsDao.setDoubleValue("BgGamingSetChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä tausta antaa työkalun
     * alatyypin 'Instrument'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setBgInstrumentChance(double value) {
        this.settingsDao.setDoubleValue("BgInstrumentChance", value);
    }

    /**
     * Aseta uusi asetus sille, montako kieltä hahmo pohjimmiltaan osaa
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param amount uusi arvo
     */
    public void setLanguageAmount(int amount) {
        this.settingsDao.setIntValue("LanguageAmount", amount);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu kieli
     * on harvinaisuudeltaan yleinen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setCommonChance(double value) {
        this.settingsDao.setDoubleValue("CommonChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu kieli
     * on harvinaisuudeltaan harvinainen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setRareChance(double value) {
        this.settingsDao.setDoubleValue("RareChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu kieli
     * on harvinaisuudeltaan legendaarinen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setLegendaryChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryChance", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * yleinen kieli on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setCommonFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonFTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * yleinen kieli on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setCommonSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonSTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * yleinen kieli on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setCommonThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("CommonTTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * harvinainen kieli on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setRareFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("RareFTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * harvinainen kieli on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setRareSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("RareSTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * harvinainen kieli on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setRareThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("RareTTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * legendaarinen kieli on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setLegendaryFirstTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryFTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * legendaarinen kieli on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setLegendarySecondTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendarySTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmolle arvottu
     * yleinen kieli on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setLegendaryThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("LegendaryTTC", value);
    }

    /**
     * Aseta uusi asetus sille, saako hahmo äidinkielen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param value uusi arvo
     */
    public void setMotherLanguage(boolean value) {
        if (value) {
            this.settingsDao.setIntValue("MotherLanguage", 1);
        } else {
            this.settingsDao.setIntValue("MotherLanguage", 0);
        }
    }

    /**
     * Aseta uusi asetus sille, minkä tyyppisen äidinkielen hahmo saa
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setIntValue(String, int)
     *
     * @param value uusi arvo
     */
    public void setMotherLanguageType(int value) {
        this.settingsDao.setIntValue("MotherLanguageType", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmon äidinkieli on
     * taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setMotherLanguageSecondTierChance(double value) {
        this.settingsDao.setDoubleValue("MotherLanguageSTC", value);
    }

    /**
     * Aseta uusi asetus sille, millä todennäköisyydellä hahmon äidinkieli on
     * taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#setDoubleValue(String, double)
     *
     * @param value uusi arvo
     */
    public void setMotherLanguageThirdTierChance(double value) {
        this.settingsDao.setDoubleValue("MotherLanguageTTC", value);
    }

    /**
     * Hae asetuksista arvottavien piirteiden kokonaissumman arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getStatPool() {
        return this.settingsDao.getIntValue("StatPool");
    }

    /**
     * Hae asetuksista arvottavien piirteiden minimin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getStatMin() {
        return this.settingsDao.getIntValue("StatMin");
    }

    /**
     * Hae asetuksista arvottavien piirteiden maksimin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getStatMax() {
        return this.settingsDao.getIntValue("StatMax");
    }

    /**
     * Hae asetuksista arvottavien piirteiden varianssin arvo
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getStatVar() {
        return this.settingsDao.getIntValue("StatVar");
    }

    /**
     * Hae asetuksista, lisätäänkö arvottuihin piirteisin rodusta peräisin
     * olevat bonus-piirteet. Lisäksi metodi muuttaa tiedostossa olevan 0 tai 1
     * arvon booleaniksi
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
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

    /**
     * Hae asetuksista arvottavien rotuominaisuuksien määrä
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getRacialAmount() {
        return this.settingsDao.getIntValue("RacialAmount");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavien taitojen määrä
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getBgSkillsAmount() {
        return this.settingsDao.getIntValue("BgSkillsAmount");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavien muiden taitojen määrä
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getBgOtherAmount() {
        return this.settingsDao.getIntValue("BgOtherAmount");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavan työkalutaidon todennäköisyys
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getBgToolChance() {
        return this.settingsDao.getDoubleValue("BgToolChance");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavan kielitaidon todennäköisyys
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getBgLangChance() {
        return this.settingsDao.getDoubleValue("BgLangChance");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavan työkalutaidon alatyypin
     * 'Artisan's Tools' todennäköisyys
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getBgArtisanChance() {
        return this.settingsDao.getDoubleValue("BgArtisanChance");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavan työkalutaidon alatyypin
     * 'Gaming Set' todennäköisyys
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getBgGamingSetChance() {
        return this.settingsDao.getDoubleValue("BgGamingSetChance");
    }

    /**
     * Hae asetuksista taustan pohjalta arvottavan työkalutaidon alatyypin
     * 'Instrument' todennäköisyys
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getBgInstrumentChance() {
        return this.settingsDao.getDoubleValue("BgInstrumentChance");
    }

    /**
     * Hae asetuksista hahmon pohjimmiltaan osaamien kielten määrä
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getLanguageAmount() {
        return this.settingsDao.getIntValue("LanguageAmount");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu kieli on
     * harvinaisuudeltaan yleinen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getCommonChance() {
        return this.settingsDao.getDoubleValue("CommonChance");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu kieli on
     * harvinaisuudeltaan harvinainen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getRareChance() {
        return this.settingsDao.getDoubleValue("RareChance");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu kieli on
     * harvinaisuudeltaan legendaarinen
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getLegendaryChance() {
        return this.settingsDao.getDoubleValue("LegendaryChance");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu yleinen kieli
     * on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getCommonFirstTierChance() {
        return this.settingsDao.getDoubleValue("CommonFTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu yleinen kieli
     * on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getCommonSecondTierChance() {
        return this.settingsDao.getDoubleValue("CommonSTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu yleinen kieli
     * on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getCommonThirdTierChance() {
        return this.settingsDao.getDoubleValue("CommonTTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu harvinainen
     * kieli on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getRareFirstTierChance() {
        return this.settingsDao.getDoubleValue("RareFTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu harvinainen
     * kieli on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getRareSecondTierChance() {
        return this.settingsDao.getDoubleValue("RareSTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu harvinainen
     * kieli on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getRareThirdTierChance() {
        return this.settingsDao.getDoubleValue("RareTTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu legendaarinen
     * kieli on taitotasoltaan 'I'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getLegendaryFirstTierChance() {
        return this.settingsDao.getDoubleValue("LegendaryFTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu legendaarinen
     * kieli on taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getLegendarySecondTierChance() {
        return this.settingsDao.getDoubleValue("LegendarySTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle arvottu harvinainen
     * kieli on taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getLegendaryThirdTierChance() {
        return this.settingsDao.getDoubleValue("LegendaryTTC");
    }

    /**
     * Hae asetuksista tieto siitä, asetetaanko hahmolle äidinkieli
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public boolean getMotherLanguage() {
        int value = this.settingsDao.getIntValue("MotherLanguage");

        if (value > 0) {
            return true;
        }
        return false;
    }

    /**
     * Hae asetuksista hahmolle asetettavan äidinkielen tyyppi
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getIntValue(String)
     *
     * @return arvo
     */
    public int getMotherLanguageType() {
        return this.settingsDao.getIntValue("MotherLanguageType");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle asetettu äidinkieli on
     * taitotasoltaan 'II'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
    public double getMotherLanguageSecondTierChance() {
        return this.settingsDao.getDoubleValue("MotherLanguageSTC");
    }

    /**
     * Hae asetuksista, millä todennäköisyydellä hahmolle asetettu äidinkieli on
     * taitotasoltaan 'III'
     *
     * @see hahmogeneraattori.dao.FileSettingsDao#getDoubleValue(String)
     *
     * @return arvo
     */
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
