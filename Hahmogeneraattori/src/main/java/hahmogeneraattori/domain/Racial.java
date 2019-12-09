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
 * Luokka kuvaa tietokantataulusta 'Racial' muodostettavat oliot, jotka
 * kuvaavat hahmon rodun puolesta saamia ominaisuuksia
 * 
 * @author sampo
 */
public class Racial {
    
    private int id;
    private String name;
    private int stats;
    private boolean feat;
    private List<Proficiency> racialProfs;
    
    /**
     * Racialilla on indeksi ja nimi, joiden avulla se voidaan uniikisti 
     * tunnistaa. Sen lisäksi racial saattaa vaikuttaa hahmon piirteisiin tai 
     * uuden antaa uuden featin (erityistaidon), ja siihen liittyy lista
     * taitoja, jotka se saattaa hahmolle antaa.
     * 
     * @param id racialin indeksi
     * @param name racialin nimi
     * @param stats racialin antamat tai vähentämät piirteet
     * @param feat tieto, antaako racial uuden featin vai ei
     */
    public Racial(int id, String name, int stats, boolean feat) {
        this.id = id;
        this.name = name;
        this.stats = stats;
        this.feat = feat;
        this.racialProfs = new ArrayList<>();
    }
    
    /**
     * Racial-olion voi luoda myös ilman indeksiä (esimerkiksi uutta
     * tietokantaan lisättävää racialia luodessa), jolloin asetetaan 
     * indeksiksi väliaikaisesti -1
     * 
     * @param name racialin nimi
     * @param stats racialin antamat tai vähentämät piirteet
     * @param feat tieto, antaako racial uuden featin vai ei
     */
    public Racial(String name, int stats, boolean feat) {
        this(-1, name, stats, feat);
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
     * @return lista racialiin liittyvistä taidoista
     */
    public List<Proficiency> getRacialProfs() {
        return this.racialProfs;
    }
    
    /**
     * Metodi asetaa racialille nimen
     * 
     * @param name uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Metodi asetaa racialin antamat piirteet
     * 
     * @param stats uusi piirremäärä
     */
    public void setStats(int stats) {
        this.stats = stats;
    }
    
    /**
     * Metodi asetaa tiedon siitä, kuuluko racialiin feat
     * 
     * @param value kuuluuko racialiin feat
     */
    public void setFeat(boolean value) {
        this.feat = value;
    }
    
    /**
     * Metodi asettaa racialille indeksin, mutta vain, jos sillä ei
     * vielä sellaista ole
     * 
     * @param id 
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    /**
     * Metodi antaa racialille siihen liittyvän taidot
     * 
     * @param profs lista taidoista
     */
    public void setRacialProfs(List<Proficiency> profs) {
        this.racialProfs = profs;
    }
    
    /**
     * Metodi lisää racialiin liittyvän taidon
     * 
     * @param prof uusi racialiin liittyvä taito
     */
    public void addRacialProf(Proficiency prof) {
        if (!this.racialProfs.contains(prof)) {
            this.racialProfs.add(prof);
        }
    }

    /**
     * racialin uniikkiuteen vaikuttaa sen nimi
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.name);
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
