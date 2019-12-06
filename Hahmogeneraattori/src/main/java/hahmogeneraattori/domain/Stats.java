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
public class Stats {
    
    private int str;
    private int dex;
    private int con;
    private int inte;
    private int wis;
    private int cha;
    
    public Stats() {
        int[] stats = new int[6];
        for (int i = 0; i < 6; i++) {
            stats[i] = 10;
        }
        setStats(stats);
    }
    
    public void setStat(String stat, int value) {
        switch (stat) {
            case "str": this.str = value;
            case "dex": this.dex = value;
            case "con": this.con = value;
            case "inte": this.inte = value;
            case "wis": this.wis = value;
            case "cha": this.cha = value;
        }
    }
    
    public void setStats(int[] stats) {
        this.str = stats[0];
        this.dex = stats[1];
        this.con = stats[2];
        this.inte = stats[3];
        this.wis = stats[4];
        this.cha = stats[5];
    }
    
    public int getStat(String stat) {
        switch (stat) {
            case "str": return this.str;
            case "dex": return this.dex;
            case "con": return this.con;
            case "inte": return this.inte;
            case "wis": return this.wis;
            case "cha": return this.cha;
        }
        return -1;
    }
    
    public int[] getStats() {
        int[] stats = new int[6];
        stats[0] = this.str;
        stats[1] = this.dex;
        stats[2] = this.con;
        stats[3] = this.wis;
        stats[4] = this.inte;
        stats[5] = this.cha;
        
        return stats;
    }
    
    public int getMin() {
        int min = 100;
        int[] stats = getStats();
        for (int i = 0; i < 6; i++) {
            if (stats[i] < min) {
                min = stats[i];
            }
        }
        return min;
    }
    
    public int getMax() {
        int max = -1;
        int[] stats = getStats();
        for (int i = 0; i < 6; i++) {
            if (stats[i] > max) {
                max = stats[i];
            }
        }
        return max;
    }
    
    public int getSum() {
        int sum = 0;
        int[] stats = getStats();
        for (int i = 0; i < 6; i++) {
            sum += stats[i];
        }
        return sum;
    }
    
    @Override
    public String toString() {
        return "Stats:\n\nSTR: " + this.str + "\nDEX: " + this.dex + "\nCON: " + 
                this.con + "\nINT: " + this.inte + "\nWIS: " + this.wis + "\nCHA: " 
                + this.cha;
    }
}
