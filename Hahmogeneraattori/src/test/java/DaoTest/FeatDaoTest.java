/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLFeatDatabaseDao;
import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.domain.Proficiency;
import hahmogeneraattori.domain.Feat;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sampo
 */
public class FeatDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public FeatDaoTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.properties = new Properties();
        this.properties.load(new FileInputStream("fakeConfig.properties"));
        this.testConnectionPath = this.properties.getProperty("spring.datasource.url");
        this.testUser = this.properties.getProperty("spring.datasource.username");
        this.testPswd = properties.getProperty("spring.datasource.password");
        this.dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);
    }
    
    @Test
    public void featCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);
        int id = feat.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Feat WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int featFound = 0;
        while (rs.next()) {
            featFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, featFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void featWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);
        Feat featWithSameName = new Feat(-1, "Supervoimat", "WIS", 2, 2, 2, "Tool");
        this.dbDao.create(featWithSameName);
        int id = feat.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT stats, randomProfs, "
                + "randomLangs, extraProfs, extraProfType FROM Feat WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        String stats = "CON";
        int randomProfs = -10;
        int randomLangs = -10;
        int extraProfs = -10;
        String extraProfType = "Fudge";
        while (rs.next()) {
            stats = rs.getString(1);
            randomProfs = rs.getInt(2);
            randomLangs = rs.getInt(3);
            extraProfs = rs.getInt(4);
            extraProfType = rs.getString(5);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals("STR/DEX", stats);
        assertEquals(1, randomProfs);
        assertEquals(1, randomLangs);
        assertEquals(1, extraProfs);
        assertEquals("Skill", extraProfType);
        this.dbDao.initialize();
    }
    
    @Test
    public void featNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Hypervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String name = "";
        while (rs.next()) {
            name = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Hypervoimat", name);
        this.dbDao.initialize();
    }
    
    @Test
    public void featStatsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Supervoimat", "WIS/CHA", 1, 1, 1, "Skill");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT stats FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String stats = "";
        while (rs.next()) {
            stats = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("WIS/CHA", stats);
        this.dbDao.initialize();
    }
    
    @Test
    public void featRandomProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Supervoimat", "STR/DEX", 7, 1, 1, "Skill");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomProfs FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndProfs = 0;
        while (rs.next()) {
            rndProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(7, rndProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void featRandomLangsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Supervoimat", "STR/DEX", 1, 11, 1, "Skill");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomLangs FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndLangs = 0;
        while (rs.next()) {
            rndLangs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(11, rndLangs);
        this.dbDao.initialize();
    }
    
    @Test
    public void featExtraProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Supervoimat", "STR/DEX", 1, 1, 8, "Skill");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfs FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int xtraProfs = 0;
        while (rs.next()) {
            xtraProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(8, xtraProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void featExtraProfTypeCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);

        Feat newFeat = new Feat(feat.getId(), "Supervoimat", "STR/DEX", 1, 1, 1, "Tool");
        this.dbDao.update(newFeat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfType FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String type = "";
        while (rs.next()) {
            type = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Tool", type);
        this.dbDao.initialize();
    }
    
    @Test
    public void featCanBeDeleted() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);
        int id = feat.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Feat WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int featFound = 0;
        while (rs1.next()) {
            featFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(feat);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Feat WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int featNotFound = 1;
        while (rs2.next()) {
            featNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, featFound);
        assertEquals(0, featNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllFeatsFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        this.dbDao.create(feat);
        Feat anotherFeat = new Feat(-1, "Hypervoimat", "CON", 2, 2, 2, "Tool");
        this.dbDao.create(anotherFeat);
        conn.close();
        
        SQLFeatDatabaseDao featDao = new SQLFeatDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Feat> feats = featDao.list(Feat.class);       
        
        assertTrue(feats.contains(feat));
        assertTrue(feats.contains(anotherFeat));
        this.dbDao.initialize();
    }
    
    @Test
    public void featCertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        feat.addCertainProf(prof1);
        feat.addCertainProf(prof2);
        feat.addCertainProf(prof3);
        this.dbDao.create(feat);
        conn.close();
        
        SQLFeatDatabaseDao featDao = new SQLFeatDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Feat> feats = featDao.list(Feat.class);
        Feat savedFeat = feats.get(0);
        
        assertTrue(savedFeat.getCertainProfs().contains(prof1));
        assertTrue(savedFeat.getCertainProfs().contains(prof2));
        assertTrue(savedFeat.getCertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
    
    @Test
    public void classUncertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        feat.addUncertainProf(prof1);
        feat.addUncertainProf(prof2);
        feat.addUncertainProf(prof3);
        this.dbDao.create(feat);
        conn.close();
        
        SQLFeatDatabaseDao featDao = new SQLFeatDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Feat> classes = featDao.list(Feat.class);
        Feat savedFeat = classes.get(0);
        
        assertTrue(savedFeat.getUncertainProfs().contains(prof1));
        assertTrue(savedFeat.getUncertainProfs().contains(prof2));
        assertTrue(savedFeat.getUncertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
}