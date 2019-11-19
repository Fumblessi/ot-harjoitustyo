/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.dao;

/**
 *
 * @author sampo
 */
public interface SettingsDao {
    void setValue(String setting, int value);
    //asetetaan asetukselle uusi arvo
    int getValue(String setting);
    //haetaan tietyn asetuksen arvo
    void update() throws Exception;
    //päivitetään muutokset tiedostoon
}
