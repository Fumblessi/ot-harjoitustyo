/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sampo
 */
public interface SkillDao {
    void create(Proficiency prof) throws SQLException;
    Proficiency read(Integer key) throws SQLException;
    Proficiency update(Proficiency prof) throws SQLException;
    void delete(Integer key) throws SQLException;
    List<Proficiency> list();    
}
