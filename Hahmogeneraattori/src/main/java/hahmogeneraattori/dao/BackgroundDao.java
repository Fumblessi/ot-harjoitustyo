/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import java.sql.SQLException;
import hahmogeneraattori.domain.Background;
import java.util.List;
/**
 *
 * @author sampo
 */
public interface BackgroundDao {
    void create(Background bg) throws SQLException;
    Background read(Integer key) throws SQLException;
    Background update(Background bg) throws SQLException;
    void delete(Integer key) throws SQLException;
    List<Background> list() throws SQLException;
}