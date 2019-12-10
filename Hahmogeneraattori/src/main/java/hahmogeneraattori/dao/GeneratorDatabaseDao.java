/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

/**
 *
 * @author sampo
 */
public interface GeneratorDatabaseDao {
    void create(Object obj) throws SQLException;
    void update(Object obj) throws SQLException;
    void delete(Object obj) throws SQLException;
    List list(Class c);
    void initialize() throws SQLException;
    Connection openConnection() throws SQLException;
}
