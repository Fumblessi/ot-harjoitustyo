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
public class Proficiency {
    
    private String name;
    private String type;
    
    
    public Proficiency(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return this.name + ", type: " + this.type;
    }
}
