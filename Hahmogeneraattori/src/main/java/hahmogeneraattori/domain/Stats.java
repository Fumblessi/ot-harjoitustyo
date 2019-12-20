/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

/**
 * Luokan avulla käsitellään hahmolle arvottavia piirteitä (stats), joihin
 * kuuluu Strength (str), Dexterity (dex), Constitution (con), Intelligence
 * (inte), Wisdom (wis) ja Charisma (cha)
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

    /**
     * Konstruoitaessa uusi Stats-olio, saadaan piirteiden alkuarvot
     * kokonaislukutaulukosta
     *
     * @param stats piirteiden alkuarvot
     */
    public Stats(int[] stats) {
        this.str = stats[0];
        this.dex = stats[1];
        this.con = stats[2];
        this.inte = stats[3];
        this.wis = stats[4];
        this.cha = stats[5];
    }

    /**
     * Metodi asettaa tietylle piirteelle uuden arvon
     *
     * @param stat piirre
     * @param value uusi arvo
     */
    public void setStat(String stat, int value) {
        switch (stat) {
            case "str":
                this.str = value;
                break;
            case "dex":
                this.dex = value;
                break;
            case "con":
                this.con = value;
                break;
            case "inte":
                this.inte = value;
                break;
            case "wis":
                this.wis = value;
                break;
            case "cha":
                this.cha = value;
                break;
        }
    }

    /**
     * Metodi palauttaa kysytyn piirteen
     *
     * @param stat piirre
     * @return piirteen arvo
     */
    public int getStat(String stat) {
        switch (stat) {
            case "str":
                return this.str;
            case "dex":
                return this.dex;
            case "con":
                return this.con;
            case "inte":
                return this.inte;
            case "wis":
                return this.wis;
            case "cha":
                return this.cha;
        }
        return -1;
    }

    /**
     * Metodi palauttaa piirteet kokonaislukuja sisältävänä taulukkona
     *
     * @return piirteet taulukossa
     */
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

    /**
     * Metodi hakee piirteiden minimiarvon
     *
     * @see hahmogeneraattori.domain.Stats#getStats()
     *
     * @return arvoltaan pienin piirre
     */
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

    /**
     * Metodi hakee piirteiden maksimiarvon
     *
     * @see hahmogeneraattori.domain.Stats#getStats()
     *
     * @return arvoltaan suurin piirre
     */
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

    /**
     * Metodi hakee piirteiden summan
     *
     * @see hahmogeneraattori.domain.Stats#getStats()
     *
     * @return piirteiden summa
     */
    public int getSum() {
        int sum = 0;
        int[] stats = getStats();
        for (int i = 0; i < 6; i++) {
            sum += stats[i];
        }
        return sum;
    }

    /**
     * Metodi palauttaa piirteet String-muotoisena, eli siinä muodossa, missä ne
     * tulevat generaattorin käyttöliittymään näkyviin
     *
     * @return piirteen String-muotoisena
     */
    @Override
    public String toString() {
        return "Stats:\n\nSTR: " + this.str + "\nDEX: " + this.dex + "\nCON: "
                + this.con + "\nINT: " + this.inte + "\nWIS: " + this.wis + "\nCHA: "
                + this.cha;
    }
}
