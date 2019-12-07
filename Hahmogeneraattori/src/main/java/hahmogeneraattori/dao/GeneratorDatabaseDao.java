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
public interface GeneratorDatabaseDao {
    void create(Proficiency prof) throws SQLException;
    void updateProf(Proficiency oldProf, Proficiency newProf) throws SQLException;
    void deleteProf(Proficiency prof) throws SQLException;
    List<Proficiency> listProfs();    
}
