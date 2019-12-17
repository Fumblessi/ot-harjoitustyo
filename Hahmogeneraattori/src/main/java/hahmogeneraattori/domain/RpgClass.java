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
    private List<Proficiency> certainProfs;
    private List<Proficiency> uncertainProfs;
    private int randomProfs;
    private int randomLangs;
    private int extraProfs;
    private String extraProfType;
    
    public RpgClass(int id, String name, int randomProfs, int randomLangs, int 
            extraProfs, String extraProfType) {
        this.id = id;
        this.name = name;
        this.randomProfs = randomProfs;
        this.randomLangs = randomLangs;
        this.extraProfs = extraProfs;
        this.extraProfType = extraProfType;
        this.subclasses = new ArrayList<>();
        this.certainProfs = new ArrayList<>();
        this.uncertainProfs = new ArrayList<>();
    }
    
    public RpgClass(String name, int randomProfs, int randomLangs, int extraProfs, 
            String extraProfType) {
        this(-1, name, randomProfs, randomLangs, extraProfs, extraProfType);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getRandomProfs() {
        return this.randomProfs;
    }
    
    public int getRandomLangs() {
        return this.randomLangs;
    }
    
    public String getExtraProfType() {
        return this.extraProfType;
    }
    
    public int getExtraProfs() {
        return this.extraProfs;
    }
    
    public List<String> getSubclasses() {
        return this.subclasses;
    }
    
    public List<Proficiency> getCertainProfs() {
        return this.certainProfs;
    }
    
    public List<Proficiency> getUncertainProfs() {
        return this.uncertainProfs;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setRandomProfs(int amount) {
        this.randomProfs = amount;
    }
    
    public void setRandomLangs(int amount) {
        this.randomLangs = amount;
    }
    
    public void setExtraProfs(int amount) {
        this.extraProfs = amount;
    }
    
    public void setExtraProfType(String type) {
        this.extraProfType = type;
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
    
    public void setCertainProfs(List<Proficiency> profs) {
        this.certainProfs = profs;
    }
    
    public void setUncertainProfs(List<Proficiency> profs) {
        this.uncertainProfs = profs;
    }
    
    public void addCertainProf(Proficiency prof) {
        if (!this.certainProfs.contains(prof)) {
            this.certainProfs.add(prof);
        }
    }
    
    public void addUncertainProf(Proficiency prof) {
        if (!this.uncertainProfs.contains(prof)) {
            this.uncertainProfs.add(prof);
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