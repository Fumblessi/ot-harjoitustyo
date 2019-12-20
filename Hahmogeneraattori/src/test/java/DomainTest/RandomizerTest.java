/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomainTest;

import hahmogeneraattori.dao.FileSettingsDao;
import hahmogeneraattori.domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sampo
 */
public class RandomizerTest {
    
    Randomizer randomizer;
    Settings settings;
    List<Proficiency> profs;
    
    public RandomizerTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        FileSettingsDao settingsDao = new FileSettingsDao("testSettings.txt");
        this.settings = new Settings(settingsDao);
        this.profs = new ArrayList<>();
        this.randomizer = new Randomizer(this.settings, this.profs);
    }

    @Test
    public void statRandomizationSumIsCorrect() {
        ArrayList<Racial> racials = new ArrayList<>();
        this.settings.setRacialBonus(false);
        this.settings.setStatVar(0);
        Stats stats = new Stats(this.randomizer.randomizeStats(racials));
        int sum = stats.getSum();
        
        assertEquals(sum, this.settings.getStatPool());
    }
    
    @Test
    public void statRandomizationRacialBonusIsAdded() {
        ArrayList<Racial> racials = new ArrayList<>();
        this.settings.setRacialBonus(false);
        this.settings.setStatVar(0);
        Stats stats1 = new Stats(this.randomizer.randomizeStats(racials));
        int sum1 = stats1.getSum();
        this.settings.setRacialBonus(true);
        Stats stats2 = new Stats(this.randomizer.randomizeStats(racials));
        int sum2 = stats2.getSum();
        
        assertEquals(sum1 + 3, sum2);
    }
    
    @Test
    public void statRandomizationRacialStatsAreAdded() {
        ArrayList<Racial> racials = new ArrayList<>();
        racials.add(new Racial(-1, "Voimakas", 3, false, 0, 0, 0, ""));
        racials.add(new Racial(-1, "Heikko", -5, false, 0, 0, 0, ""));
        this.settings.setRacialBonus(false);
        this.settings.setStatVar(0);
        
        Stats stats = new Stats(this.randomizer.randomizeStats(racials));
        int sum = stats.getSum();
        
        assertEquals(sum, this.settings.getStatPool() - 2);
    }
    
    @Test
    public void statRandomizationMinLimitWorks() {
        ArrayList<Racial> racials = new ArrayList<>();
        this.settings.setRacialBonus(false);
        Stats stats = new Stats(this.randomizer.randomizeStats(racials));
        assertTrue(stats.getMin() >= this.settings.getStatMin());
    }
    
    @Test
    public void statRandomizationMaxLimitWorks() {
        ArrayList<Racial> racials = new ArrayList<>();
        this.settings.setRacialBonus(false);
        Stats stats = new Stats(this.randomizer.randomizeStats(racials));
        assertTrue(stats.getMax() <= this.settings.getStatMax());
    }
    
    @Test
    public void getRandomLangWorksEvenIfLangPoolIsEmpty() {
        HashMap<Proficiency, String> langs = this.randomizer.getRandomLangs();
        
        assertTrue(langs.containsKey(new Proficiency("-", "Language", "")));
    }
    
    @Test
    public void getTwoRandomLangsGivesTwoLanguages() {
        this.randomizer.setLangCount(2);
        this.settings.setMotherLanguage(false);
        this.profs.add(new Proficiency("kieli1", "Language", "Common"));
        this.profs.add(new Proficiency("kieli2", "Language", "Rare"));
        this.profs.add(new Proficiency("kieli3", "Language", "Legendary"));
        HashMap<Proficiency, String> langs = this.randomizer.getRandomLangs();
        
        assertEquals(2, langs.size());
    }
    
    @Test
    public void getMotherLanguageGivesMotherLanguage() {
        this.randomizer.setLangCount(1);
        this.settings.setMotherLanguage(true);
        this.settings.setMotherLanguageType(1);
        Proficiency motherLang = new Proficiency("Argan", "Language", "Common");
        this.profs.add(motherLang);
        this.profs.add(new Proficiency("kieli2", "Language", "Rare"));
        this.profs.add(new Proficiency("kieli3", "Language", "Legendary"));
        HashMap<Proficiency, String> langs = this.randomizer.getRandomLangs();
        
        assertTrue(langs.containsKey(motherLang));
    }
    
    @Test
    public void getRandomLangByRarityGivesRightRarity() {
        HashMap<Proficiency, String> langs = new HashMap<>();
        this.profs.add(new Proficiency("Argan", "Language", "Common"));
        this.profs.add(new Proficiency("Korgan", "Language", "Rare"));
        this.profs.add(new Proficiency("Margan", "Language", "Legendary"));
        Proficiency lang = this.randomizer.getRandomLangByRarity(langs, "Rare");
        
        assertEquals("Rare", lang.getSubtype());
    }
    
    @Test
    public void languageAddingIsDoneIfAllLangsHaveBeenObtained() {
        this.randomizer.setLangCount(35);
        Proficiency prof1 = new Proficiency("Argan", "Language", "Common");
        Proficiency prof2 = new Proficiency("Korgan", "Language", "Rare");
        Proficiency prof3 = new Proficiency("Margan", "Language", "Legendary");
        this.profs.add(prof1);
        this.profs.add(prof2);
        this.profs.add(prof3);
        HashMap<Proficiency, String> langs = this.randomizer.getRandomLangs();
        
        assertEquals(3, langs.size());
    }
    
    @Test
    public void getRandomClassWorksEvenIfClassesIsEmpty() {
        ArrayList<RpgClass> allClasses = new ArrayList<>();
        
        assertTrue(this.randomizer.getRandomClass(allClasses).getName().equals("-"));
    }
    
    @Test
    public void getRandomClassGivesClass() {
        ArrayList<RpgClass> allClasses = new ArrayList<>();
        allClasses.add(new RpgClass(-1, "Soturi", 0, 0, 0, ""));
        allClasses.add(new RpgClass(-1, "Taistelija", 0, 0, 0, ""));
        allClasses.add(new RpgClass(-1, "Velho", 0, 0, 0, ""));
        
        RpgClass randomClass = this.randomizer.getRandomClass(allClasses);
        
        assertTrue(randomClass.getName().equals("Soturi") || 
                randomClass.getName().equals("Taistelija") || 
                randomClass.getName().equals("Velho"));
    }
    
    @Test
    public void getRandomSubclassWorksEvenIfSubclassesIsEmpty() {
        ArrayList<String> subclasses = new ArrayList<>();
        
        assertTrue(this.randomizer.getRandomSubclass(subclasses).equals("-"));
    }
    @Test
    public void getRandomSubclassGivesSubclass() {
        RpgClass rpg = new RpgClass(-1, "Soturi", 0, 0, 0, "");
        rpg.addSubclass("Miekkailija");
        rpg.addSubclass("Vasaroija");
        rpg.addSubclass("Hutkija");
        
        String randomSubclass = this.randomizer.getRandomSubclass(rpg.getSubclasses());
        
        assertTrue(randomSubclass.equals("Miekkailija") || 
                randomSubclass.equals("Vasaroija") || randomSubclass.equals("Hutkija"));
    }
    
    @Test
    public void getRandomBackgroundWorksEvenIfBackgroundsIsEmpty() {
        ArrayList<Background> bgs = new ArrayList<>();
        
        assertTrue(this.randomizer.getRandomBackground(bgs).getName().equals("-"));
    }
    
    @Test
    public void getRandomBackgroundGivesBackground() {
        ArrayList<Background> allBgs = new ArrayList<>();
        allBgs.add(new Background(-1, "Soturi", ""));
        allBgs.add(new Background(-1, "Työukko", ""));
        allBgs.add(new Background(-1, "Maalari", ""));
        
        Background randomBg = this.randomizer.getRandomBackground(allBgs);
        
        assertTrue(randomBg.getName().equals("Soturi") || 
                randomBg.getName().equals("Työukko") || 
                randomBg.getName().equals("Maalari"));
    }
    
    @Test
    public void getRandomRacialsWorksEvenIfRacialsIsEmpty() {
        ArrayList<Racial> racials = new ArrayList<>();
        
        assertTrue(this.randomizer.getRandomRacials(15, racials, 
                new RpgClass(-1, "Aake", 0, 0, 0, "")).isEmpty());
    }
    
    @Test
    public void getTwoRandomRacialsGivesTwoRacials() {
        ArrayList<Racial> allRacials = new ArrayList<>();
        allRacials.add(new Racial(-1, "Voimaa", 0, false, 0, 0, 0, "Skill"));
        allRacials.add(new Racial(-1, "Tahtoa", 0, false, 0, 0, 0, "Tool"));
        allRacials.add(new Racial(-1, "Intoa", 0, true, 4, 2, 0, "Language"));
        
        ArrayList<Racial> newRacials = this.randomizer.getRandomRacials(2, allRacials, 
                new RpgClass(-1, "soturi", 0, 0, 0, "Tool"));
        
        assertEquals(2, newRacials.size());
    }
    
    @Test
    public void illegalRacialIsNotGiven() {
        ArrayList<Racial> allRacials = new ArrayList<>();
        Proficiency prof = new Proficiency(-1, "Acrobatics", "Skill", "");
        Racial tahto = new Racial(-1, "Tahtoa", 0, false, 0, 0, 0, "Tool");
        tahto.addCertainProf(prof);
        
        RpgClass tahtoluokka = new RpgClass(-1, "Soturi", 0, 0, 0, "");
        tahtoluokka.addCertainProf(prof);
        
        allRacials.add(tahto);
        allRacials.add(new Racial(-1, "Voimaa", 0, false, 0, 0, 0, "Skill"));
        allRacials.add(new Racial(-1, "Intoa", 0, true, 4, 2, 0, "Language"));
        
        ArrayList<Racial> racials = this.randomizer.getRandomRacials(3, allRacials, 
                tahtoluokka);
        assertFalse(racials.contains(tahto));
    }
    
    @Test
    public void addUncertainClassProfsRightAmountIsGiven() {
        ArrayList<Proficiency> classProfs = new ArrayList<>();
        RpgClass rpg = new RpgClass(-1, "Soturi", 1, 0, 0, "");
        rpg.addUncertainProf(new Proficiency("Taistelutahto", "", ""));
        rpg.addUncertainProf(new Proficiency("Pakenemistahto", "", ""));
        rpg.addUncertainProf(new Proficiency("Huhhuh", "", ""));
        
        this.randomizer.addUncertainClassProfs(rpg, classProfs);
        
        assertEquals(1, classProfs.size());
    }
    
    @Test
    public void classIfAllUncertainIsAddedRandomSkillIsGiven() {
        ArrayList<Proficiency> classProfs = new ArrayList<>();
        RpgClass rpg = new RpgClass(-1, "Soturi", 4, 0, 0, "");
        Proficiency prof1 = new Proficiency("Taistelutahto", "", "");
        Proficiency prof2 = new Proficiency("Pakenemistahto", "", "");
        Proficiency prof3 = new Proficiency("Huhhuh", "", "");
        Proficiency prof4 = new Proficiency("RandomSkill", "Skill", "");
        rpg.addUncertainProf(prof1);
        this.profs.add(prof1);
        rpg.addUncertainProf(prof2);
        this.profs.add(prof2);
        rpg.addUncertainProf(prof3);
        this.profs.add(prof3);
        this.profs.add(prof4);
        
        this.randomizer.addUncertainClassProfs(rpg, classProfs);
        
        assertEquals(4, profs.size());
    }
    
    @Test
    public void addUncertainRacialProfsRightAmountIsGiven() {
        ArrayList<Proficiency> racialProfs = new ArrayList<>();
        ArrayList<Racial> racials = new ArrayList<>();
        Racial racial1 = new Racial(-1, "vahvuus", 0, false, 1, 0, 0, "");
        Racial racial2 = new Racial(-1, "voima", 0, false, 2, 0, 0, "");
        racial1.addUncertainProf(new Proficiency("Taistelutahto", "", ""));
        racial1.addUncertainProf(new Proficiency("Pakenemistahto", "", ""));
        racial2.addUncertainProf(new Proficiency("Huhhuh", "", ""));
        racial2.addUncertainProf(new Proficiency("Hahhah", "", ""));
        racial2.addUncertainProf(new Proficiency("Höhhöh", "", ""));
        racials.add(racial1);
        racials.add(racial2);
        
        this.randomizer.getUncertainRacialProfs(racials, racialProfs);
        
        assertEquals(3, racialProfs.size());
    }
    
    @Test
    public void racialIfAllUncertainIsAddedRandomSkillIsGiven() {
        ArrayList<Proficiency> racialProfs = new ArrayList<>();
        Racial racial1 = new Racial(-1, "vahvuus", 0, false, 1, 0, 0, "");
        Racial racial2 = new Racial(-1, "voima", 0, false, 3, 0, 0, "");
        racial1.addUncertainProf(new Proficiency("Taistelutahto", "", ""));
        racial1.addUncertainProf(new Proficiency("Pakenemistahto", "", ""));
        racial2.addUncertainProf(new Proficiency("Taistelutahto", "", ""));
        racial2.addUncertainProf(new Proficiency("Pakenemistahto", "", ""));
        racial2.addUncertainProf(new Proficiency("Miekkataito", "", ""));
        this.profs.add(new Proficiency("Extrataito", "Skill", ""));
        
        this.randomizer.addUncertainRacialProfs(racial2, racialProfs);
        this.randomizer.addUncertainRacialProfs(racial1, racialProfs);
        
        
        assertEquals(4, racialProfs.size());
    }
    
    @Test
    public void classExtraProfsGiveRightAmount() {
        ArrayList<Proficiency> classProfs = new ArrayList<>();
        RpgClass rpg = new RpgClass(-1, "Soturi", 0, 0, 2, "Skill");
        this.profs.add(new Proficiency(-1, "taito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "tuito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "maito", "Skill", ""));
        this.randomizer.addExtraClassProfs(rpg, classProfs);
        
        assertEquals(2, classProfs.size());
    }
    
    @Test
    public void classExtraProfsGiveRightType() {
        ArrayList<Proficiency> classProfs = new ArrayList<>();
        RpgClass rpg = new RpgClass(-1, "Soturi", 0, 0, 1, "Tool");
        this.profs.add(new Proficiency(-1, "taito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "tuito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "maito", "Tool", ""));
        this.randomizer.addExtraClassProfs(rpg, classProfs);
        
        assertEquals("Tool", classProfs.get(0).getType());
    }
    
    @Test
    public void racialExtraProfsGiveRightAmount() {
        ArrayList<Proficiency> racialProfs = new ArrayList<>();
        Racial racial = new Racial(-1, "Uskottavuus", 0, false, 0, 0, 1, "Skill");
        this.profs.add(new Proficiency(-1, "taito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "tuito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "maito", "Skill", ""));
        this.randomizer.addExtraRacialProfs(racial, racialProfs);
        
        assertEquals(1, racialProfs.size());
    }
    
    @Test
    public void racialExtraProfsGiveRightType() {
        ArrayList<Proficiency> racialProfs = new ArrayList<>();
        Racial racial = new Racial(-1, "Uskottavuus", 0, false, 0, 0, 1, "Instrument");
        this.profs.add(new Proficiency(-1, "taito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "tuito", "Skill", "Instrument"));
        this.profs.add(new Proficiency(-1, "maito", "Skill", ""));
        this.randomizer.addExtraRacialProfs(racial, racialProfs);
        
        assertEquals("Instrument", racialProfs.get(0).getSubtype());
    }
    
    @Test
    public void bgExtraSkillsGivesRightAmount() {
        ArrayList<Proficiency> bgProfs = new ArrayList<>();
        this.profs.add(new Proficiency(-1, "taito", "Skill", ""));
        this.profs.add(new Proficiency(-1, "tuito", "Skill", "Instrument"));
        this.profs.add(new Proficiency(-1, "maito", "Skill", ""));
        this.settings.setBgSkillsAmount(2);
        this.randomizer.addExtraBgSkills(bgProfs);
        
        assertEquals(2, bgProfs.size());
    }
    
    @Test
    public void bgExtraToolsGivesRightAmount() {
        ArrayList<Proficiency> bgProfs = new ArrayList<>();
        this.profs.add(new Proficiency(-1, "taito", "Tool", "Gaming Set"));
        this.profs.add(new Proficiency(-1, "tuito", "Tool", "Instrument"));
        this.profs.add(new Proficiency(-1, "maito", "Tool", "Artisan"));
        this.settings.setBgOtherAmount(1);
        this.settings.setBgToolChance(100.0);
        this.randomizer.addExtraBgOther(bgProfs);       
        
        assertEquals(1, bgProfs.size());
    }
}
