/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
/**
 *
 * @author sampo
 */
public class RpgClass {
    
    private int id;
    private String name;
    private List<String> subclasses;
    private List<Proficiency> classProfs;
    
    public RpgClass(int id, String name) {
        this.id = id;
        this.name = name;
        this.subclasses = new ArrayList<>();
        this.classProfs = new ArrayList<>();
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
    
    public List<Proficiency> getClassProfs() {
        return this.classProfs;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public void setClassProfs(List<Proficiency> profs) {
        this.classProfs = profs;
    }
    
    public void addClassProf(Proficiency prof) {
        if (!this.classProfs.contains(prof)) {
            this.classProfs.add(prof);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RpgClass other = (RpgClass) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

       
}
