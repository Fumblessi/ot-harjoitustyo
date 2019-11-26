/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hahmogeneraattori.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author sampo
 */
public class ProficiencyList {

    private List<String> skills;
    private List<String> saves;
    private List<String> armors;
    private List<String> weapons;
    private List<String> tools;
    private List<String> languages;

    public ProficiencyList() {
        this.skills = new ArrayList<>();
        this.saves = new ArrayList<>();
        this.armors = new ArrayList<>();
        this.weapons = new ArrayList<>();
        this.tools = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    public boolean addProf(String profType, String skill) {
        switch (profType) {
            case "skill":
                return addIfAbsent(this.skills, skill);
            case "save":
                return addIfAbsent(this.saves, skill);
            case "armor":
                return addIfAbsent(this.armors, skill);
            case "weapon":
                return addIfAbsent(this.weapons, skill);
            case "tool":
                return addIfAbsent(this.tools, skill);
            case "language":
                return addIfAbsent(this.languages, skill);
        }
        return true;
    }
    
    public boolean addIfAbsent(List profType, String skill) {
        if (profType.contains(skill)) {
            return false;
        }
        profType.add(skill);
        return true;
    }

    public void sortSkills() {
        Collections.sort(this.skills);
    }

    @Override
    public String toString() {
        sortSkills();
        String print = "";
        return print;
    }
}
