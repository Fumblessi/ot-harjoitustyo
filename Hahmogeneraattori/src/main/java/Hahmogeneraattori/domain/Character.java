/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.domain;

import java.util.*;
/**
 *
 * @author sampo
 */
public class Character {
    
    private String race;
    private String charClass;
    private String background;
    private HashMap<String, Integer> stats;
    
    public Character(String race, String charClass, String background) {
        this.race = race;
        this.charClass = charClass;
        this.background = background;
        this.stats = new HashMap<>();
    }
    
    public String getRace() {
        return this.race;
    }
    
    public String getCharClass() {
        return this.charClass;
    }
    
    public String getBackground() {
        return this.background;
    }
    
    public int getStat(String stat) {
        return this.stats.get(stat);
    }
    
    public void setRace(String race) {
        this.race = race;
    }
    
    public void setCharClass(String charClass) {
        this.charClass = charClass;
    }
    
    public void setBackground(String background) {
        this.background = background;
    }
    
    public void setStat(String stat, int value) {
        this.stats.put(stat, value);
    }
}
