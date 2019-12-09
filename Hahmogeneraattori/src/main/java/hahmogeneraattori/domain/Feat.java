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
    
    private int id;
    private String name;
    private int[] stats;
    
    public Feat(int id, String name) {
        this.name = name;
        this.stats = new int[6];
        this.id = id;
        for (int i = 0; i < 6; i++) {
            this.stats[i] = 0;
        }
    }
    
    public Feat(String name) {
        this(-1, name);
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getStats() {
        String statString = "";
        if (this.stats[0] == 1) {
            statString += "STR/";
        }
        if (this.stats[1] == 1) {
            statString += "DEX/";
        }
        if (this.stats[2] == 1) {
            statString += "CON/";
        }
        if (this.stats[3] == 1) {
            statString += "INT/";
        }
        if (this.stats[4] == 1) {
            statString += "WIS/";
        }
        if (this.stats[5] == 1) {
            statString += "CHA/";
        }
        if (!statString.isEmpty()) {
            statString = statString.substring(0, statString.length() - 1);
        }
        return statString;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setStatsFromString(String stats) {
        String[] parts = stats.split("/");
        for (int i = 0; i < parts.length; i++) {
            switch (parts[i]) {
                case "STR": this.stats[0] = 1;
                    break;
                case "DEX": this.stats[1] = 1;
                    break;
                case "CON": this.stats[2] = 1;
                    break;
                case "INT": this.stats[3] = 1;
                    break;
                case "WIS": this.stats[4] = 1;
                    break;
                case "CHA": this.stats[5] = 1;
                    break;
            }
        }
    }
}
