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

public class Racial {
    
    private int id;
    private String name;
    private List<Proficiency> racialProfs;
    
    public Racial(int id, String name) {
        this.id = id;
        this.name = name;
        this.racialProfs = new ArrayList<>();
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
    
    public List<Proficiency> getRacialProfs() {
        return this.racialProfs;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setRacialProfs(List<Proficiency> profs) {
        this.racialProfs = profs;
    }
    
    public void addRacialProf(Proficiency prof) {
        if (!this.racialProfs.contains(prof)) {
            this.racialProfs.add(prof);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final Racial other = (Racial) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
