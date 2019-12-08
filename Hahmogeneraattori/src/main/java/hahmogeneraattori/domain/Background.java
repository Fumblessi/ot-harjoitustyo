/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.ArrayList;

/**
 *
 * @author sampo
 */
public class Background {
    
    private int id;
    private String name;
    
    public Background(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Background(String name) {
        this(-1, name);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
}
