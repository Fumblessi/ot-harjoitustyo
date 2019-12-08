/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

/**
 *
 * @author sampo
 */
public class Feat {
    
    private String name;
    private int[] stats;
    
    public Feat(String name) {
        this.name = name;
        this.stats = new int[6];
        for (int i = 0; i < 6; i++) {
            this.stats[i] = 0;
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getStats() {
        String stats = "";
        if (this.stats[0] == 1) {
            stats += "STR/";
        }
        if (this.stats[1] == 1) {
            stats += "DEX/";
        }
        if (this.stats[2] == 1) {
            stats += "CON/";
        }
        if (this.stats[3] == 1) {
            stats += "INT/";
        }
        if (this.stats[4] == 1) {
            stats += "WIS/";
        }
        if (this.stats[5] == 1) {
            stats += "CHA/";
        }
        if (!stats.isEmpty()) {
            stats = stats.substring(0, stats.length() - 1);
        }
        return stats;
    }
    
    public void setStatsFromString(String stats) {
        String[] parts = stats.split("|");
        for (int i = 0; i < parts.length; i++) {
            this.stats[i] = 1;
        }
    }
}
