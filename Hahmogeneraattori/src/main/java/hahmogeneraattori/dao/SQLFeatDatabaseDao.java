/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Feat;
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
public class SQLFeatDatabaseDao implements GeneratorDatabaseDao {
    
    private List<Feat> feats;
    private String connection;
    private String user;
    private String password;
    
    public SQLFeatDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.feats = new ArrayList<>();
        
        initialize();
    }
    
    @Override
    public void create(Object obj) throws SQLException {
        Feat feat = (Feat) obj;
        
        if (!this.feats.contains(feat)) {
            Connection conn = openConnection();
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        Feat feat = (Feat) obj;
        Connection conn = openConnection();

        conn.close();
        this.feats.remove(feat);
    }
    
    @Override
    public List list(Class c) {
        return this.feats;
    }
    
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Feat");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Feat feat = new Feat(rs.getInt(1), rs.getString(2));
            feat.setStatsFromString(rs.getString(3));
            this.feats.add(feat);
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
}
