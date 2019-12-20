/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.Objects;

/**
 * Luokka kuvaa tietokantataulusta 'Race' muodostettavat oliot, jotka kuvaavat
 * hahmon rotua, joka generoidulle hahmolle arvotaan.
 *
 * @author sampo
 */
public class Race {

    private int id;
    private String name;

    /**
     * Raceilla on indeksi ja nimi, joiden avulla ne voidaan uniikisti tunnistaa
     *
     * @param id backgroundin indeksi
     * @param name backgroundin nimi
     */
    public Race(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Race-olion voi luoda myös ilman indeksiä (esimerkiksi uutta tietokantaan
     * lisättävää backgroundia luodessa), jolloin asetetaan indeksiksi
     * väliaikaisesti -1
     *
     * @param name backgroundin nimi
     */
    public Race(String name) {
        this(-1, name);
    }

    /**
     * Metodi asettaa racelle indeksin, mutta vain, jos sillä ei vielä sellaista
     * ole
     *
     * @param id uusi indeksi
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }

    /**
     * @param name racen uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return racen indeksi
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return racen nimi
     */
    public String getName() {
        return this.name;
    }

    /**
     * racen uniikkiuteen vaikuttaa sen nimi
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * racen 'samuuteen' vaikuttaa nimi
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
        final Race other = (Race) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
