/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sampo
 */
public class Randomizer {

    private Random random;
    private List<Proficiency> certainProfs;
    private List<Proficiency> uncertainProfs;
    private int amountOfUncertain;
    private HashMap<String, Integer> randomProfs;
    private List<Proficiency> finalProfs;

    public Randomizer(long seed) {
        this.random = new Random(seed);
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
        this.amountOfUncertain = 0;
        this.randomProfs = new HashMap<>();
        this.finalProfs = new ArrayList<>();
    }

    public Randomizer() {
        this.random = new Random();
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
        this.amountOfUncertain = 0;
        this.randomProfs = new HashMap<>();
        this.finalProfs = new ArrayList<>();
    }

    public int[] randomizeStats(int statPool, int statVar, int statMin,
            int statMax, boolean racialBonus) {
        statPool -= statVar;
        statPool += this.random.nextInt(2 * statVar + 1);
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

        if (racialBonus) {
            int bonusStat1 = random.nextInt(6);
            int bonusStat2 = random.nextInt(5);

            if (bonusStat2 >= bonusStat1) {
                bonusStat2++;
            }

            newStats[bonusStat1] += 2;
            newStats[bonusStat2]++;
        }
        return newStats;
    }

    public Race getRandomRace(List<Race> races) {
        int index = this.random.nextInt(races.size());
        return races.get(index);
    }

    public RpgClass getRandomClass(List<RpgClass> classes) {
        int index = this.random.nextInt(classes.size());
        RpgClass randomClass = classes.get(index);
        for (Proficiency prof : randomClass.getCertainProfs()) {
            this.certainProfs.add(prof);
        }
        for (Proficiency prof : randomClass.getUncertainProfs()) {
            this.uncertainProfs.add(prof);
            this.amountOfUncertain++;
        }
        return randomClass;
    }

    public String getRandomSubclass(List<String> subclasses) {
        int index = this.random.nextInt(subclasses.size());
        return subclasses.get(index);
    }

    public Background getRandomBackground(List<Background> bgs) {
        int index = this.random.nextInt(bgs.size());
        Background randomBg = bgs.get(index);
        return randomBg;
    }

    public ArrayList<Racial> getRandomRacials(int amount, List<Racial> racials) {
        ArrayList<Racial> randomRacials = new ArrayList<>();

        if (amount >= racials.size()) {
            randomRacials.addAll(racials);
            for (Racial newRacial : randomRacials) {
                extractRacialProficiencies(newRacial);
            }
            return randomRacials;
        }

        while (randomRacials.size() < amount) {
            int index = this.random.nextInt(racials.size());
            Racial newRacial = racials.get(index);
            if (!randomRacials.contains(newRacial)) {
                randomRacials.add(newRacial);
                extractRacialProficiencies(newRacial);
            }
        }
        return randomRacials;
    }

    public void extractRacialProficiencies(Racial racial) {
        for (Proficiency prof : racial.getCertainProfs()) {
            if (!this.certainProfs.contains(prof)) {
                this.certainProfs.add(prof);
            } else {
                String type = prof.getType();
                String subtype = prof.getSubtype();
                if (!subtype.isEmpty()) {
                    if (!this.randomProfs.containsKey(subtype)) {
                        this.randomProfs.put(subtype, 1);
                    } else {
                        int number = this.randomProfs.get(subtype) + 1;
                        this.randomProfs.put(subtype, number);
                    }
                } else {
                    if (!this.randomProfs.containsKey(type)) {
                        this.randomProfs.put(type, 1);
                    } else {
                        int number = this.randomProfs.get(type) + 1;
                        this.randomProfs.put(type, number);
                    }
                }
            }
        }
        for (Proficiency prof : racial.getUncertainProfs()) {
            if (!this.uncertainProfs.contains(prof)) {
                this.uncertainProfs.add(prof);
            }
            this.amountOfUncertain++;
        }
        int extraProfs = racial.getExtraProfs();
        String extraProfType = racial.getExtraProfType();
        if (!(extraProfs == 0)) {
            if (!this.randomProfs.containsKey(extraProfType)) {
                this.randomProfs.put(extraProfType, 1);
            } else {
                int number = this.randomProfs.get(extraProfType) + extraProfs;
                this.randomProfs.put(extraProfType, number);
            }
        }
    }

    /**
     * Metodi sekoittaa kuusi kokonaislukua sisältävän taulukon
     *
     * @see hahmogeneraattori.domain.GeneratorService#swap(int[], int, int)
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
     * Metodi vaihtaa annetun taulukon kahden arvon paikkaa taulukossa keskenään
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
}
