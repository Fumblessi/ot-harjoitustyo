/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.Objects;

/**
 * Luokka kuvaa tietokantataulusta 'Background' muodostettavat oliot, jotka
 * kuvaavat hahmon taustaa, joka generoidulle hahmolle arvotaan.
 *
 * @author sampo
 */
public class Background {

    private int id;
    private String name;
    private String feature;

    /**
     * Backgroundeilla on indeksi ja nimi, joiden avulla ne voidaan uniikisti
     * tunnistaa. Tämän lisäksi backgroundiin liittyy jokin sen antama
     * ominaisuus.
     *
     * @param id backgroundin indeksi
     * @param name backgroundin nimi
     * @param feature backgroundin ominaisuus
     */
    public Background(int id, String name, String feature) {
        this.id = id;
        this.name = name;
        this.feature = feature;
    }

    /**
     * Background-olion voi luoda myös ilman indeksiä (esimerkiksi uutta
     * tietokantaan lisättävää backgroundia luodessa), jolloin asetetaan
     * indeksiksi väliaikaisesti -1
     *
     * @param name backgroundin nimi
     * @param feature backgroundin ominaisuus
     */
    public Background(String name, String feature) {
        this(-1, name, feature);
    }

    /**
     * @return backgroundin indeksi
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return backgroundin nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return backgroundin feature
     */
    public String getFeature() {
        return this.feature;
    }

    /**
     * Metodi asettaa backgroundille indeksin, mutta vain, jos sillä ei vielä
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
     * @param name backgroundin uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param feature backgroundin uusi ominaisuus
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * backgroundin uniikkiuteen vaikuttaa sen nimi
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * backgroundin 'samuuteen' vaikuttaa nimi
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
        final Background other = (Background) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
