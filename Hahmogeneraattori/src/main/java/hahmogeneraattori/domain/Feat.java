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
    
    public int[] getStats() {
        return this.stats;
    }
    
    public void setStatsFromString(String stats) {
        String[] parts = stats.split("|");
        for (int i = 0; i < parts.length; i++) {
            this.stats[i] = 1;
        }
    }
}
