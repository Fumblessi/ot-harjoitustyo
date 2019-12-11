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
                    + "(name, stats) VALUES (?, ?);");
            stmt.setString(1, feat.getName());
            stmt.setString(2, feat.getStats());
            stmt.executeUpdate();
            stmt.close();

            feat.setId(getFeatId(feat, conn));

            this.feats.add(feat);

            addFeatProficiencies(feat, conn);
            
            conn.close();
        }
    }
    
    @Override
    public void update(Object obj) throws SQLException {
        Feat feat = (Feat) obj;

        Connection conn = openConnection();

        updateFeatToFeats(feat);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Feat "
                + "SET name = ?, stats = ? WHERE id = ?;");
        stmt.setString(1, feat.getName());
        stmt.setString(2, feat.getStats());
        stmt.setInt(3, feat.getId());
        stmt.executeUpdate();
        stmt.close();

        deleteFeatProficiencies(feat, conn);
        addFeatProficiencies(feat, conn);

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
            Feat feat = new Feat(rs.getInt(1), rs.getString(2));
            feat.setStats(rs.getString(3));
            getFeatProficiencies(feat, conn);
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
    
    public void addFeatProficiencies(Feat feat, Connection conn) throws SQLException {
        for (Proficiency prof : feat.getFeatProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO FeatProficiency (feat_id, prof_id) VALUES "
                    + "(?, ?);");
            connectProf.setInt(1, feat.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.executeUpdate();
            connectProf.close();
        }
    }
    
    public void getFeatProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN FeatProficiency ON "
                + "FeatProficiency.prof_id = Proficiency.id WHERE "
                + "feat_id = ?;");
        getProfs.setInt(1, feat.getId());
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            feat.addFeatProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3)));
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
                oldFeat.setFeatProfs(feat.getFeatProfs());
                break;
            }
        }
    }
}
