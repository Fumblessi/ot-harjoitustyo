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
 *
 * @author sampo
 */
public class Generator {

    private Settings settings;
    private Stats stats;
    private GeneratorDatabaseDao generatorDatabaseDao;

    public Generator(Settings settings, GeneratorDatabaseDao gbDao) {
        this.settings = settings;
        this.generatorDatabaseDao = gbDao;
        this.stats = new Stats();
    }

    public Stats getStats() {
        return this.stats;
    }

    public void generate() {
        createRandomStats();
    }

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

    public String generateStatList() {
        return this.stats.toString();
    }
    
    public void addNewProfToDb(Proficiency prof) throws SQLException {
        this.generatorDatabaseDao.create(prof);
    }
    
    public void updateProfToDb(Proficiency newProf) throws SQLException {
        this.generatorDatabaseDao.update(newProf);
    }
    
    public void deleteProfFromDb(Proficiency prof) throws SQLException {
        this.generatorDatabaseDao.delete(prof);
    }
    
    public List<Proficiency> listAllProfs() {
        return this.generatorDatabaseDao.list(Proficiency.class);
    }

    public static void shuffle(int[] array) {
        Random random = new Random();
        for (int i = 6; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void getNewSettings(Settings settings) {
        this.settings = settings;
    }
}
