/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.domain;

import java.util.ArrayList;

/**
 *
 * @author sampo
 */
public class Background {
    
    private String name;
    private String feature;
    private ArrayList<String> skills;
    private ArrayList<String> tools;
    private ArrayList<String> languages;
    
    public Background() {
        this.name = "";
        this.feature= "";
        this.skills = new ArrayList<>();
        this.tools = new ArrayList<>();
        this.languages = new ArrayList<>();
    }
    
}
