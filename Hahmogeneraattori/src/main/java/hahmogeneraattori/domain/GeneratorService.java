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
 * Luokka sisältää generaattorin hahmon generointi-toiminnallisuuden, ja tämän
 * lisäksi yhteyden tietokantaluokkaan GeneratorDatabaseDao, josta haetaan eri
 * ominaisuudet, minkä joukosta arvonta tehdään
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
    private List<Racial> racials;
    private List<Proficiency> profs;
    private HashMap<Proficiency, String> langs;
    private List<Feat> feats;
    private Randomizer randomizer;
    private GeneratorDatabaseDao generatorDatabaseDao;

    /**
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao
     * @see hahmogeneraattori.domain.Settings
     * @see hahmogeneraattori.domain.Stats
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

    public void initializeRandomizer() {
        this.randomizer = new Randomizer(this.settings, listAllProfs());
    }

    public Stats getStats() {
        return this.stats;
    }

    public Background getBackground() {
        return this.bg;
    }

    /**
     * Metodi toteuttaa hahmon generoinnin, aloittaen hahmon piirteiden
     * (stattien) arpomisesta.
     *
     * @see hahmogeneraattori.domain.GeneratorService#createRandomStats()
     */
    public void generate() {
        initializeRandomizer();
        createRandomRace();
        createRandomClass();
        createRandomSubclass();
        createRandomBackground();
        createOrderAndMorality();
        createRandomRacials();
        createRandomStats();
        createRandomProfs();
        createRandomLangs();
    }

    /**
     * Metodi arpoo käyttäjän asetusten pohjalta hahmolle piirteet, ja tallentaa
     * ne luokassa olevaan Stats-muotoiseen olioon
     *
     * @see hahmogeneraattori.domain.GeneratorService#shuffle(int[])
     * @see hahmogeneraattori.domain.Stats
     */
    public void createRandomStats() {
        this.stats = new Stats(this.randomizer.randomizeStats(this.racials));
    }

    public void createRandomRace() {
        this.race = this.randomizer.getRandomRace(listAllRaces());
    }

    public void createRandomClass() {
        this.rpgclass = this.randomizer.getRandomClass(listAllClasses());
    }

    public void createRandomSubclass() {
        this.subclass = this.randomizer.getRandomSubclass(this.rpgclass.getSubclasses());
    }

    public void createRandomBackground() {
        this.bg = this.randomizer.getRandomBackground(listAllBackgrounds());
    }

    public void createOrderAndMorality() {
        this.order = this.randomizer.getOrder();
        this.morality = this.randomizer.getMorality();
    }

    public void createRandomRacials() {
        int racialAmount = this.settings.getRacialAmount();
        this.racials = this.randomizer.getRandomRacials(racialAmount, listAllRacials());
    }

    public void createRandomProfs() {
        this.profs = this.randomizer.getRandomProfs(this.rpgclass, this.racials,
                this.bg, listAllRacials());
    }

    public void createRandomLangs() {
        this.langs = this.randomizer.getRandomLangs(this.rpgclass, this.racials,
                this.bg);
    }

    /**
     * Hahmon piirteet palautetaan String-muotoisena
     *
     * @return hahmon piirteet
     */
    public String getStatsString() {
        return this.stats.toString();
    }

    public String getRaceString() {
        return "\nRace: " + this.race.getName();
    }

    public String getClassString() {
        return "Class: " + this.subclass + " " + this.rpgclass.getName();
    }

    public String getBgString() {
        return "Background: " + this.bg.getName();
    }

    public String getOrderMoralityString() {
        return "Order/Morality: " + this.order + "/" + this.morality;
    }

    public String getRacialString() {
        String racialString = "\nRacials: ";
        if (this.racials.isEmpty()) {
            racialString += "\n\tNone";
        } else {
            for (Racial racial : this.racials) {
                racialString += "\n\t" + racial.getName();
            }
        }
        return racialString;
    }

    public String getSaveString() {
        String saveString = "\nSaves: ";
        saveString = addProfsByType(saveString, "Save");
        return saveString;
    }

    public String getWeaponString() {
        String weaponString = "\nWeapons: ";
        weaponString = addProfsByType(weaponString, "Weapon");
        return weaponString;
    }

    public String getArmorString() {
        String armorString = "\nArmor: ";
        armorString = addProfsByType(armorString, "Armor");
        return armorString;
    }

    public String getSkillString() {
        String skillString = "\nSkills: ";
        skillString = addProfsByType(skillString, "Skill");
        return skillString;
    }

    public String getToolString() {
        String toolString = "\nTools: ";
        toolString = addProfsByType(toolString, "Tool");
        return toolString;
    }

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

    public void addNewRaceToDb(Race race) throws SQLException {
        this.generatorDatabaseDao.create(race);
    }

    public void updateRaceToDb(Race race) throws SQLException {
        this.generatorDatabaseDao.update(race);
    }

    public void deleteRaceFromDb(Race race) throws SQLException {
        this.generatorDatabaseDao.delete(race);
    }

    /**
     * Lisätään uusi proficiency tietokantaan
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
     * Päivitetään tietokannassa oleva proficiency
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
     * Poistetaan tietokannassa oleva proficiency
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
     * Lisätään uusi racial tietokantaan
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
     * Päivitetään tietokannassa oleva racial
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
     * Poistetaan tietokannassa oleva racial
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

    public void addNewClassToDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.create(rpgclass);
    }

    public void updateClassToDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.update(rpgclass);
    }

    public void deleteClassFromDb(RpgClass rpgclass) throws SQLException {
        this.generatorDatabaseDao.delete(rpgclass);
    }

    public void addNewBgToDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.create(bg);
    }

    public void updateBgToDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.update(bg);
    }

    public void deleteBgFromDb(Background bg) throws SQLException {
        this.generatorDatabaseDao.delete(bg);
    }

    public void addNewFeatToDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.create(feat);
    }

    public void updateFeatToDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.update(feat);
    }

    public void deleteFeatFromDb(Feat feat) throws SQLException {
        this.generatorDatabaseDao.delete(feat);
    }

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

    public List<RpgClass> listAllClasses() {
        return this.generatorDatabaseDao.list(RpgClass.class
        );
    }

    public List<Background> listAllBackgrounds() {
        return this.generatorDatabaseDao.list(Background.class
        );
    }

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
