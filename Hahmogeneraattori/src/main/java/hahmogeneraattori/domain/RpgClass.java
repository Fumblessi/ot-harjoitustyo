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
    
    private String name;
    private List<String> subclasses;
    
    public RpgClass(String name) {
        this.name = name;
        this.subclasses = new ArrayList<>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getSubclasses() {
        return this.subclasses;
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
