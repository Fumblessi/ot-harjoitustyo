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
public class RPGCharacter {
    
    private String race;
    private String charClass;
    private String background;
    private HashMap<String, Integer> stats;
    private ProficiencyList profs;
    
    public RPGCharacter() {
        this.race = "";
        this.charClass = "";
        this.background = "";
        this.stats = new HashMap<>();
        this.profs = new ProficiencyList();
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
    
    public HashMap getStats() {
        return this.stats;
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
    
    public void setStats(int[] stats) {       
        setStat("STR", stats[0]);
        setStat("DEX", stats[1]);
        setStat("CON", stats[2]);
        setStat("INT", stats[3]);
        setStat("WIS", stats[4]);
        setStat("CHA", stats[5]);
    }
}
