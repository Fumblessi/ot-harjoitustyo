/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author sampo
 */
public class Background {
    
    private int id;
    private String name;
    private String feature;
    
    public Background(int id, String name, String feature) {
        this.id = id;
        this.name = name;
        this.feature = feature;
    }
    
    public Background(String name, String feature) {
        this(-1, name, feature);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getFeature() {
        return this.feature;
    }
    
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
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
        final Background other = (Background) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}