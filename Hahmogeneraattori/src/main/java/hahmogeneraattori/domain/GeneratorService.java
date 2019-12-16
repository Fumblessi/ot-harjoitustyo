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
 * lisäksi yhteyden tietokantaluokkaan GeneratorDatabaseDao, josta haetaan
 * eri ominaisuudet, minkä joukosta arvonta tehdään
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
    private List<Racial> racials;
    private List<Proficiency> profs;
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
        this.racials = new ArrayList<>();
        this.profs = new ArrayList<>();
        this.feats = new ArrayList<>();
    }

    public Stats getStats() {
        return this.stats;
    }

    /**
     * Metodi toteuttaa hahmon generoinnin, aloittaen hahmon piirteiden
     * (stattien) arpomisesta.
     * 
     * @see hahmogeneraattori.domain.GeneratorService#createRandomStats()
     */
    public void generate() {
        this.randomizer = new Randomizer();
        createRandomRace();
        createRandomClass();
        createRandomSubclass();
        createRandomBackground();
        createRandomRacials();
        createRandomStats();
    }

    /**
     * Metodi arpoo käyttäjän asetusten pohjalta hahmolle piirteet, ja
     * tallentaa ne luokassa olevaan Stats-muotoiseen olioon
     * 
     * @see hahmogeneraattori.domain.GeneratorService#shuffle(int[])
     * @see hahmogeneraattori.domain.Stats
     */
    public void createRandomStats() {
        int statPool = this.settings.getStatPool();
        int statVar = this.settings.getStatVar();
        int statMin = this.settings.getStatMin();
        int statMax = this.settings.getStatMax();
        boolean racialBonus = this.settings.getRacialBonus();
        this.stats = new Stats(this.randomizer.randomizeStats(statPool, statVar, 
                statMin, statMax, racialBonus));
    }
    
    public void createRandomRace() {
        List<Race> races = listAllRaces();
        this.race = this.randomizer.getRandomRace(races);
    }
    
    public void createRandomClass() {
        List<RpgClass> classes = listAllClasses();
        this.rpgclass = this.randomizer.getRandomClass(classes);
    }
    
    public void createRandomSubclass() {
        List<String> subclasses = this.rpgclass.getSubclasses();
        this.subclass = this.randomizer.getRandomSubclass(subclasses);
    }
    
    public void createRandomBackground() {
        List<Background> bgs = listAllBackgrounds();
        this.bg = this.randomizer.getRandomBackground(bgs);
    }
    
    public void createRandomRacials() {
        List<Racial> racialList = listAllRacials();
        int racialAmount = this.settings.getRacialAmount();
        this.racials = this.randomizer.getRandomRacials(racialAmount, racialList);
    }

    /**
     * Hahmon piirteet palautetaan String-muotoisena
     * 
     * @return hahmon piirteet
     */
    public String generateStatList() {
        return this.stats.toString();
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
        return this.generatorDatabaseDao.list(Race.class);
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
        return this.generatorDatabaseDao.list(Racial.class);
    }
    
    public List<RpgClass> listAllClasses() {
        return this.generatorDatabaseDao.list(RpgClass.class);
    }
    
    public List<Background> listAllBackgrounds() {
        return this.generatorDatabaseDao.list(Background.class);
    }
    
    public List<Feat> listAllFeats() {
        return this.generatorDatabaseDao.list(Feat.class);
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
