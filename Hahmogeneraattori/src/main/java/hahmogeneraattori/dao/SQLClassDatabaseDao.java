/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import hahmogeneraattori.domain.RpgClass;
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
public class SQLClassDatabaseDao implements GeneratorDatabaseDao {
    
    private List<RpgClass> classes;
    private String connection;
    private String user;
    private String password;
    
    public SQLClassDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.classes = new ArrayList<>();
        
        initialize();
    }
    
    @Override
    public void create(Object obj) throws SQLException {
        RpgClass rpgclass = (RpgClass) obj;
        
        if (!this.classes.contains(rpgclass)) {
            Connection conn = openConnection();
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        RpgClass rpgclass = (RpgClass) obj;
        Connection conn = openConnection();

        conn.close();
        this.classes.remove(rpgclass);
    }
    
    @Override
    public List list(Class c) {
        return this.classes;
    }
    
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Class");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            PreparedStatement stmtSubclass = conn.prepareStatement("SELECT * FROM"
                    + " SubClass WHERE class_id = ?");
            int id = rs.getInt(1);
            stmtSubclass.setInt(1, id);
            ResultSet rsSubclass = stmtSubclass.executeQuery();
            RpgClass rpgClass = new RpgClass(id, rs.getString(2));
            ArrayList<String> subclasses = new ArrayList<>();
            while (rsSubclass.next()) {
                subclasses.add(rsSubclass.getString(3));
            }
            rpgClass.setSubclasses(subclasses);
            this.classes.add(rpgClass);
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
}
