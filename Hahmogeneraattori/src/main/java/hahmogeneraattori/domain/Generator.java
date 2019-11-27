/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

//import hahmogeneraattori.dao.SQLRaceDao;
//import hahmogeneraattori.dao.SQLClassDao;
//import hahmogeneraattori.dao.SQLBgDao;
import java.util.Random;
import java.lang.*;
import java.util.*;

/**
 *
 * @author sampo
 */
public class Generator {

    //private SQLRaceDao raceDao;
    //private SQLClassDao classDao;
    //private SQLBgDao backgroundDao;
    private Settings settings;
    private RPGCharacter character;

    public Generator(Settings settings) {
        this.settings = settings;
        this.character = null;
    }

    public RPGCharacter getCharacter() {
        return this.character;
    }

    public void generate() {
        this.character = new RPGCharacter();
        this.character.setStats(createRandomStats());
    }

    public int[] createRandomStats() {
        int statVar = this.settings.getStatVar();
        int statPool = this.settings.getStatPool() - statVar;
        Random random = new Random(System.currentTimeMillis());
        statPool += random.nextInt(2 * statVar + 1);
        int statMin = this.settings.getStatMin();
        int statMax = this.settings.getStatMax();
        int range = statMax - statMin;
        int[] stats = new int[6];
        for (int i = 0; i < 6; i++) {
            stats[i] = statMin;
        }
        statPool -= 6 * statMin;

        for (int i = 0; i < 6; i++) {
            int statBonus = random.nextInt(Math.min(statPool, range) + 1);
            stats[i] += statBonus;
            statPool -= statBonus;

            while (statPool > (5 - i) * range) {
                stats[i]++;
                statPool--;
            }
        }
        shuffle(stats);

        if (this.settings.getRacialBonus()) {
            int bonusStat1 = random.nextInt(6);
            int bonusStat2 = random.nextInt(5);

            if (bonusStat2 >= bonusStat1) {
                bonusStat2++;
            }

            stats[bonusStat1] += 2;
            stats[bonusStat2]++;
        }
        
        return stats;
    }

    public String generateStatList() {
        HashMap<String, Integer> stats = character.getStats();
        String statListing = "Stats:\n\nSTR: " + stats.get("STR") + "\nDEX: "
                + stats.get("DEX") + "\nCON: " + stats.get("CON") + "\nINT: "
                + stats.get("INT") + "\nWIS: " + stats.get("WIS") + "\nCHA: "
                + stats.get("CHA");
        return statListing;
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
