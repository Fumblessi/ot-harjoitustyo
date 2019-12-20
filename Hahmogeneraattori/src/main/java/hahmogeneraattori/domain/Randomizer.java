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
 * Luokka vastaa generaattorin arvontatoiminnallisuudesta
 *
 * @author sampo
 */
public class Randomizer {

    private Random random;
    private Settings settings;
    private List<Proficiency> allProfs;
    private int langCount;

    /**
     * Konstruktorissa arpojalle annetaan voimassa olevat asetukset, lista
     * kaikista mahdollisista taidoista, ja halutessaan arvonnan seedi
     *
     * @param settings asetukset
     * @param allProfs lista taidoista
     * @param seed arvonnan seedi
     */
    public Randomizer(Settings settings, List<Proficiency> allProfs, long seed) {
        this.settings = settings;
        this.allProfs = allProfs;
        this.langCount = this.settings.getLanguageAmount();
        this.random = new Random(seed);
    }

    /**
     * Jos haluaa käyttää javan oletus seediä arvonnan pohjalla, voi sen jättää
     * syöttämättä parametrina konstruktorille
     *
     * @param settings asetukset
     * @param allProfs lista taidoista
     */
    public Randomizer(Settings settings, List<Proficiency> allProfs) {
        this.settings = settings;
        this.allProfs = allProfs;
        this.langCount = this.settings.getLanguageAmount();
        this.random = new Random();
    }

    /**
     * @param amount uusi arvottavien kielien määrä
     */
    public void setLangCount(int amount) {
        this.langCount = amount;
    }

    /**
     * @return arvottavien kielien määrä
     */
    public int getLangCount() {
        return this.langCount;
    }

    /**
     * @return arvottu hahmon järjestelmällisyys (1-10)
     */
    public int getOrder() {
        return this.random.nextInt(10) + 1;
    }

    /**
     * @return arvottu hahmon moraali (1-10)
     */
    public int getMorality() {
        return this.random.nextInt(10) + 1;
    }

    /**
     * Arvotaan hahmolle piirteet, ja lisätään niihin rodun ja
     * rotuominaisuuksien vaikutukset
     *
     * @see hahmogeneraattori.domain.Settings#getStatPool()
     * @see hahmogeneraattori.domain.Settings#getStatVar()
     * @see hahmogeneraattori.domain.Settings#getStatMin()
     * @see hahmogeneraattori.domain.Settings#getStatMax()
     * @see hahmogeneraattori.domain.Randomizer#addRacialBonus(int[])
     * @see hahmogeneraattori.domain.Randomizer#makeRacialModifications(int[],
     * List)
     * @see hahmogeneraattori.domain.Randomizer#shuffle(int[])
     *
     * @param racials rotuominaisuudet
     *
     * @return hahmon piirteet
     */
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
        if (!racials.isEmpty()) {
            makeRacialModifications(newStats, racials);
        }
        shuffle(newStats);
        return newStats;
    }

    /**
     * Lisätään arvottuihin piirteisiin satunnaisesti rodun antamat bonukset
     *
     * @param stats hahmon piirteet
     */
    public void addRacialBonus(int[] stats) {
        int bonusStat1 = this.random.nextInt(6);
        int bonusStat2 = this.random.nextInt(5);

        if (bonusStat2 >= bonusStat1) {
            bonusStat2++;
        }

        stats[bonusStat1] += 2;
        stats[bonusStat2]++;
    }

    /**
     * Tarkistetaan, vaikuttavatko hahmolle arvotut rotuominaisuudet
     * piirteisiin, ja jos, niin tehdään muutokset
     *
     * @param stats hahmon piirteet
     * @param racials hahmon rotuominaisuudet
     */
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

    /**
     * Arvotaan hahmolle satunnaiset kielet ja kielten osaamistasot
     *
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.Settings#getMotherLanguage()
     * @see hahmogeneraattori.domain.Randomizer#getMotherLanguage(HashMap)
     * @see hahmogeneraattori.domain.Randomizer#getRandomLanguage(HashMap)
     *
     * @return hahmon kielitaidot hajautustauluna
     */
    public HashMap<Proficiency, String> getRandomLangs() {
        HashMap<Proficiency, String> langs = new HashMap<>();
        ArrayList<Proficiency> langPool = createExtraProfPool("Language");
        if (langPool.isEmpty()) {
            langs.put(new Proficiency("-", "Language", "-"), "-");
            return langs;
        }
        if (this.settings.getMotherLanguage()) {
            getMotherLanguage(langs);
            this.langCount--;
        }
        while (this.langCount > 0) {
            getRandomLanguage(langs);
        }
        return langs;
    }

    /**
     * Arvotaan hahmolle äidinkieli ja osaamistaso
     *
     * @see hahmogeneraattori.domain.Settings#getMotherLanguageType()
     * @see hahmogeneraattori.domain.Settings#getMotherLanguageThirdTierChance()
     * @see hahmogeneraattori.domain.Randomizer#findProfByName(String)
     * @see hahmogeneraattori.domain.Randomizer#getRandomLangByRarity(HashMap,
     * String)
     * @see hahmogeneraattori.domain.Randomizer#getRandomMotherLang(HashMap)
     *
     * @param langs
     */
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

    /**
     * Arvotaan satunnainen kieli lisättäväksi kielitaito-hajatutustauluun
     *
     * @see hahmogeneraattori.domain.Randomizer#gotAllLangs(HashMap, String)
     * @see hahmogeneraattori.domain.Settings#getCommonChance()
     * @see hahmogeneraattori.domain.Settings#getRareChance()
     * @see hahmogeneraattori.domain.Randomizer#getRandomLangByRarity(HashMap,
     * String)
     * @see hahmogeneraattori.domain.Randomizer#getRandomCommonLangLevel()
     * @see hahmogeneraattori.domain.Randomizer#getRandomRareLangLevel()
     * @see hahmogeneraattori.domain.Randomizer#getRandomLegendaryLangLevel()
     *
     * @param langs
     */
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

    /**
     * Arvotaan asetusten pohjalta yleiselle kielelle osaamistaso
     *
     * @see hahmogeneraattori.domain.Settings#getCommonFirstTierChance()
     * @see hahmogeneraattori.domain.Settings#getCommonSecondTierChance()
     *
     * @return kielen osaamistaso
     */
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

    /**
     * Arvotaan asetusten pohjalta harvinaiselle kielelle osaamistaso
     *
     * @see hahmogeneraattori.domain.Settings#getRareFirstTierChance()
     * @see hahmogeneraattori.domain.Settings#getRareSecondTierChance()
     *
     * @return kielen osaamistaso
     */
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

    /**
     * Arvotaan asetusten pohjalta legendaariselle kielelle osaamistaso
     *
     * @see hahmogeneraattori.domain.Settings#getLegendaryFirstTierChance()
     * @see hahmogeneraattori.domain.Settings#getLegendarySecondTierChance()
     *
     * @return kielen osaamistaso
     */
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

    /**
     * Arvotaan satunnainen kieli, jolla on tietty harvinaisuus
     *
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     *
     * @param langs hahmon kielitaidot
     * @param rarity satunnaisen kielen harvinaisuus
     *
     * @return satunnainen kieli
     */
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

    /**
     * Arvotaan hahmolle satunnainen äidinkieli (legendaariset kielet
     * poissuljettu)
     *
     * @see hahmogeneraattori.domain.Settings#getCommonChance()
     * @see hahmogeneraattori.domain.Settings#getRareChance()
     * @see hahmogeneraattori.domain.Randomizer#getRandomLangByRarity(HashMap,
     * String)
     *
     * @param langs hahmon kielitaidot
     *
     * @return satunnainen äidinkieli
     */
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

    /**
     * Metodi tarkistaa, onko hahmolla jo kaikki tietyn harvinaisuustason kielet
     *
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     *
     * @param langs hahmon kielitaidot
     * @param rarity tarkistettava harvinaisuustaso
     *
     * @return true tai false
     */
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

    /**
     * Metodi arpoo satunnaisen rodun (tai palauttaa uuden tyhjän, jos
     * tietokannassa ei ole rotuja)
     *
     * @param allRaces lista kaikista roduista
     *
     * @return satunnainen rotu
     */
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

    /**
     * Metodi arpoo satunnaisen hahmoluokan (tai uuden tyhjän luokan, jos
     * tietokannassa ei ole yhtään hahmoluokkaa)
     *
     * @param allClasses lista kaikista hahmoluokista
     *
     * @return satunnainen hahmoluokka
     */
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

    /**
     * Metodi arpoo hahmolle alaluokan kaikista luokan alaluokista (tai tyhjän
     * alaluokan, jos luokalla ei ole alaluokkia)
     *
     * @param subclasses lista alaluokista
     *
     * @return satunnainen alaluokka
     */
    public String getRandomSubclass(List<String> subclasses) {
        String randomSubclass = "-";
        if (!subclasses.isEmpty()) {
            int index = this.random.nextInt(subclasses.size());
            randomSubclass = subclasses.get(index);
        }
        return randomSubclass;
    }

    /**
     * Metodi arpoo hahmolle taustan tietokannassa olevien taustojen joukosta
     * (tai antaa uuden tyhjän taustan, jos tietokannassa ei ole taustoja)
     *
     * @param allBgs lista kaikista taustoista
     *
     * @return satunnainen tausta
     */
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

    /**
     * Metodi arpoo hahmolle satunnaiset rotuominaisuudet, ja tarkistaa, ettei
     * arvo sellaisia, jotka antaisivat varmasti taitoja, jotka hahmolla jo on
     *
     * @see hahmogeneraattori.domain.Randomizer#gotAllLegalRacials(List,
     * ArrayList, RpgClass)
     * @see hahmogeneraattori.domain.Randomizer#getRandomRacial(List, List,
     * RpgClass)
     *
     * @param amount arvottavien rotuominaisuuksien määrä
     * @param allRacials lista kaikista rotuominaisuuksista
     * @param rpgclass hahmon luokka
     *
     * @return lista satunnaisista rotuominaisuuksista
     */
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

    /**
     * Metodi tarkistaa, onko hahmo jo saanut kaikki 'lailliset'
     * rotuominaisuudet
     *
     * @see hahmogeneraattori.domain.Randomizer#checkRacialLegality(List,
     * Racial, RpgClass)
     *
     * @param allRacials lista kaikista rotuominaisuuksista
     * @param randomRacials lista hahmon rotuominaisuuksista
     * @param rpgclass hahmon luokka
     *
     * @return true tai false
     */
    public boolean gotAllLegalRacials(List<Racial> allRacials, ArrayList<Racial> randomRacials, RpgClass rpgclass) {
        for (Racial racial : allRacials) {
            if (!randomRacials.contains(racial) && checkRacialLegality(randomRacials,
                    racial, rpgclass)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metodi tarkistaa rotuominaisuuden laillisuuden, eli antaako se varmasti
     * jotain taitoja, jotka hahmo saa jo muualta
     *
     * @param randomRacials hahmon rotuominaisuudet
     * @param racial tarkistettava rotuominaisuus
     * @param rpgclass hahmon luokka
     *
     * @return true tai false
     */
    public boolean checkRacialLegality(List<Racial> randomRacials,
            Racial racial, RpgClass rpgclass) {
        for (Proficiency prof : racial.getCertainProfs()) {
            if (rpgclass.getCertainProfs().contains(prof)) {
                return false;
            }
            for (Racial rndRacial : randomRacials) {
                if (rndRacial.getCertainProfs().contains(prof)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodi arpoo 'laillisen' satunnaisen rotuominaisuuden
     *
     * @see hahmogeneraattori.domain.Randomizer#checkRacialLegality(List,
     * Racial, RpgClass)
     *
     * @param randomRacials hahmon rotuominaisuudet
     * @param allRacials kaikki rotuominaisuudet
     * @param rpgclass hahmon luokka
     *
     * @return satunnainen rotuominaisuus
     */
    public Racial getRandomRacial(List<Racial> randomRacials,
            List<Racial> allRacials, RpgClass rpgclass) {
        while (true) {
            int index = this.random.nextInt(allRacials.size());
            Racial newRacial = allRacials.get(index);
            if (!randomRacials.contains(newRacial) && checkRacialLegality(randomRacials,
                    newRacial, rpgclass)) {
                return newRacial;
            }
        }
    }

    /**
     * Metodi lisää hahmolle sen luokan varmasti antamat taidot
     *
     * @see hahmogeneraattori.domain.RpgClass#getCertainProfs()
     * @see hahmogeneraattori.domain.RpgClass#getRandomLangs()
     *
     * @param rpgclass hahmon luokka
     * @param profs hahmon taidot
     */
    public void getCertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        profs.addAll(rpgclass.getCertainProfs());
        this.langCount += rpgclass.getRandomLangs();
    }

    /**
     * Metodi lisää hahmolle satunnaisesti taitoja sen hahmoluokaltaan
     * epävarmasti saatavien taitojen joukosta
     *
     * @see hahmogeneraattori.domain.RpgClass#getRandomProfs()
     * @see hahmogeneraattori.domain.RpgClass#getUncertainProfs()
     * @see hahmogeneraattori.domain.Randomizer#addUncertainClassProfs(RpgClass,
     * ArrayList)
     * @see hahmogeneraattori.domain.RpgClass#getExtraProfs()
     * @see hahmogeneraattori.domain.Randomizer#addExtraClassProfs(RpgClass,
     * ArrayList)
     *
     * @param rpgclass hahmon luokka
     * @param profs hahmon taidot
     */
    public void getUncertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        if (rpgclass.getRandomProfs() != 0 && !rpgclass.getUncertainProfs().isEmpty()) {
            addUncertainClassProfs(rpgclass, profs);
        }
        if (rpgclass.getExtraProfs() != 0) {
            addExtraClassProfs(rpgclass, profs);
        }
    }

    /**
     * Metodi lisää hahmolle sen rotuominaisuuksien varmasti sille antamat
     * taidot
     *
     * @see hahmogeneraattori.domain.Racial#getCertainProfs()
     * @see hahmogeneraattori.domain.Racial#getRandomLangs()
     *
     * @param racials
     * @param profs
     */
    public void getCertainRacialProfs(List<Racial> racials, ArrayList<Proficiency> profs) {
        for (Racial racial : racials) {
            profs.addAll(racial.getCertainProfs());
            this.langCount += racial.getRandomLangs();
        }
    }

    /**
     * Metodi lisää hahmolle satunnaisesti taitoja sen rotuominaisuuksiltaan
     * epävarmasti saatavien taitojen joukosta
     *
     * @see hahmogeneraattori.domain.Racial#getRandomProfs()
     * @see hahmogeneraattori.domain.Racial#getUncertainProfs()
     * @see hahmogeneraattori.domain.Randomizer#addUncertainRacialProfs(Racial,
     * ArrayList)
     * @see hahmogeneraattori.domain.Racial#getExtraProfs()
     * @see hahmogeneraattori.domain.Randomizer#addExtraRacialProfs(Racial,
     * ArrayList)
     *
     * @param racials hahmon rotuominaisuudet
     * @param profs hahmon taidot
     */
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

    /**
     * Metodi lisää satunnaiset hahmon taustasta saamat taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#addExtraBgSkills(ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#addExtraBgOther(ArrayList)
     *
     * @param bg hahmon tausta
     * @param profs hahmon taidot
     */
    public void getBgProfs(Background bg, ArrayList<Proficiency> profs) {
        addExtraBgSkills(profs);
        addExtraBgOther(profs);
    }

    /**
     * Metodi lisää satunnaisesti hahmolle sen luokaltaan saamat epävarmat
     * taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#getRandomSkillTool()
     *
     * @param rpgclass hahmon luokka
     * @param profs hahmon taidot
     */
    public void addUncertainClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        int uncertainAdded = 0;
        int uncertainToAdd = rpgclass.getRandomProfs();
        while (uncertainAdded < uncertainToAdd) {
            if (allAdded(rpgclass.getUncertainProfs(), profs)) {
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

    /**
     * Metodi tarkistaa, ovatko kaikki tietyt taidot lisätty jo hahmolle
     *
     * @param profPool lista lisättävistä taidoista
     * @param profs hahmon taidot
     *
     * @return true tai false
     */
    public boolean allAdded(List<Proficiency> profPool,
            ArrayList<Proficiency> profs) {
        for (Proficiency prof : profPool) {
            if (!profs.contains(prof)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metodi arpoo satunnaisen taidon tai työkalutaidon
     *
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     *
     * @return satunnainen taito
     */
    public Proficiency getRandomSkillTool() {
        ArrayList<Proficiency> extraProfPool = createExtraProfPool("Skill/Tool");
        int index = this.random.nextInt(extraProfPool.size());
        return extraProfPool.get(index);
    }

    /**
     * Metodi lisää satunnaisesti hahmolle sen rotuominaisuudeltaan saamat
     * epävarmat taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#getRandomSkillTool()
     *
     * @param racial rotuominaisuus
     * @param profs hahmon taidot
     */
    public void addUncertainRacialProfs(Racial racial, ArrayList<Proficiency> profs) {
        int uncertainAdded = 0;
        int uncertainToAdd = racial.getRandomProfs();
        while (uncertainAdded < uncertainToAdd) {
            if (allAdded(racial.getUncertainProfs(), profs)) {
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

    /**
     * Metodi lisää satunnaisesti hahmolle sen luokaltaan saamat ylimääräiset
     * taidot
     *
     * @see hahmogeneraattori.domain.RpgClass#getExtraProfType()
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.RpgClass#getExtraProfs()
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     *
     * @param rpgclass hahmon luokka
     * @param profs hahmon taidot
     */
    public void addExtraClassProfs(RpgClass rpgclass, ArrayList<Proficiency> profs) {
        String type = rpgclass.getExtraProfType();
        ArrayList<Proficiency> extraProfPool = createExtraProfPool(type);

        int extraAdded = 0;
        int extraToAdd = rpgclass.getExtraProfs();

        while (extraAdded < extraToAdd) {
            if (allAdded(extraProfPool, profs)) {
                extraAdded++;
            } else {
                int index = this.random.nextInt(extraProfPool.size());
                Proficiency newProf = extraProfPool.get(index);
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    extraAdded++;
                }
            }
        }
    }

    /**
     * Metodi lisää satunnaisesti hahmolle sen rotuominaisuudeltaan saamat
     * ylimääräiset taidot
     *
     * @see hahmogeneraattori.domain.Racial#getExtraProfType()
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.Racial#getExtraProfs()
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     *
     * @param racial rotuominaisuus
     * @param profs hahmon taidot
     */
    public void addExtraRacialProfs(Racial racial, ArrayList<Proficiency> profs) {
        String type = racial.getExtraProfType();
        ArrayList<Proficiency> extraProfPool = createExtraProfPool(type);

        int extraAdded = 0;
        int extraToAdd = racial.getExtraProfs();

        while (extraAdded < extraToAdd) {
            if (allAdded(extraProfPool, profs)) {
                extraAdded++;
            } else {
                int index = this.random.nextInt(extraProfPool.size());
                Proficiency newProf = extraProfPool.get(index);
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    extraAdded++;
                }
            }
        }
    }

    /**
     * Metodi lisää hahmolle sen taustastaan saamat taidot
     *
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.Settings#getBgSkillsAmount()
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     *
     * @param profs hahmon taidot
     */
    public void addExtraBgSkills(ArrayList<Proficiency> profs) {
        ArrayList<Proficiency> skills = createExtraProfPool("Skill");

        int extraAdded = 0;
        int extraToAdd = this.settings.getBgSkillsAmount();

        while (extraAdded < extraToAdd) {
            if (allAdded(skills, profs)) {
                extraAdded++;
            } else {
                int index = this.random.nextInt(skills.size());
                Proficiency newProf = skills.get(index);
                if (!profs.contains(newProf)) {
                    profs.add(newProf);
                    extraAdded++;
                }
            }
        }
    }

    /**
     * Metodi lisää hahmolle sen taustastaan saamat muut taidot
     *
     * @see hahmogeneraattori.domain.Settings#getBgToolChance()
     * @see hahmogeneraattori.domain.Settings#getBgOtherAmount()
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     *
     * @param profs hahmon taidot
     */
    public void addExtraBgOther(ArrayList<Proficiency> profs) {
        int bgToolChance = (int) (this.settings.getBgToolChance() * 100);
        int otherAdded = 0;
        int otherToAdd = this.settings.getBgOtherAmount();
        ArrayList<Proficiency> tools = createExtraProfPool("Tool");

        while (otherAdded < otherToAdd) {
            int rnd = this.random.nextInt(10001);

            if (rnd <= bgToolChance && !allAdded(tools, profs)) {
                bgAddExtraTool(profs);
                otherAdded++;
            } else {
                this.langCount++;
                otherAdded++;
            }
        }
    }

    /**
     * Metodi lisää hahmolle sen taustastaan saaman työkalutaidon
     *
     * @see hahmogeneraattori.domain.Settings#getBgGamingSetChance()
     * @see hahmogeneraattori.domain.Settings#getBgInstrumentChance()
     * @see hahmogeneraattori.domain.Randomizer#createExtraProfPool(String)
     * @see hahmogeneraattori.domain.Randomizer#allAdded(List, ArrayList)
     *
     * @param profs hahmon taidot
     */
    public void bgAddExtraTool(ArrayList<Proficiency> profs) {
        int bgGamingChance = (int) (this.settings.getBgGamingSetChance() * 100);
        int bgInstrumentChance = (int) (this.settings.getBgInstrumentChance() * 100)
                + bgGamingChance;
        int rnd = this.random.nextInt(10001);
        ArrayList<Proficiency> gamingSets = createExtraProfPool("Gaming Set");
        ArrayList<Proficiency> instruments = createExtraProfPool("Instrument");
        ArrayList<Proficiency> artisans = createExtraProfPool("Artisan");

        if (rnd <= bgGamingChance && !allAdded(gamingSets, profs)) {
            addExtraBgSubTool(profs, gamingSets);
        } else if (rnd <= bgInstrumentChance && !allAdded(instruments, profs)) {
            addExtraBgSubTool(profs, instruments);
        } else if (!allAdded(artisans, profs)) {
            addExtraBgSubTool(profs, artisans);
        }
    }

    /**
     * Metodi lisää hahmolle tietyn alatyypin työkalutaidon
     *
     * @param profs hahmon taidot
     * @param subPool lista alatyypin taidoista
     */
    public void addExtraBgSubTool(ArrayList<Proficiency> profs,
            ArrayList<Proficiency> subPool) {
        while (true) {
            int index = this.random.nextInt(subPool.size());
            Proficiency newProf = subPool.get(index);
            if (!profs.contains(newProf)) {
                profs.add(newProf);
                break;
            }
        }
    }

    /**
     * Metodi luo osalistan kaikista taidoista, joka sisältää vain halutun
     * tyyppisia/alatyyppisiä taitoja
     *
     * @see hahmogeneraattori.domain.Randomizer#createOneTypePool(String,
     * ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#createOneSubtypePool(String,
     * ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#createTwoTypePool(String,
     * String, ArrayList)
     * @see hahmogeneraattori.domain.Randomizer#createTwoSubtypePool(String,
     * String, ArrayList)
     *
     * @param type taitojen tyyppi
     *
     * @return lista tietyn tyyppisistä taidoista
     */
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
            case "Language":
                createOneTypePool("Language", extraProfPool);
            default:
                break;
        }
        return extraProfPool;
    }

    /**
     * Metodi luo tietyn yhden tyypin sisältävistä taidoista listan
     *
     * @param type tyyppi
     * @param extraProfs lista, johon lisätään pyydetyn tyyppiset taidot
     */
    public void createOneTypePool(String type, ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getType().equals(type)) {
                extraProfs.add(prof);
            }
        }
    }

    /**
     * Metodi luo tietyn yhden alatyypin sisältävistä taidoista listan
     *
     * @param subtype alatyyppi
     * @param extraProfs lista, johon lisätään pyydetyn tyyppiset taidot
     */
    public void createOneSubtypePool(String subtype, ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getSubtype().equals(subtype)) {
                extraProfs.add(prof);
            }
        }
    }

    /**
     * Metodi luo tietyn kahden tyypin sisältävistä taidoista listan
     *
     * @param type1 tyyppi
     * @param type2 toinen tyyppi
     * @param extraProfs lista, johon lisätään pyydetyn tyyppiset taidot
     */
    public void createTwoTypePool(String type1, String type2,
            ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getType().equals(type1)
                    || prof.getType().equals(type2)) {
                extraProfs.add(prof);
            }
        }
    }

    /**
     * Metodi luo tietyn kahden alatyypin sisältävistä taidoista listan
     *
     * @param subtype1 alatyyppi
     * @param subtype2 toinen alatyyppi
     * @param extraProfs lista, johon lisätään pyydetyn tyyppiset taidot
     */
    public void createTwoSubtypePool(String subtype1, String subtype2,
            ArrayList<Proficiency> extraProfs) {
        for (Proficiency prof : this.allProfs) {
            if (prof.getSubtype().equals(subtype1)
                    || prof.getSubtype().equals(subtype2)) {
                extraProfs.add(prof);
            }
        }
    }

    /**
     * Metodi etsii tietyn nimisen taidon
     *
     * @param name taidon nimi
     *
     * @return etsitty taito, jos löytyi
     */
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
     * @see hahmogeneraattori.domain.Randomizer#swap(int[], int, int)
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
