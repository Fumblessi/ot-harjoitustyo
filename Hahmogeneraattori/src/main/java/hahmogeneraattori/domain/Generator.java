/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import hahmogeneraattori.dao.GeneratorDatabaseDao;
import java.util.Random;
import java.lang.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Luokka sisältää generaattorin hahmon generointi-toiminnallisuuden, ja tämän
 * lisäksi yhteyden tietokantaluokkaan GeneratorDatabaseDao, josta haetaan
 * eri ominaisuudet, minkä joukosta arvonta tehdään
 * 
 * @author sampo
 */
public class Generator {

    private Settings settings;
    private Stats stats;
    private GeneratorDatabaseDao generatorDatabaseDao;

    /**
     * @see hahmogeneraattori.dao.GeneratorDatabaseDao
     * @see hahmogeneraattori.domain.Settings
     * @see hahmogeneraattori.domain.Stats
     * 
     * @param settings generaattorin asetukset
     * @param gbDao generaattorin tietokanta
     */
    public Generator(Settings settings, GeneratorDatabaseDao gbDao) {
        this.settings = settings;
        this.generatorDatabaseDao = gbDao;
        this.stats = new Stats();
    }

    public Stats getStats() {
        return this.stats;
    }

    /**
     * Metodi toteuttaa hahmon generoinnin, aloittaen hahmon piirteiden
     * (stattien) arpomisesta.
     * 
     * @see hahmogeneraattori.domain.Generator#createRandomStats()
     */
    public void generate() {
        createRandomStats();
    }

    /**
     * Metodi arpoo käyttäjän asetusten pohjalta hahmolle piirteet, ja
     * tallentaa ne luokassa olevaan Stats-muotoiseen olioon
     * 
     * @see hahmogeneraattori.domain.Generator#shuffle(int[])
     * @see hahmogeneraattori.domain.Stats
     */
    public void createRandomStats() {
        int statVar = this.settings.getStatVar();
        int statPool = this.settings.getStatPool() - statVar;
        Random random = new Random(System.currentTimeMillis());
        statPool += random.nextInt(2 * statVar + 1);
        int statMin = this.settings.getStatMin();
        int statMax = this.settings.getStatMax();
        int range = statMax - statMin;
        int[] newStats = new int[6];
        for (int i = 0; i < 6; i++) {
            newStats[i] = statMin;
        }
        statPool -= 6 * statMin;

        for (int i = 0; i < 6; i++) {
            int statBonus = random.nextInt(Math.min(statPool, range) + 1);
            newStats[i] += statBonus;
            statPool -= statBonus;

            while (statPool > (5 - i) * range) {
                newStats[i]++;
                statPool--;
            }
        }
        shuffle(newStats);

        if (this.settings.getRacialBonus()) {
            int bonusStat1 = random.nextInt(6);
            int bonusStat2 = random.nextInt(5);

            if (bonusStat2 >= bonusStat1) {
                bonusStat2++;
            }

            newStats[bonusStat1] += 2;
            newStats[bonusStat2]++;
        }
        this.stats.setStats(newStats);
    }

    /**
     * Hahmon piirteet palautetaan String-muotoisena
     * 
     * @return hahmon piirteet
     */
    public String generateStatList() {
        return this.stats.toString();
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

    /**
     * Metodi sekoittaa kuusi kokonaislukua sisältävän taulukon
     * 
     * @see hahmogeneraattori.domain.Generator#swap(int[], int, int)
     * 
     * @param array sekoitettava taulukko
     */
    public static void shuffle(int[] array) {
        Random random = new Random();
        for (int i = 6; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    /**
     * Metodi vaihtaa annetun taulukon kahden arvon paikkaa
     * taulukossa keskenään
     * 
     * @param array syötetty taulukko
     * @param i ensimmäisen vaihdettavan arvon indeksi
     * @param j toisen vaihdettavan arvon indeksi
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
