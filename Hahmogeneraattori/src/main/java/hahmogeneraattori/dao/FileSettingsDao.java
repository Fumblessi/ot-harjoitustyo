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
 *
 * @author sampo
 */
public class FileSettingsDao implements SettingsDao {
    
    private String file;
    private HashMap<String, Integer> settings;
    
    public FileSettingsDao(String file) throws Exception {
        this.settings = new HashMap<>();
        this.file = file;
        
        try {
            Scanner reader = new Scanner(new File(this.file));
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(" ");
                int value = Integer.parseInt(parts[1]);
                
                this.settings.put(parts[0], value);
            }
        } catch (Exception e) {
            FileWriter writer = new FileWriter(new File(this.file));
            writer.close();
        }
    }
    
    @Override
    public void setValue(String setting, int value) {
        this.settings.put(setting, value);
    }
    
    @Override
    public int getValue(String setting) {
        return this.settings.get(setting);
    }
    
    @Override
    public void update() throws Exception {
        try (FileWriter writer = new FileWriter(new File(this.file))) {
            for (String setting : this.settings.keySet()) {
                String value = String.valueOf(this.settings.get(setting));
                writer.write(setting + " " + value + "\n");
            }
        }
    }
}
