/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import java.sql.*;
import hahmogeneraattori.domain.Background;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
/**
 *
 * @author sampo
 */
@Component
public class SQLBgDao implements BackgroundDao {
    
    @Override
    public void connect() throws SQLException {
        
    }
    
    @Override
    public void create(Background bg) throws SQLException {
        
    }
    
    @Override
    public Background read(Integer key) throws SQLException {
        return null;
    }
    
    @Override
    public Background update(Background bg) throws SQLException {
        return null;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        
    }
    
    @Override
    public List<Background> list() throws SQLException {
        return null;
    }
}
