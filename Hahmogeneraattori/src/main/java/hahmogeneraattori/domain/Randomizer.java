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
    private Settings settings;
    private List<Proficiency> allProfs;
    private int langCount;

    public Randomizer(Settings settings, List<Proficiency> allProfs, long seed) {
        this.settings = settings;
        this.allProfs = allProfs;
        this.langCount = this.settings.getLanguageAmount();
        this.random = new Random(seed);
    }

    public Randomizer(Settings settings, List<Proficiency> allProfs) {
        this.settings = settings;
        this.allProfs = allProfs;
        this.langCount = this.settings.getLanguageAmount();
        this.random = new Random();
    }

    public int getOrder() {
        return this.random.nextInt(10) + 1;
    }

    public int getMorality() {
        return this.random.nextInt(10) + 1;
    }

    public int[] randomizeStats(List<Racial> racials) {
        int statPool = this.settings.getStatPool();
        int statVar = this.settings.getStatVar();
        int statMin = this.settings.getStatMin();
        int statMax = this.settings.getStatMax();
        boolean racialBonus = this.settings.getRacialBonus();
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
        if (racialBonus) {
            addRacialBonus(newStats);
        }
        if (racials.isEmpty()) {
            makeRacialModifications(newStats, racials);
        }
        shuffle(newStats);
        return newStats;
    }

    public void addRacialBonus(int[] stats) {
        int bonusStat1 = this.random.nextInt(6);
        int bonusStat2 = this.random.nextInt(5);

        if (bonusStat2 >= bonusStat1) {
            bonusStat2++;
        }

        stats[bonusStat1] += 2;
        stats[bonusStat2]++;
    }

    public void makeRacialModifications(int[] stats, List<Racial> racials) {
        for (Racial racial : racials) {
            if (racial.getStats() < 0) {
                int minus = racial.getStats();
                while (minus < 0) {
                    int index = random.nextInt(6);
                    stats[index]--;
                    minus++;
                }
            } else if (racial.getStats() > 0) {
                int plus = racial.getStats();
                while (plus > 0) {
                    int index = random.nextInt(6);
                    stats[index]++;
                    plus--;
                }
            }
        }
    }

    public HashMap<Proficiency, String> getRandomLangs(RpgClass rpgclass,
            List<Racial> racials, Background bg) {
        HashMap<Proficiency, String> langs = new HashMap<>();
        if (this.settings.getMotherLanguage()) {
            getMotherLanguage(langs);
            this.langCount--;
        }
        while (this.langCount > 0) {
            getRandomLanguage(langs);
        }
        return langs;
    }

    public void getMotherLanguage(HashMap<Proficiency, String> langs) {
        int type = this.settings.getMotherLanguageType();
        int rnd = this.random.nextInt(10001);
        int tier3chance = (int) (this.settings.
                getMotherLanguageThirdTierChance() * 100);
        switch (type) {
            case 1:
                Proficiency argan = findProfByName("Argan");
                if (rnd <= tier3chance) {
                    langs.put(argan, "III");
                } else {
                    langs.put(argan, "II");
                }
                break;
            case 2:
                Proficiency commonLang = getRandomLangByRarity(langs, "Common");
                if (rnd <= tier3chance) {
                    langs.put(commonLang, "III");
                } else {
                    langs.put(commonLang, "II");
                }
                break;
            default:
                Proficiency lang = getRandomMotherLang(langs);
                if (rnd <= tier3chance) {
                    langs.put(lang, "III");
                } else {
                    langs.put(lang, "II");
                }
                break;
        }
    }

    public void getRandomLanguage(HashMap<Proficiency, String> langs) {
        boolean gotAllCommons = gotAllLangs(langs, "Common");
        boolean gotAllRares = gotAllLangs(langs, "Rare");
        boolean gotAllLegendaries = gotAllLangs(langs, "Legendary");
        int commonChance = (int) (this.settings.getCommonChance() * 100);
        int rareChance = (int) (this.settings.getRareChance() * 100) + commonChance;
        Proficiency newLang = null;
        String langLevel = "";
        int rnd = this.random.nextInt(10001);

        if (rnd <= commonChance && !gotAllCommons) {
            newLang = getRandomLangByRarity(langs, "Common");
            langLevel = getRandomCommonLangLevel();
        } else if (rnd <= rareChance && !gotAllRares) {
            newLang = getRandomLangByRarity(langs, "Rare");
            langLevel = getRandomRareLangLevel();
        } else if (!gotAllLegendaries) {
            newLang = getRandomLangByRarity(langs, "Legendary");
            langLevel = getRandomLegendaryLangLevel();
        } else {
            this.langCount--;
        }
        if (!langs.containsKey(newLang) && (newLang != null)) {
            langs.put(newLang, langLevel);
            this.langCount--;
        }
    }

    public String getRandomCommonLangLevel() {
        int commonFtc = (int) (this.settings.getCommonFirstTierChance() * 100);
        int commonStc = (int) (this.settings.getCommonSecondTierChance() * 100)
                + commonFtc;
        int rnd = this.random.nextInt(10001);

        if (rnd <= commonFtc) {
            return "I";
        } else if (rnd <= commonStc) {
            return "II";
        } else {
            return "III";
        }
    }

    public String getRandomRareLangLevel() {
        int rareFtc = (int) (this.settings.getRareFirstTierChance() * 100);
        int rareStc = (int) (this.settings.getRareSecondTierChance() * 100)
                + rareFtc;
        int rnd = this.random.nextInt(10001);

        if (rnd <= rareFtc) {
            return "I";
        } else if (rnd <= rareStc) {
            return "II";
        } else {
            return "III";
        }
    }

    public String getRandomLegendaryLangLevel() {
        int legendaryFtc = (int) (this.settings.getLegendaryFirstTierChance() * 100);
        int legendaryStc = (int) (this.settings.getLegendarySecondTierChance() * 100)
                + legendaryFtc;
        int rnd = this.random.nextInt(10001);

        if (rnd <= legendaryFtc) {
            return "I";
        } else if (rnd <= legendaryStc) {
            return "II";
        } else {
            return "III";
        }
    }

    public Proficiency getRandomLangByRarity(HashMap<Proficiency, String> langs,
            String rarity) {
        ArrayList<Proficiency> extraProfs = createExtraProfPool(rarity);
        while (true) {
            int index = this.random.nextInt(extraProfs.size());
            Proficiency randomLang = extraProfs.get(index);
            if (!langs.containsKey(randomLang)) {
                return randomLang;
            }
        }
    }

    public Proficiency getRandomMotherLang(HashMap<Proficiency, String> langs) {
        int commonChance = (int) (this.settings.getCommonChance() * 100);
        int total = (int) (this.settings.getRareChance() * 100) + commonChance;
        int rnd = this.random.nextInt(total + 1);
        if (rnd <= commonChance) {
            return getRandomLangByRarity(langs, "Common");
        } else {
            return getRandomLangByRarity(langs, "Rare");
        }
    }

    public boolean gotAllLangs(HashMap<Proficiency, String> langs, String rarity) {
        boolean gotAll = true;
        ArrayList<Proficiency> allLangs = createExtraProfPool(rarity);
        for (Proficiency lang : allLangs) {
            if (!langs.containsKey(lang)) {
                gotAll = false;
                break;
            }
        }
        return gotAll;
    }

    public Race getRandomRace(List<Race> allRaces) {
        Race randomRace = null;
        if (!allRaces.isEmpty()) {
            int index = this.random.nextInt(allRaces.size());
            randomRace = allRaces.get(index);
        } else {
            randomRace = new Race("-");
        }
        return randomRace;
    }

    public RpgClass getRandomClass(List<RpgClass> allClasses) {
        RpgClass randomClass = null;
        if (!allClasses.isEmpty()) {
            int index = this.random.nextInt(allClasses.size());
            randomClass = allClasses.get(index);
        } else {
            randomClass = new RpgClass("-", 0, 0, 0, "");
        }
        return randomClass;
    }

    public String getRandomSubclass(List<String> subclasses) {
        String randomSubclass = "-";
        if (!subclasses.isEmpty()) {
            int index = this.random.nextInt(subclasses.size());
            randomSubclass = subclasses.get(index);
        }
        return randomSubclass;
    }

    public Background getRandomBackground(List<Background> allBgs) {
        Background randomBg = null;
        if (!allBgs.isEmpty()) {
            int index = this.random.nextInt(allBgs.size());
            randomBg = allBgs.get(index);
        } else {
            randomBg = new Background("-", "");
        }
        return randomBg;
    }

    public ArrayList<Racial> getRandomRacials(int amount, List<Racial> allRacials,
            RpgClass rpgclass) {
        ArrayList<Racial> randomRacials = new ArrayList<>();

        while (randomRacials.size() < amount) {
            if (gotAllLegalRacials(allRacials, randomRacials, rpgclass)) {
                break;
            }
            randomRacials.add(getRandomRacial(randomRacials, allRacials, rpgclass));
        }
        return randomRacials;
    }

    public boolean gotAllLegalRacials(List<Racial> allRacials, ArrayList<Racial> randomRacials, RpgClass rpgclass) {
        for (Racial racial : allRacials) {
            if (!randomRacials.contains(racial) && checkRacialLegality(racial,
                    rpgclass)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkRacialLegality(Racial racial, RpgClass rpgclass) {
        for (Proficiency prof : racial.getCertainProfs()) {
            if (rpgclass.getCertainProfs().contains(prof)) {
                return false;
            }
        }
        return true;
    }

    public Racial getRandomRacial(List<Racial> randomRacials,
            List<Racial> allRacials, RpgClass rpgclass) {
        while (true) {
            int index = this.random.nextInt(allRacials.size());
            Racial newRacial = allRacials.get(index);
            if (!randomRacials.contains(newRacial) && checkRacialLegality(newRacial,
                    rpgclass)) {
                return newRacial;
            }
        }
    }

    public void getCertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        profs.addAll(rpgclass.getCertainProfs());
        this.langCount += rpgclass.getRandomLangs();
    }

    public void getUncertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        if (rpgclass.getRandomProfs() != 0 && !rpgclass.getUncertainProfs().isEmpty()) {
            addUncertainClassProfs(rpgclass, profs);
        }
        if (rpgclass.getExtraProfs() != 0) {
            addExtraClassProfs(rpgclass, profs);
        }
    }

    public void getCertainRacialProfs(List<Racial> racials, ArrayList<Proficiency> profs) {
        for (Racial racial : racials) {
            profs.addAll(racial.getCertainProfs());
            this.langCount += racial.getRandomLangs();
        }
    }

    public void getUncertainRacialProfs(List<Racial> racials, ArrayList<Proficiency> profs) {
        for (Racial racial : racials) {
            if (racial.getRandomProfs() != 0 && !racial.getUncertainProfs().isEmpty()) {
                addUncertainRacialProfs(racial, profs);
            }
            if (racial.getExtraProfs() != 0) {
                addExtraRacialProfs(racial, profs);
            }
        }
    }

    public void getBgProfs(Background bg, ArrayList<Proficiency> profs) {
        addExtraBgSkills(profs);
        addExtraBgOther(profs);
    }

    public void addUncertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        int uncertainAdded = 0;
        int uncertainToAdd = rpgclass.getRandomProfs();
        while (uncertainAdded < uncertainToAdd) {
            if (allUncertainAdded(rpgclass.getUncertainProfs(), profs)) {
                Proficiency newProf = getRandomSkillTool();
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    uncertainAdded++;
                }
            } else {
                int index = this.random.nextInt(rpgclass.getUncertainProfs().size());
                Proficiency newProf = rpgclass.getUncertainProfs().get(index);
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    uncertainAdded++;
                }
            }
        }
    }

    public boolean allUncertainAdded(List<Proficiency> uncertainProfs,
            ArrayList<Proficiency> profs) {
        for (Proficiency prof : uncertainProfs) {
            if (!profs.contains(prof)) {
                return false;
            }
        }
        return true;
    }

    public Proficiency getRandomSkillTool() {
        ArrayList<Proficiency> extraProfPool = createExtraProfPool("Skill/Tool");
        int index = this.random.nextInt(extraProfPool.size());
        return extraProfPool.get(index);
    }

    public void addUncertainRacialProfs(Racial racial, ArrayList<Proficiency> profs) {
        int uncertainAdded = 0;
        int uncertainToAdd = racial.getRandomProfs();
        while (uncertainAdded < uncertainToAdd) {
            if (allUncertainAdded(racial.getUncertainProfs(), profs)) {
                Proficiency newProf = getRandomSkillTool();
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    uncertainAdded++;
                }
            } else {
                int index = this.random.nextInt(racial.getUncertainProfs().size());
                Proficiency newProf = racial.getUncertainProfs().get(index);
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    uncertainAdded++;
                }
            }
        }
    }

    public void addExtraClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        String type = rpgclass.getExtraProfType();
        ArrayList<Proficiency> extraProfPool = createExtraProfPool(type);

        int extraAdded = 0;
        int extraToAdd = rpgclass.getExtraProfs();

        while (extraAdded < extraToAdd) {
            int index = this.random.nextInt(extraProfPool.size());
            Proficiency newProf = extraProfPool.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                extraAdded++;
            }
        }
    }

    public void addExtraRacialProfs(Racial racial, ArrayList<Proficiency> profs) {
        String type = racial.getExtraProfType();
        ArrayList<Proficiency> extraProfPool = createExtraProfPool(type);

        int extraAdded = 0;
        int extraToAdd = racial.getExtraProfs();

        while (extraAdded < extraToAdd) {
            int index = this.random.nextInt(extraProfPool.size());
            Proficiency newProf = extraProfPool.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                extraAdded++;
            }
        }
    }

    public void addExtraBgSkills(ArrayList<Proficiency> profs) {
        ArrayList<Proficiency> skills = createExtraProfPool("Skill");

        int extraAdded = 0;
        int extraToAdd = this.settings.getBgSkillsAmount();

        while (extraAdded < extraToAdd) {
            int index = this.random.nextInt(skills.size());
            Proficiency newProf = skills.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                extraAdded++;
            }
        }
    }

    public void addExtraBgOther(ArrayList<Proficiency> profs) {
        int bgToolChance = (int) (this.settings.getBgToolChance() * 100);
        int otherAdded = 0;
        int otherToAdd = this.settings.getBgOtherAmount();

        while (otherAdded < otherToAdd) {
            int rnd = this.random.nextInt(10001);

            if (rnd <= bgToolChance) {
                bgAddExtraTool(profs);
                otherAdded++;
            } else {
                this.langCount++;
                otherAdded++;
            }
        }
    }

    public void bgAddExtraTool(ArrayList<Proficiency> profs) {
        int bgArtisanChance = (int) (this.settings.getBgArtisanChance() * 100);
        int bgGamingChance = (int) (this.settings.getBgGamingSetChance() * 100)
                + bgArtisanChance;
        int rnd = this.random.nextInt(10001);

        if (rnd <= bgArtisanChance) {
            addExtraBgArtisan(profs);
        } else if (rnd <= bgGamingChance) {
            addExtraBgGaming(profs);
        } else {
            addExtraBgInstrument(profs);
        }
    }

    public void addExtraBgArtisan(ArrayList<Proficiency> profs) {
        ArrayList<Proficiency> artisanProfs = createExtraProfPool("Artisan");

        while (true) {
            int index = this.random.nextInt(artisanProfs.size());
            Proficiency newProf = artisanProfs.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                break;
            }
        }
    }

    public void addExtraBgGaming(ArrayList<Proficiency> profs) {
        ArrayList<Proficiency> artisanProfs = createExtraProfPool("Gaming Set");

        while (true) {
            int index = this.random.nextInt(artisanProfs.size());
            Proficiency newProf = artisanProfs.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                break;
            }
        }
    }

    public void addExtraBgInstrument(ArrayList<Proficiency> profs) {
        ArrayList<Proficiency> artisanProfs = createExtraProfPool("Instrument");

        while (true) {
            int index = this.random.nextInt(artisanProfs.size());
            Proficiency newProf = artisanProfs.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                break;
            }
        }
    }

    public boolean addCertainRacialProfs(Racial racial, ArrayList<Proficiency> profs) {
        for (Proficiency prof : racial.getCertainProfs()) {
            if (profs.contains(prof)) {
                return false;
            }
        }
        profs.addAll(racial.getCertainProfs());
        return true;
    }

    public ArrayList<Proficiency> createExtraProfPool(String type) {
        ArrayList<Proficiency> extraProfPool = new ArrayList<>();
        switch (type) {
            case "Skill":
                createOneTypePool("Skill", extraProfPool);
                break;
            case "Tool":
                createOneTypePool("Tool", extraProfPool);
                break;
            case "Artisan":
                createOneSubtypePool("Artisan", extraProfPool);
                break;
            case "Gaming Set":
                createOneSubtypePool("Gaming Set", extraProfPool);
                break;
            case "Instrument":
                createOneSubtypePool("Instrument", extraProfPool);
                break;
            case "Skill/Tool":
                createTwoTypePool("Skill", "Tool", extraProfPool);
                break;
            case "Artisan/Instrument":
                createTwoSubtypePool("Artisan", "Instrument", extraProfPool);
                break;
            case "Artisan/Gaming Set":
                createTwoSubtypePool("Artisan", "Gaming Set", extraProfPool);
                break;
            case "Gaming Set/Instrument":
                createTwoSubtypePool("Gaming Set", "Instrument", extraProfPool);
                break;
            case "Common":
                createOneSubtypePool("Common", extraProfPool);
                break;
            case "Rare":
                createOneSubtypePool("Rare", extraProfPool);
                break;
            case "Legendary":
                createOneSubtypePool("Legendary", extraProfPool);
                break;
            default:
                break;
        }
        return extraProfPool;
    }

    public void createOneTypePool(String type, ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getType().equals(type)) {
                extraProfs.add(prof);
            }
        }
    }

    public void createOneSubtypePool(String subtype, ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getSubtype().equals(subtype)) {
                extraProfs.add(prof);
            }
        }
    }

    public void createTwoTypePool(String type1, String type2,
            ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getType().equals(type1)
                    || prof.getType().equals(type2)) {
                extraProfs.add(prof);
            }
        }
    }

    public void createTwoSubtypePool(String subtype1, String subtype2,
            ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getSubtype().equals(subtype1)
                    || prof.getSubtype().equals(subtype2)) {
                extraProfs.add(prof);
            }
        }
    }

    public Proficiency findProfByName(String name) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getName().equals(name)) {
                return prof;
            }
        }
        return null;
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
