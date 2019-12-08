/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author sampo
 */
public class RpgClass {
    
    private int id;
    private String name;
    private List<String> subclasses;
    
    public RpgClass(int id, String name) {
        this.id = id;
        this.name = name;
        this.subclasses = new ArrayList<>();
    }
    
    public RpgClass(String name) {
        this(-1, name);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getSubclasses() {
        return this.subclasses;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setSubclasses(List<String> subclasses) {
        this.subclasses = subclasses;
    }
    
    public void addSubclass(String subclass) {
        if (!this.subclasses.contains(subclass)) {
            this.subclasses.add(subclass);
        }
    }
    
    public void removeSubclass(String subclass) {
        this.subclasses.remove(subclass);
    }
}
