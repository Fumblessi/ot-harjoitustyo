/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Background;
import hahmogeneraattori.domain.Proficiency;
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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Background "
                    + "(name, feature) VALUES (?, ?);");
            stmt.setString(1, bg.getName());
            stmt.setString(2, bg.getFeature());
            stmt.executeUpdate();
            stmt.close();

            bg.setId(getBackgroundId(bg, conn));

            this.backgrounds.add(bg);
            
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        Background bg = (Background) obj;

        Connection conn = openConnection();

        updateBackgroundToBackgrounds(bg);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Background "
                + "SET name = ?, feature = ? WHERE id = ?;");
        stmt.setString(1, bg.getName());
        stmt.setString(2, bg.getFeature());
        stmt.setInt(3, bg.getId());
        stmt.executeUpdate();
        stmt.close();

        conn.close();
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        Background bg = (Background) obj;
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Background "
                + "WHERE id = ?;");
        stmt.setInt(1, bg.getId());

        stmt.executeUpdate();
        stmt.close();
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
            Background newBg = new Background(rs.getInt(1), rs.getString(2), 
                rs.getString(3));
            
            this.backgrounds.add(newBg);
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
    
    public int getBackgroundId(Background bg, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Background WHERE name = ?;");
        stmt.setString(1, bg.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        return id;
    }
    
    public void updateBackgroundToBackgrounds(Background bg) {
        for (Background oldBg : this.backgrounds) {
            if (oldBg.getId() == bg.getId()) {
                oldBg.setName(bg.getName());
                oldBg.setFeature(bg.getFeature());
                break;
            }
        }
    }
}
