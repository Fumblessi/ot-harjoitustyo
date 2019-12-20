/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import hahmogeneraattori.dao.GeneratorDatabaseDao;
import java.sql.SQLException;
import java.util.*;

/**
 * Luokka sisältää generaattorin palvelut, eli tietokannan käsittelyn sekä
 * generoinnin muuntamisen käyttöliittymässä näytettäväksi
 *
 * @author sampo
 */
public class GeneratorService {

    private Settings settings;
    private Stats stats;
    private Race race;
    private RpgClass rpgclass;
    private String subclass;
    private Background bg;
    private int order;
    private int morality;
    private ArrayList<Racial> racials;
    private ArrayList<Proficiency> profs;
    private HashMap<Proficiency, String> langs;
    private List<Feat> feats;
    private Randomizer randomizer;
    private GeneratorDatabaseDao generatorDatabaseDao;

    /**
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao
     * @see hahmogeneraattori.domain.Settings
     * @see hahmogeneraattori.domain.Randomizer
     * @see hahmogeneraattori.domain.Stats
     * @see hahmogeneraattori.domain.Race
     * @see hahmogeneraattori.domain.RpgClass
     * @see hahmogeneraattori.domain.Background
     * @see hahmogeneraattori.domain.Racial
     * @see hahmogeneraattori.domain.Proficiency
     * @see hahmogeneraattori.domain.Feat
     *
     * @param settings generaattorin asetukset
     * @param gbDao generaattorin tietokanta
     */
    public GeneratorService(Settings settings, GeneratorDatabaseDao gbDao) {
        this.settings = settings;
        this.generatorDatabaseDao = gbDao;
        this.randomizer = null;
        this.stats = null;
        this.race = null;
        this.rpgclass = null;
        this.subclass = null;
        this.bg = null;
        this.order = -1;
        this.morality = -1;
        this.racials = new ArrayList<>();
        this.profs = new ArrayList<>();
        this.langs = new HashMap<>();
        this.feats = new ArrayList<>();
    }

    /**
     * Alustetaan seuraavan generoinnin arpoja
     *
     * @see hahmogeneraattori.domain.Randomizer
     */
    public void initializeRandomizer() {
        this.profs.clear();
        this.randomizer = new Randomizer(this.settings, listAllProfs());
    }

    /**
     * @return arvotut piirteet
     */
    public Stats getStats() {
        return this.stats;
    }

    /**
     * @return arvottu tausta
     */
    public Background getBackground() {
        return this.bg;
    }

    /**
     * Metodi toteuttaa hahmon generoinnin, eli Randomizer-olion avulla arpoo
     * hahmon rodun, luokan, alaluokan, rotuominaisuudet, taustan, taidot,
     * järjestelmällisyyden/moraalin, piirteet ja osaamat kielet
     *
     * @see hahmogeneraattori.domain.GeneratorService#initializeRandomizer()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomRace()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomClass()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomSubclass()
     * @see hahmogeneraattori.domain.GeneratorService#createCertainClassProfs()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomRacials()
     * @see hahmogeneraattori.domain.GeneratorService#createCertainRacialProfs()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomBackground()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomProfs()
     * @see hahmogeneraattori.domain.GeneratorService#createOrderAndMorality()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomStats()
     * @see hahmogeneraattori.domain.GeneratorService#createRandomLangs()
     */
    public void generate() {
        initializeRandomizer();
        createRandomRace();
        createRandomClass();
        createRandomSubclass();
        createCertainClassProfs();
        createRandomRacials();
        createCertainRacialProfs();
        createRandomBackground();
        createRandomProfs();
        createOrderAndMorality();
        createRandomStats();
        createRandomLangs();
    }

    /**
     * Metodi arpoo käyttäjän asetusten pohjalta hahmolle piirteet, ja tallentaa
     * ne luokassa olevaan Stats-muotoiseen olioon
     *
     * @see hahmogeneraattori.domain.Randomizer#randomizeStats(List)
     */
    public void createRandomStats() {
        this.stats = new Stats(this.randomizer.randomizeStats(this.racials));
    }

    /**
     * Metodi arpoo hahmolle rodun
     *
     * @see hahmogeneraattori.domain.Randomizer#getRandomRace(List)
     */
    public void createRandomRace() {
        this.race = this.randomizer.getRandomRace(listAllRaces());
    }

    /**
     * Metodi arpoo hahmolle luokan
     *
     * @see hahmogeneraattori.domain.Randomizer#getRandomClass(List)
     */
    public void createRandomClass() {
        this.rpgclass = this.randomizer.getRandomClass(listAllClasses());
    }

    /**
     * Metodi arpoo hahmolle alaluokan
     *
     * @see hahmogeneraattori.domain.Randomizer#getRandomSubclass(List)
     */
    public void createRandomSubclass() {
        this.subclass = this.randomizer.getRandomSubclass(this.rpgclass.getSubclasses());
    }

    /**
     * Metodi arpoo hahmolle taustan
     *
     * @see hahmogeneraattori.domain.Randomizer#getRandomBackground(List)
     */
    public void createRandomBackground() {
        this.bg = this.randomizer.getRandomBackground(listAllBackgrounds());
    }

    /**
     * Metodi arpoo hahmolle järjestelmällisyyden ja moraalin
     *
     * @see hahmogeneraattori.domain.Randomizer#getOrder()
     * @see hahmogeneraattori.domain.Randomizer#getMorality()
     */
    public void createOrderAndMorality() {
        this.order = this.randomizer.getOrder();
        this.morality = this.randomizer.getMorality();
    }

    /**
     * Metodi arpoo hahmolle rotuominaisuudet asetusten pohjalta
     *
     * @see hahmogeneraattori.domain.Settings#getRacialAmount()
     * @see hahmogeneraattori.domain.Randomizer#getRandomRacials(int, List,
     * RpgClass)
     */
    public void createRandomRacials() {
        int racialAmount = this.settings.getRacialAmount();
        this.racials = this.randomizer.getRandomRacials(racialAmount, listAllRacials(),
                this.rpgclass);
    }

    /**
     * Metodi ottaa taitoihin hahmoluokan varmasti antamat taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#getCertainClassProfs(RpgClass,
     * ArrayList)
     */
    public void createCertainClassProfs() {
        this.randomizer.getCertainClassProfs(this.rpgclass, this.profs);
    }

    /**
     * Metodi ottaa taitoihin rotuominaisuuksien varmasti antamat taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#getCertainRacialProfs(List,
     * ArrayList)
     */
    public void createCertainRacialProfs() {
        this.randomizer.getCertainRacialProfs(this.racials, this.profs);
    }

    /**
     * Metodi arpoo taitoihin luokan, rotuominaisuuksien ja taustan epävarmat ja
     * ylimääräiset taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#getUncertainClassProfs(RpgClass,
     * ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#getUncertainRacialProfs(List,
     * ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#getBgProfs(Background,
     * ArrayList)
     */
    public void createRandomProfs() {
        this.randomizer.getUncertainClassProfs(this.rpgclass, this.profs);
        this.randomizer.getUncertainRacialProfs(this.racials, this.profs);
        this.randomizer.getBgProfs(this.bg, this.profs);
    }

    /**
     * Metodi arpoo hahmon osaamat kielet
     *
     * @see hahmogeneraattori.domain.Randomizer#getRandomLangs()
     */
    public void createRandomLangs() {
        this.langs = this.randomizer.getRandomLangs();
    }

    /**
     * Hahmon piirteet palautetaan String-muotoisena
     *
     * @return hahmon piirteet
     */
    public String getStatsString() {
        return this.stats.toString();
    }

    /**
     * Hahmon rotu palautetaan String-muotoisena
     *
     * @return hahmon rotu
     */
    public String getRaceString() {
        return "\nRace: " + this.race.getName();
    }

    /**
     * Hahmon luokka ja alaluokka palautetaan String-muotoisena
     *
     * @return hahmon luokka
     */
    public String getClassString() {
        return "Class: " + this.subclass + " " + this.rpgclass.getName();
    }

    /**
     * Hahmon tausta palautetaan String-muotoisena
     *
     * @return hahmon tausta
     */
    public String getBgString() {
        return "Background: " + this.bg.getName();
    }

    /**
     * Hahmon järjestelmällisyys ja moraali palautetaan String-muotoisena
     *
     * @return hahmon järjestelmällisyys ja moraali
     */
    public String getOrderMoralityString() {
        return "Order/Morality: " + this.order + "/" + this.morality;
    }

    /**
     * Hahmon rotuominaisuudet palautetaan String-muotoisena
     *
     * @return hahmon rotuominaisuudet
     */
    public String getRacialString() {
        String racialString = "\nRacials: ";
        if (this.racials.isEmpty()) {
            racialString += "\n\tNone";
        } else {
            ArrayList<String> racialNames = new ArrayList<>();
            for (Racial racial : this.racials) {
                racialNames.add(racial.getName());
            }
            Collections.sort(racialNames);
            for (String racialName : racialNames) {
                racialString += "\n\t" + racialName;
            }
        }
        return racialString;
    }

    /**
     * Hahmon "pelastus"-taidot palautetaan String-muotoisena
     *
     * @return hahmon savet
     */
    public String getSaveString() {
        String saveString = "\nSaves: ";
        saveString = addProfsByType(saveString, "Save");
        return saveString;
    }

    /**
     * Hahmon ase-taidot palautetaan String-muotoisena
     *
     * @return hahmon ase-taidot
     */
    public String getWeaponString() {
        String weaponString = "\nWeapons: ";
        weaponString = addProfsByType(weaponString, "Weapon");
        return weaponString;
    }

    /**
     * Hahmon suojavarustustaidot palautetaan String-muotoisena
     *
     * @return hahmon suojavarustustaidot
     */
    public String getArmorString() {
        String armorString = "\nArmor: ";
        armorString = addProfsByType(armorString, "Armor");
        return armorString;
    }

    /**
     * Hahmon taidot palautetaan String-muotoisena
     *
     * @return hahmon taidot
     */
    public String getSkillString() {
        String skillString = "\nSkills: ";
        skillString = addProfsByType(skillString, "Skill");
        return skillString;
    }

    /**
     * Hahmon työkalutaidot palautetaan String-muotoisena
     *
     * @return hahmon työkalutaidot
     */
    public String getToolString() {
        String toolString = "\nTools: ";
        toolString = addProfsByType(toolString, "Tool");
        return toolString;
    }

    /**
     * Hahmon kielitaidot palautetaan String-muotoisena
     *
     * @return hahmon kielitaidot
     */
    public String getLanguageString() {
        String langString = "\nLanguages: ";
        ArrayList<String> langNames = new ArrayList<>();
        for (Proficiency prof : this.langs.keySet()) {
            langNames.add(prof.getName());
        }
        Collections.sort(langNames);
        for (String langName : langNames) {
            for (Proficiency prof : this.langs.keySet()) {
                if (prof.getName().equals(langName)) {
                    langString += "\n\t" + langName + " " + this.langs.get(prof);
                }
            }
        }
        return langString;
    }

    /**
     * Hahmon piirteet palautetaan String-muotoisena tyypin perusteella ja
     * järjestetään aakkosjärjestykseen
     *
     * @return hahmon tietyn tyyppiset piirteet
     */
    public String addProfsByType(String stringToAdd, String type) {
        if (noProfsOfThatType(type)) {
            stringToAdd += "\n\tNone";
        } else {
            ArrayList<String> profNames = new ArrayList<>();
            for (Proficiency prof : this.profs) {
                if (prof.getType().equals(type)) {
                    profNames.add(prof.getName());
                }
            }
            Collections.sort(profNames);
            for (String profName : profNames) {
                stringToAdd += "\n\t" + profName;
            }
        }
        return stringToAdd;
    }

    /**
     * Metodi tarkistaa, onko hahmolla tietyntyyppisiä taitoja
     *
     * @param type tarkistettava tyyppi
     *
     * @return true tai false
     */
    public boolean noProfsOfThatType(String type) {
        boolean noProfsOfThatType = true;
        for (Proficiency prof : this.profs) {
            if (prof.getType().equals(type)) {
                noProfsOfThatType = false;
                break;
            }
        }
        return noProfsOfThatType;
    }

    /**
     * Metodi lisää uuden rodun tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#create(Object)
     *
     * @param race lisättävä rotu
     *
     * @throws SQLException
     */
    public void addNewRaceToDb(Race race) throws SQLException {
        this.generatorDatabaseDao.create(race);
    }

    /**
     * Metodi päivittää rodun tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#update(Object)
     *
     * @param race päivitettävä rotu
     *
     * @throws SQLException
     */
    public void updateRaceToDb(Race race) throws SQLException {
        this.generatorDatabaseDao.update(race);
    }

    /**
     * Metodi poistaa rodun tietokannasta
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#delete(Object)
     *
     * @param race poistettava rotu
     *
     * @throws SQLException
     */
    public void deleteRaceFromDb(Race race) throws SQLException {
        this.generatorDatabaseDao.delete(race);
    }

    /**
     * Metodi lisää uuden proficiencyn tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#create(Object)
     *
     * @param prof lisättävä proficiency
     *
     * @throws SQLException
     */
    public void addNewProfToDb(Proficiency prof) throws SQLException {
        this.generatorDatabaseDao.create(prof);
    }

    /**
     * Metodi päivittää proficiencyn tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#update(Object)
     *
     * @param prof päivitettävä proficiency
     *
     * @throws SQLException
     */
    public void updateProfToDb(Proficiency prof) throws SQLException {
        this.generatorDatabaseDao.update(prof);
    }

    /**
     * Metodi poistaa tietokannassa olevan proficiency
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#delete(Object)
     *
     * @param prof poistettava proficiency
     *
     * @throws SQLException
     */
    public void deleteProfFromDb(Proficiency prof) throws SQLException {
        this.generatorDatabaseDao.delete(prof);
    }

    /**
     * Metodi lisää uuden racialin tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#create(Object)
     *
     * @param racial lisättävä racial
     *
     * @throws SQLException
     */
    public void addNewRacialToDb(Racial racial) throws SQLException {
        this.generatorDatabaseDao.create(racial);
    }

    /**
     * Metodi päivittää racialin tietokantaan
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#update(Object)
     *
     * @param racial päivitettävä racial
     *
     * @throws SQLException
     */
    public void updateRacialToDb(Racial racial) throws SQLException {
        this.generatorDatabaseDao.update(racial);
    }

    /**
     * Metodi poistaa tietokannassa olevan racialin
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#delete(Object)
     *
     * @param racial poistettava racial
     *
     * @throws SQLException
     */
    public void deleteRacialFromDb(Racial racial) throws SQLException {
        this.generatorDatabaseDao.delete(racial);
    }

    /**
     * Metodi lisää uuden hahmoluokan tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#create(Object)
     *
     * @param rpgclass lisättävä hahmoluokka
     *
     * @throws SQLException
     */
    public void addNewClassToDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.create(rpgclass);
    }

    /**
     * Metodi päivittää hahmoluokan tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#update(Object)
     *
     * @param rpgclass päivitettävä hahmoluokka
     *
     * @throws SQLException
     */
    public void updateClassToDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.update(rpgclass);
    }

    /**
     * Metodi poistaa hahmoluokan tietokannasta
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#delete(Object)
     *
     * @param rpgclass poistettava hahmoluokka
     *
     * @throws SQLException
     */
    public void deleteClassFromDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.delete(rpgclass);
    }

    /**
     * Metodi lisää uuden taustan tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#create(Object)
     *
     * @param bg lisättävä tausta
     *
     * @throws SQLException
     */
    public void addNewBgToDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.create(bg);
    }

    /**
     * Metodi päivittää taustan tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#update(Object)
     *
     * @param bg päivitettävä tausta
     *
     * @throws SQLException
     */
    public void updateBgToDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.update(bg);
    }

    /**
     * Metodi poistaa taustan tietokannasta
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#delete(Object)
     *
     * @param bg poistettava tausta
     *
     * @throws SQLException
     */
    public void deleteBgFromDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.delete(bg);
    }

    /**
     * Metodi lisää uuden erityistaidon tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#create(Object)
     *
     * @param feat lisättävä erityistaito
     *
     * @throws SQLException
     */
    public void addNewFeatToDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.create(feat);
    }

    /**
     * Metodi päivittää erityistaidon tietokantaan
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#update(Object)
     *
     * @param feat päivitettävä erityistaito
     *
     * @throws SQLException
     */
    public void updateFeatToDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.update(feat);
    }

    /**
     * Metodi poistaa erityistaidon tietokannasta
     *
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao#delete(Object)
     *
     * @param feat poistettava erityistaito
     *
     * @throws SQLException
     */
    public void deleteFeatFromDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.delete(feat);
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Race' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista roduista
     */
    public List<Race> listAllRaces() {
        return this.generatorDatabaseDao.list(Race.class
        );
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Proficiency' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista proficiencyistä
     */
    public List<Proficiency> listAllProfs() {
        return this.generatorDatabaseDao.list(Proficiency.class);
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Racial' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista racialeista
     */
    public List<Racial> listAllRacials() {
        return this.generatorDatabaseDao.list(Racial.class
        );
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Class' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista hahmoluokista
     */
    public List<RpgClass> listAllClasses() {
        return this.generatorDatabaseDao.list(RpgClass.class
        );
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Background' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista taustoista
     */
    public List<Background> listAllBackgrounds() {
        return this.generatorDatabaseDao.list(Background.class
        );
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Feat' sisällön
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#list(Class)
     *
     * @return lista erityistaidoista
     */
    public List<Feat> listAllFeats() {
        return this.generatorDatabaseDao.list(Feat.class
        );
    }

    /**
     * Metodi tyhjentää tietokannan ja alustaa sen uudelleen
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initialize()
     *
     * @throws SQLException
     */
    public void initializeDatabase() throws SQLException {
        this.generatorDatabaseDao.initialize();
    }
}
