/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Feat;
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
            
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Feat "
                    + "(name, stats, randomProfs, randomLangs, extraProfs, "
                    + "extraProfType) VALUES (?, ?, ?, ?, ?, ?);");
            stmt.setString(1, feat.getName());
            stmt.setString(2, feat.getStats());
            stmt.setInt(3, feat.getRandomProfs());
            stmt.setInt(4, feat.getRandomLangs());
            stmt.setInt(5, feat.getExtraProfs());
            stmt.setString(6, feat.getExtraProfType());
            stmt.executeUpdate();
            stmt.close();

            feat.setId(getFeatId(feat, conn));

            this.feats.add(feat);

            addCertainProficiencies(feat, conn);
            addUncertainProficiencies(feat, conn);
            
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        Feat feat = (Feat) obj;

        Connection conn = openConnection();

        updateFeatToFeats(feat);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Feat "
                + "SET name = ?, stats = ?, randomProfs = ?, randomLangs = ?, "
                + "extraProfs = ?, extraProfType = ? WHERE id = ?;");
        stmt.setString(1, feat.getName());
        stmt.setString(2, feat.getStats());
        stmt.setInt(3, feat.getRandomProfs());
        stmt.setInt(4, feat.getRandomLangs());
        stmt.setInt(5, feat.getExtraProfs());
        stmt.setString(6, feat.getExtraProfType());
        stmt.setInt(7, feat.getId());
        stmt.executeUpdate();
        stmt.close();

        deleteFeatProficiencies(feat, conn);
        addCertainProficiencies(feat, conn);
        addUncertainProficiencies(feat, conn);

        conn.close();
    }
    
    @Override
    public void delete(Object obj) throws SQLException {
        Feat feat = (Feat) obj;
        Connection conn = openConnection();

        deleteFeatProficiencies(feat, conn);

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Feat "
                + "WHERE id = ?;");
        stmt.setInt(1, feat.getId());

        stmt.executeUpdate();
        stmt.close();
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
            Feat feat = new Feat(rs.getInt(1), rs.getString(2), rs.getString(3), 
                rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7));
            
            getCertainProficiencies(feat, conn);
            getUncertainProficiencies(feat, conn);
            
            this.feats.add(feat);
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
    
    public int getFeatId(Feat feat, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Feat WHERE name = ?;");
        stmt.setString(1, feat.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        return id;
    }
    
    public void addCertainProficiencies(Feat feat, Connection conn) throws SQLException {
        for (Proficiency prof : feat.getCertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO FeatProficiency (feat_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, feat.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, true);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }
    
    public void addUncertainProficiencies(Feat feat, Connection conn) throws SQLException {
        for (Proficiency prof : feat.getUncertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO FeatProficiency (feat_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, feat.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, false);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }
    
    public void getCertainProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN FeatProficiency ON "
                + "FeatProficiency.prof_id = Proficiency.id WHERE "
                + "feat_id = ? AND certain = ?;");
        getProfs.setInt(1, feat.getId());
        getProfs.setBoolean(2, true);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            feat.addCertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
    }
    
    public void getUncertainProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN FeatProficiency ON "
                + "FeatProficiency.prof_id = Proficiency.id WHERE "
                + "feat_id = ? AND certain = ?;");
        getProfs.setInt(1, feat.getId());
        getProfs.setBoolean(2, false);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            feat.addUncertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
    }
    
    public void deleteFeatProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement deleteProfs = conn.prepareStatement("DELETE FROM "
                + "FeatProficiency WHERE feat_id = ?;");
        deleteProfs.setInt(1, feat.getId());
        deleteProfs.executeUpdate();
        deleteProfs.close();
    }
    
    public void updateFeatToFeats(Feat feat) {
        for (Feat oldFeat : this.feats) {
            if (oldFeat.getId() == feat.getId()) {
                oldFeat.setName(feat.getName());
                oldFeat.setStats(feat.getStats());
                oldFeat.setRandomProfs(feat.getRandomProfs());
                oldFeat.setRandomLangs(feat.getRandomLangs());
                oldFeat.setExtraProfs(feat.getExtraProfs());
                oldFeat.setExtraProfType(feat.getExtraProfType());
                oldFeat.setCertainProfs(feat.getCertainProfs());
                oldFeat.setUncertainProfs(feat.getUncertainProfs());
                break;
            }
        }
    }
}
