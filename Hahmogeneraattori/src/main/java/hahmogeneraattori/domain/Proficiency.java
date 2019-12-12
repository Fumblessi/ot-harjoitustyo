/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.Objects;

/**
 * Luokka kuvaa tietokantataulusta 'Proficiency' muodostettavat oliot, jotka
 * kuvaavat hahmon omaamia taitoja, ja joita generoidulle hahmolle arvotaan.
 * 
 * @author sampo
 */
public class Proficiency {
    
    private int id;
    private String name;
    private String type;
    private String subtype;
    
    /**
     * Proficiencyillä on indeksi ja nimi, joiden avulla ne voidaan uniikisti 
     * tunnistaa. Tämän lisäksi proficiencyt jakautuvat eri kategorioihin niiden 
     * tyypin mukaan.
     * 
     * @param id proficiencyn indeksi
     * @param name proficiencyn nimi
     * @param type proficiencyn tyyppi
     */
    public Proficiency(int id, String name, String type, String subtype) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subtype = subtype;
    }
    
    /**
     * Proficiency-olion voi luoda myös ilman indeksiä (esimerkiksi uutta
     * tietokantaan lisättävää proficiencyä luodessa), jolloin asetetaan 
     * indeksiksi väliaikaisesti -1
     * 
     * @param name proficiencyn nimi
     * @param type proficiencyn tyyppi
     */
    public Proficiency(String name, String type, String subtype) {
        this(-1, name, type, subtype);
    }
    
    /**
     * @return proficiencyn indeksi
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * @return proficiencyn nimi
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return proficiencyn tyyppi
     */
    public String getType() {
        return this.type;
    }
    
    public String getSubtype() {
        return this.subtype;
    }
    
    /**
     * Metodi asettaa proficiencylle indeksin, mutta vain, jos sillä ei
     * vielä sellaista ole
     * 
     * @param id uusi indeksi
     */
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    /**
     * @param name prociencyn uusi nimi
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @param type proficiencyn uusi tyyppi
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * proficiencyn uniikkiuteen vaikuttaa sen nimi
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * proficiencyjen 'samuuteen' vaikuttaa nimi
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
        final Proficiency other = (Proficiency) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
