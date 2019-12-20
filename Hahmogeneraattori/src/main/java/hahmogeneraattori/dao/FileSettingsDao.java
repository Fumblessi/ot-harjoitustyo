/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Luokka tallentaa käyttäjän asettamat asetukset tekstitiedostoon
 *
 * @author sampo
 */
public class FileSettingsDao implements SettingsDao {

    private String file;
    private HashMap<String, Integer> intSettings;
    private HashMap<String, Double> doubleSettings;

    /**
     * Konstruktori saa parametrinaan tekstitiedoston, josta asetukset luetaan
     *
     * @param file asetustiedosto
     * @throws Exception
     */
    public FileSettingsDao(String file) throws Exception {
        this.intSettings = new HashMap<>();
        this.doubleSettings = new HashMap<>();
        this.file = file;

        try {
            Scanner reader = new Scanner(new File(this.file));
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(" ");
                int isDouble = Integer.parseInt(parts[0]);
                if (isDouble == 0) {
                    int value = Integer.parseInt(parts[2]);

                    this.intSettings.put(parts[1], value);
                } else if (isDouble == 1) {
                    double value = Double.parseDouble(parts[2]);

                    this.doubleSettings.put(parts[1], value);
                }
            }
        } catch (Exception e) {
            FileWriter writer = new FileWriter(new File(this.file));
            writer.close();
        }
    }

    /**
     * Metodi asettaa tietylle kokonaisluku-asetukselle uuden arvon
     *
     * @param setting muutettava asetus
     * @param value uusi asetuksen arvo
     */
    @Override
    public void setIntValue(String setting, int value) {
        this.intSettings.put(setting, value);
    }

    /**
     * Metodi asettaa tietylle double-asetukselle uuden arvon
     *
     * @param setting muutettava asetus
     * @param value uusi asetuksen arvo
     */
    @Override
    public void setDoubleValue(String setting, double value) {
        this.doubleSettings.put(setting, value);
    }

    /**
     * Metodi hakee tietyn kokonaisluku-asetuksen arvon
     *
     * @param setting haettu asetus
     * @return asetuksen arvo
     */
    @Override
    public int getIntValue(String setting) {
        return this.intSettings.get(setting);
    }

    /**
     * Metodi hakee tietyn double-asetuksen arvon
     *
     * @param setting haettu asetus
     * @return asetuksen arvo
     */
    @Override
    public double getDoubleValue(String setting) {
        return this.doubleSettings.get(setting);
    }

    /**
     * metodi päivittää tekstitiedostoon uudet asetukset
     *
     * @throws Exception
     */
    @Override
    public void update() throws Exception {
        try (FileWriter writer = new FileWriter(new File(this.file))) {
            for (String setting : this.intSettings.keySet()) {
                String value = String.valueOf(this.intSettings.get(setting));
                writer.write("0 " + setting + " " + value + "\n");
            }
            for (String setting : this.doubleSettings.keySet()) {
                String value = String.valueOf(this.doubleSettings.get(setting));
                writer.write("1 " + setting + " " + value + "\n");
            }
        }
    }
}
