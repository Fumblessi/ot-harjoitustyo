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
                    + "(name, randomProfs, randomLangs, extraProfs, extraProfType) "
                    + "VALUES (?, ?, ?, ?, ?);");
            stmt.setString(1, bg.getName());
            stmt.setInt(2, bg.getRandomProfs());
            stmt.setInt(3, bg.getRandomLangs());
            stmt.setInt(4, bg.getExtraProfs());
            stmt.setString(5, bg.getExtraProfType());
            stmt.executeUpdate();
            stmt.close();

            bg.setId(getBackgroundId(bg, conn));

            this.backgrounds.add(bg);

            addProficiencies(bg, conn);
            
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        Background bg = (Background) obj;

        Connection conn = openConnection();

        updateBackgroundToBackgrounds(bg);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Background "
                + "SET name = ?, randomProfs = ?, randomLangs = ?, extraProfs = ?, "
                + "extraProfType = ? WHERE id = ?;");
        stmt.setString(1, bg.getName());
        stmt.setInt(2, bg.getRandomProfs());
        stmt.setInt(3, bg.getRandomLangs());
        stmt.setInt(4, bg.getExtraProfs());
        stmt.setString(5, bg.getExtraProfType());
        stmt.setInt(6, bg.getId());
        stmt.executeUpdate();
        stmt.close();

        deleteBackgroundProficiencies(bg, conn);
        addCertainProficiencies(bg, conn);
        addUncertainProficiencies(bg, conn);

        conn.close();
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        Background bg = (Background) obj;
        Connection conn = openConnection();
        
        deleteBackgroundProficiencies(bg, conn);

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
                rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6));
            
            getCertainProficiencies(newBg, conn);
            getUncertainProficiencies(newBg, conn);
            
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
    
    public void addProficiencies(Background bg, Connection conn) throws SQLException {
        addCertainProficiencies(bg, conn);
        addUncertainProficiencies(bg, conn);
    }
    
    public void addCertainProficiencies(Background bg, Connection conn) throws SQLException {
        for (Proficiency prof : bg.getCertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO BackgroundProficiency (bg_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, bg.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, true);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }
    
    public void addUncertainProficiencies(Background bg, Connection conn) throws SQLException {
        for (Proficiency prof : bg.getUncertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO BackgroundProficiency (bg_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, bg.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, false);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }
    
    public void getCertainProficiencies(Background bg, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN BackgroundProficiency ON "
                + "BackgroundProficiency.prof_id = Proficiency.id WHERE "
                + "bg_id = ? AND certain = ?;");
        getProfs.setInt(1, bg.getId());
        getProfs.setBoolean(2, true);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            bg.addCertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
    }
    
    public void getUncertainProficiencies(Background bg, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN BackgroundProficiency ON "
                + "BackgroundProficiency.prof_id = Proficiency.id WHERE "
                + "bg_id = ? AND certain = ?;");
        getProfs.setInt(1, bg.getId());
        getProfs.setBoolean(2, false);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            bg.addUncertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
    }
    
    public void deleteBackgroundProficiencies(Background bg, Connection conn) throws SQLException {
        PreparedStatement deleteProfs = conn.prepareStatement("DELETE FROM "
                + "BackgroundProficiency WHERE bg_id = ?;");
        deleteProfs.setInt(1, bg.getId());
        deleteProfs.executeUpdate();
        deleteProfs.close();
    }
    
    public void updateBackgroundToBackgrounds(Background bg) {
        for (Background oldBg : this.backgrounds) {
            if (oldBg.getId() == bg.getId()) {
                oldBg.setName(bg.getName());
                oldBg.setRandomProfs(bg.getRandomProfs());
                oldBg.setRandomLangs(bg.getRandomLangs());
                oldBg.setExtraProfs(bg.getExtraProfs());
                oldBg.setExtraProfType(bg.getExtraProfType());
                oldBg.setCertainProfs(bg.getCertainProfs());
                oldBg.setUncertainProfs(bg.getUncertainProfs());
                break;
            }
        }
    }
}
