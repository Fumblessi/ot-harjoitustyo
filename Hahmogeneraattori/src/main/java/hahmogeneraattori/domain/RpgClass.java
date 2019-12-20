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
 * Luokka kuvaa tietokantataulusta 'Class' muodostettavat oliot, jotka kuvaavat
 * hahmon hahmoluokkaa, joka generoidulle hahmolle arvotaan.
 *
 * @author sampo
 */
public class RpgClass {

    private int id;
    private String name;
    private List<String> subclasses;
    private List<Proficiency> certainProfs;
    private List<Proficiency> uncertainProfs;
    private int randomProfs;
    private int randomLangs;
    private int extraProfs;
    private String extraProfType;

    /**
     * Classilla on indeksi ja nimi, joiden avulla ne voidaan uniikisti
     * tunnistaa. Tämän lisäksi class voi antaa hahmolle uusia taitoja tai
     * kieliä.
     *
     * @param id featin indeksi
     * @param name featin nimi
     * @param randomProfs featin antamat epävarmat taidot
     * @param randomLangs featin antamat lisäkielet
     * @param extraProfs featin antamat ylimääräiset taidot
     * @param extraProfType ylimääräisten taitojen tyyppi
     */
    public RpgClass(int id, String name, int randomProfs, int randomLangs, int extraProfs, String extraProfType) {
        this.id = id;
        this.name = name;
        this.randomProfs = randomProfs;
        this.randomLangs = randomLangs;
        this.extraProfs = extraProfs;
        this.extraProfType = extraProfType;
        this.subclasses = new ArrayList<>();
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
    }

    /**
     * Class-olion voi luoda myös ilman indeksiä (esimerkiksi uutta tietokantaan
     * lisättävää feattia luodessa), jolloin asetetaan indeksiksi väliaikaisesti
     * -1
     *
     * @param name featin nimi
     * @param randomProfs featin antamat epävarmat taidot
     * @param randomLangs featin antamat lisäkielet
     * @param extraProfs featin antamat ylimääräiset taidot
     * @param extraProfType ylimääräisten taitojen tyyppi
     */
    public RpgClass(String name, int randomProfs, int randomLangs, int extraProfs,
            String extraProfType) {
        this(-1, name, randomProfs, randomLangs, extraProfs, extraProfType);
    }

    /**
     * @return classin indeksi
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return classin nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return classin antamien epävarmojen taitojen määrä
     */
    public int getRandomProfs() {
        return this.randomProfs;
    }

    /**
     * @return classin antamien lisäkielien määrä
     */
    public int getRandomLangs() {
        return this.randomLangs;
    }

    /**
     * @return classin antamien ylimääräisten taitojen tyyppi
     */
    public String getExtraProfType() {
        return this.extraProfType;
    }

    /**
     * @return classin antamien ylimääräisten taitojen määrä
     */
    public int getExtraProfs() {
        return this.extraProfs;
    }

    /**
     * @return lista classin alaluokista
     */
    public List<String> getSubclasses() {
        return this.subclasses;
    }

    /**
     * @return lista classin antamista varmoista taidoista
     */
    public List<Proficiency> getCertainProfs() {
        return this.certainProfs;
    }

    /**
     * @return lista classin antamista epävarmoista taidoista
     */
    public List<Proficiency> getUncertainProfs() {
        return this.uncertainProfs;
    }

    /**
     * Metodi asettaa classille indeksin, mutta vain, jos sillä ei vielä
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
     * @param name classin uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param amount classin antamien epävarmojen taitojen uusi määrä
     */
    public void setRandomProfs(int amount) {
        this.randomProfs = amount;
    }

    /**
     * @param amount classin antamien lisäkielien uusi määrä
     */
    public void setRandomLangs(int amount) {
        this.randomLangs = amount;
    }

    /**
     * @param amount classin antamien ylimääräisten taitojen uusi määrä
     */
    public void setExtraProfs(int amount) {
        this.extraProfs = amount;
    }

    /**
     * @param type classin antamien ylimääräisten taitojen uusi tyyppi
     */
    public void setExtraProfType(String type) {
        this.extraProfType = type;
    }

    /**
     * @param subclasses lista classille asetettavista alaluokista
     */
    public void setSubclasses(List<String> subclasses) {
        this.subclasses = subclasses;
    }

    /**
     * @param subclass classille lisättävä alaluokka
     */
    public void addSubclass(String subclass) {
        if (!this.subclasses.contains(subclass)) {
            this.subclasses.add(subclass);
        }
    }

    /**
     * @param subclass classilta poistettava alaluokka
     */
    public void removeSubclass(String subclass) {
        this.subclasses.remove(subclass);
    }

    /**
     * @param profs lista classille asetettavista sen antamista varmoista
     * taidoista
     */
    public void setCertainProfs(List<Proficiency> profs) {
        this.certainProfs = profs;
    }

    /**
     * @param profs lista classille asetettavista sen antamista epävarmoista
     * taidoista
     */
    public void setUncertainProfs(List<Proficiency> profs) {
        this.uncertainProfs = profs;
    }

    /**
     * @param prof classille lisättävä varma taito, jos sitä class ei vielä anna
     */
    public void addCertainProf(Proficiency prof) {
        if (!this.certainProfs.contains(prof)) {
            this.certainProfs.add(prof);
        }
    }

    /**
     * @param prof classille lisättävä epävarma taito, jos sitä class ei vielä
     * anna
     */
    public void addUncertainProf(Proficiency prof) {
        if (!this.uncertainProfs.contains(prof)) {
            this.uncertainProfs.add(prof);
        }
    }

    /**
     * classin uniikkiuteen vaikuttaa sen nimi
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * classien 'samuuteen' vaikuttaa nimi
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
        final RpgClass other = (RpgClass) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
