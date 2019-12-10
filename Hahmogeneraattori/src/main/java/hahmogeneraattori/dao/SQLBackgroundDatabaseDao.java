/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Background;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sampo
 */
public class SQLBackgroundDatabaseDao implements GeneratorDatabaseDao {
    
    private List<Background> backgrounds;
    private String connection;
    private String user;
    private String password;
    
    public SQLBackgroundDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.backgrounds = new ArrayList<>();
        
        initialize();
    }
    
    @Override
    public void create(Object obj) throws SQLException {
        Background bg = (Background) obj;
        
        if (!this.backgrounds.contains(bg)) {
            Connection conn = openConnection();
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        Background bg = (Background) obj;
        Connection conn = openConnection();

        conn.close();
        this.backgrounds.remove(bg);
    }
    
    @Override
    public List list(Class c) {
        return this.backgrounds;
    }
    
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Background");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            this.backgrounds.add(new Background(rs.getInt(1), rs.getString(2)));
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
}
