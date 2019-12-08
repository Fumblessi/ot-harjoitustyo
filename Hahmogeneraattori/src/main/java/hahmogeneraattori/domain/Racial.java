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

public class Racial {
    
    private int id;
    private String name;
    
    public Racial(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Racial(String name) {
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
