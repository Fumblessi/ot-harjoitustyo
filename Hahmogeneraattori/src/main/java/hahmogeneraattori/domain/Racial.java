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
 * Luokka kuvaa tietokantataulusta 'Racial' muodostettavat oliot, jotka kuvaavat
 * hahmon rodun puolesta saamia ominaisuuksia
 *
 * @author sampo
 */
public class Racial {

    private int id;
    private String name;
    private int stats;
    private boolean feat;
    private int randomProfs;
    private int randomLangs;
    private int extraProfs;
    private String extraProfType;
    private List<Proficiency> certainProfs;
    private List<Proficiency> uncertainProfs;

    /**
     * Racialilla on indeksi ja nimi, joiden avulla se voidaan uniikisti
     * tunnistaa. Sen lisäksi racial saattaa vaikuttaa hahmon piirteisiin tai
     * uuden antaa uuden featin (erityistaidon), ja se voi antaa hahmolle
     * taitoja tai kieliä.
     *
     * @param id racialin indeksi
     * @param name racialin nimi
     * @param stats racialin antamat tai vähentämät piirteet
     * @param feat tieto, antaako racial uuden featin vai ei
     * @param randomProfs racialin antamat epävarmat taidot
     * @param randomLangs racialin antamat lisäkielet
     * @param extraProfs racialin antamat ylimääräiset taidot
     * @param extraProfType racialin antamien ylimääräisten taitojen tyyppi
     */
    public Racial(int id, String name, int stats, boolean feat, int randomProfs,
            int randomLangs, int extraProfs, String extraProfType) {
        this.id = id;
        this.name = name;
        this.stats = stats;
        this.feat = feat;
        this.randomProfs = randomProfs;
        this.randomLangs = randomLangs;
        this.extraProfs = extraProfs;
        this.extraProfType = extraProfType;
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
    }

    /**
     * Racial-olion voi luoda myös ilman indeksiä (esimerkiksi uutta
     * tietokantaan lisättävää racialia luodessa), jolloin asetetaan indeksiksi
     * väliaikaisesti -1
     *
     * @param name racialin nimi
     * @param stats racialin antamat tai vähentämät piirteet
     * @param feat tieto, antaako racial uuden featin vai ei
     * @param randomProfs racialin antamat epävarmat taidot
     * @param randomLangs racialin antamat lisäkielet
     * @param extraProfs racialin antamat ylimääräiset taidot
     * @param extraProfType racialin antamien ylimääräisten taitojen tyyppi
     */
    public Racial(String name, int stats, boolean feat, int randomProfs,
            int randomLangs, int extraProfs, String extraProfType) {
        this(-1, name, stats, feat, randomProfs, randomLangs, extraProfs,
                extraProfType);
    }

    /**
     * @return racialin indeksi
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return racialin nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return racialin antamat tai vähentämät piirteet
     */
    public int getStats() {
        return this.stats;
    }

    /**
     * @return tieto, antaako racial uuden featin vai ei
     */
    public boolean getFeat() {
        return this.feat;
    }

    /**
     * @return racialin antamien epävarmojen taitojen määrä
     */
    public int getRandomProfs() {
        return this.randomProfs;
    }

    /**
     * @return racialin antamien lisäkielien määrä
     */
    public int getRandomLangs() {
        return this.randomLangs;
    }

    /**
     * @return racialin antamien ylimääräisten taitojen määrä
     */
    public int getExtraProfs() {
        return this.extraProfs;
    }

    /**
     * @return racialin antamien ylimääräisten taitojen tyyppi
     */
    public String getExtraProfType() {
        return this.extraProfType;
    }

    /**
     * @return lista racialin antamista varmoista taidoista
     */
    public List<Proficiency> getCertainProfs() {
        return this.certainProfs;
    }

    /**
     * @return lista racialin antamista epävarmoista taidoista
     */
    public List<Proficiency> getUncertainProfs() {
        return this.uncertainProfs;
    }

    /**
     * @param name racialin uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param stats racialin uusi piirremäärä
     */
    public void setStats(int stats) {
        this.stats = stats;
    }

    /**
     * @param value kuuluuko racialiin feat
     */
    public void setFeat(boolean value) {
        this.feat = value;
    }

    /**
     * Metodi asettaa racialille indeksin, mutta vain, jos sillä ei vielä
     * sellaista ole
     *
     * @param id
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }

    /**
     * @param amount racialin antamien epävarmojen taitojen määrä
     */
    public void setRandomProfs(int amount) {
        this.randomProfs = amount;
    }

    /**
     * @param amount racialin antamien lisäkielien määrä
     */
    public void setRandomLangs(int amount) {
        this.randomLangs = amount;
    }

    /**
     * @param amount racialin antamien ylimääräisten taitojen määrä
     */
    public void setExtraProfs(int amount) {
        this.extraProfs = amount;
    }

    public void setExtraProfType(String type) {
        this.extraProfType = type;
    }

    /**
     * @param profs lista racialin antamista varmoista taidoista
     */
    public void setCertainProfs(List<Proficiency> profs) {
        this.certainProfs = profs;
    }

    /**
     * @param profs lista racialin antamista epävarmoista taidoista
     */
    public void setUncertainProfs(List<Proficiency> profs) {
        this.uncertainProfs = profs;
    }

    /**
     * @param prof uusi racialin antama varma taito
     */
    public void addCertainProf(Proficiency prof) {
        if (!this.certainProfs.contains(prof)) {
            this.certainProfs.add(prof);
        }
    }

    /**
     * @param prof uusi racialin antama epävarma taito
     */
    public void addUncertainProf(Proficiency prof) {
        if (!this.uncertainProfs.contains(prof)) {
            this.uncertainProfs.add(prof);
        }
    }

    /**
     * racialin uniikkiuteen vaikuttaa sen nimi
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * racialien 'samuuteen' vaikuttaa nimi
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
        final Racial other = (Racial) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
