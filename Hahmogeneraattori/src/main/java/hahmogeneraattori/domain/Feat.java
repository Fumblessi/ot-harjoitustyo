/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Luokka kuvaa tietokantataulusta 'Feat' muodostettavat oliot, jotka kuvaavat
 * hahmon omaamia erityistaitoja, ja joita generoidulle hahmolle saatetaan
 * arpoa.
 *
 * @author sampo
 */
public class Feat {

    private int id;
    private String name;
    private String stats;
    private int randomProfs;
    private int randomLangs;
    private int extraProfs;
    private String extraProfType;
    private List<Proficiency> certainProfs;
    private List<Proficiency> uncertainProfs;

    /**
     * Featilla on indeksi ja nimi, joiden avulla ne voidaan uniikisti
     * tunnistaa. Tämän lisäksi feat voi vaikuttaa hahmon piirteisiin, tai antaa
     * hahmolle uusia taitoja tai kieliä.
     *
     * @param id featin indeksi
     * @param name featin nimi
     * @param stats featin antamat piirteet
     * @param randomProfs featin antamat epävarmat taidot
     * @param randomLangs featin antamat lisäkielet
     * @param extraProfs featin antamat ylimääräiset taidot
     * @param extraProfType ylimääräisten taitojen tyyppi
     */
    public Feat(int id, String name, String stats, int randomProfs, int randomLangs,
            int extraProfs, String extraProfType) {
        this.id = id;
        this.name = name;
        this.stats = stats;
        this.randomProfs = randomProfs;
        this.randomLangs = randomLangs;
        this.extraProfs = extraProfs;
        this.extraProfType = extraProfType;
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
    }

    /**
     * Feat-olion voi luoda myös ilman indeksiä (esimerkiksi uutta tietokantaan
     * lisättävää feattia luodessa), jolloin asetetaan indeksiksi väliaikaisesti
     * -1
     *
     * @param name featin nimi
     * @param stats featin antamat piirteet
     * @param randomProfs featin antamat epävarmat taidot
     * @param randomLangs featin antamat lisäkielet
     * @param extraProfs featin antamat ylimääräiset taidot
     * @param extraProfType ylimääräisten taitojen tyyppi
     */
    public Feat(String name, String stats, int randomProfs, int randomLangs, int extraProfs, String extraProfType) {
        this(-1, name, stats, randomProfs, randomLangs, extraProfs, extraProfType);
    }

    /**
     * @return featin nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return featin indeksi
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return featin antamat epävarmat taidot
     */
    public int getRandomProfs() {
        return this.randomProfs;
    }

    /**
     * @return featin antamat lisäkielet
     */
    public int getRandomLangs() {
        return this.randomLangs;
    }

    /**
     * @return featin antamat ylimääräiset taidot
     */
    public int getExtraProfs() {
        return this.extraProfs;
    }

    /**
     * @return featin antamien ylimääräisten taitojen tyyppi
     */
    public String getExtraProfType() {
        return this.extraProfType;
    }

    /**
     * @return lista featin antamista varmoista taidoista
     */
    public List<Proficiency> getCertainProfs() {
        return this.certainProfs;
    }

    /**
     * @return lista featin antamista epävarmoista taidoista
     */
    public List<Proficiency> getUncertainProfs() {
        return this.uncertainProfs;
    }

    /**
     * @return merkkijonona piirteet, joihin feat voi vaikuttaa
     */
    public String getStats() {
        return this.stats;
    }

    /**
     * Metodi asettaa featille indeksin, mutta vain, jos sillä ei vielä
     * sellaista ole
     *
     * @param id uusi indeksi
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }

    /**
     * @param name featin uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param amount featin uusi epävarmojen taitojen määrä
     */
    public void setRandomProfs(int amount) {
        this.randomProfs = amount;
    }

    /**
     * @param amount featin uusi lisäkielien määrä
     */
    public void setRandomLangs(int amount) {
        this.randomLangs = amount;
    }

    /**
     * @param amount featin uusi ylimääräisten taitojen määrä
     */
    public void setExtraProfs(int amount) {
        this.extraProfs = amount;
    }

    /**
     * @param type featin uusi ylimääräisten taitojen tyyppi
     */
    public void setExtraProfType(String type) {
        this.extraProfType = type;
    }

    /**
     * @param profs lista featille asetettavista varmoista taidoista
     */
    public void setCertainProfs(List<Proficiency> profs) {
        this.certainProfs = profs;
    }

    /**
     * @param profs lista featille asetettavista epävarmoista taidoista
     */
    public void setUncertainProfs(List<Proficiency> profs) {
        this.uncertainProfs = profs;
    }

    /**
     * Lisätään featin antama varma taito, jos sitä ei featilla vielä ole
     *
     * @param prof featin antama varma taito
     */
    public void addCertainProf(Proficiency prof) {
        if (!this.certainProfs.contains(prof)) {
            this.certainProfs.add(prof);
        }
    }

    /**
     * Lisätään featin antama epävarma taito, jos sitä ei featilla vielä ole
     *
     * @param prof featin antama epävarma taito
     */
    public void addUncertainProf(Proficiency prof) {
        if (!this.uncertainProfs.contains(prof)) {
            this.uncertainProfs.add(prof);
        }
    }

    /**
     * @param stats featin antamat uudet piirteet
     */
    public void setStats(String stats) {
        this.stats = stats;
    }

    /**
     * featin uniikkiuteen vaikuttaa sen nimi
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * feattien 'samuuteen' vaikuttaa nimi
     *
     * @param obj verrattava
     * @return olivatko objektit samat
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Feat other = (Feat) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
