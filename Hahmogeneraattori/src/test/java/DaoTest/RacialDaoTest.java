/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLRacialDatabaseDao;
import hahmogeneraattori.domain.Racial;
import hahmogeneraattori.domain.Proficiency;
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
public class RacialDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public RacialDaoTest() {
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
    public void racialCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Kääpiön kestävyys", 1, false, 1, 1, 1, "Skill");
        this.dbDao.create(racial);
        int id = racial.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Racial WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int racialFound = 0;
        while (rs.next()) {
            racialFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, racialFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);
        Racial racialWithSameName = new Racial(-1, "Puolituisen rohkeus", 2, true, 
                2, 2, 2, "Legendary");
        this.dbDao.create(racialWithSameName);
        int id = racial.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT stats, feat, "
               + "randomProfs, randomLangs, extraProfs, extraProfType "
                + "FROM Racial WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int stats = -10;
        boolean feat = true;
        int randomProfs = -10;
        int randomLangs = -10;
        int extraProfs = -10;
        String extraProfType = "Fudge";
        while (rs.next()) {
            stats = rs.getInt(1);
            feat = rs.getBoolean(2);
            randomProfs = rs.getInt(3);
            randomLangs = rs.getInt(4);
            extraProfs = rs.getInt(5);
            extraProfType = rs.getString(6);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(0, stats);
        assertFalse(feat);
        assertEquals(0, randomProfs);
        assertEquals(0, randomLangs);
        assertEquals(0, extraProfs);
        assertEquals("", extraProfType);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen pelkuruus", 
                0, false, 0, 0, 0, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String name = "";
        while (rs.next()) {
            name = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Puolituisen pelkuruus", name);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialStatsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 12, 
                false, 0, 0, 0, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT stats FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int stats = 0;
        while (rs.next()) {
            stats = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(12, stats);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialFeatCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 0, 
                true, 0, 0, 0, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT feat FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        boolean feat = false;
        while (rs.next()) {
            feat = rs.getBoolean(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertTrue(feat);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialRandomProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 0, 
                false, 12, 0, 0, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomProfs FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndProfs = 0;
        while (rs.next()) {
            rndProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(12, rndProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialRandomLangsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 0, 
                false, 0, 13, 0, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomLangs FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndLangs = 0;
        while (rs.next()) {
            rndLangs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(13, rndLangs);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialExtraProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 0, 
                false, 0, 0, 14, "");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfs FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int xtraProfs = 0;
        while (rs.next()) {
            xtraProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(14, xtraProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialExtraProfTypeCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);

        Racial newRacial = new Racial(racial.getId(), "Puolituisen rohkeus", 
                0, false, 0, 0, 0, "Paukkerointi");
        this.dbDao.update(newRacial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfType FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String type = "";
        while (rs.next()) {
            type = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Paukkerointi", type);
        this.dbDao.initialize();
    }
    
    @Test
    public void racialCanBeDeleted() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);
        int id = racial.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Racial WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int racialFound = 0;
        while (rs1.next()) {
            racialFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(racial);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Racial WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int racialNotFound = 1;
        while (rs2.next()) {
            racialNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, racialFound);
        assertEquals(0, racialNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllRacialsFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Puolituisen rohkeus", 0, false, 0, 0, 0, "");
        this.dbDao.create(racial);
        Racial anotherRacial = new Racial(-1, "Kääpiön kestävyys", 1, false, 1, 
                1, 1, "Skill");
        this.dbDao.create(anotherRacial);
        conn.close();
        
        SQLRacialDatabaseDao racialDao = new SQLRacialDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Racial> racials = racialDao.list(Racial.class);       
        
        assertTrue(racials.contains(racial));
        assertTrue(racials.contains(anotherRacial));
        this.dbDao.initialize();
    }
    
    @Test
    public void racialCertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Kääpiön kestävyys", 1, false, 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        racial.addCertainProf(prof1);
        racial.addCertainProf(prof2);
        racial.addCertainProf(prof3);
        this.dbDao.create(racial);
        conn.close();
        
        SQLRacialDatabaseDao racialDao = new SQLRacialDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Racial> racials = racialDao.list(Racial.class);
        Racial savedRacial = racials.get(0);
        
        assertTrue(savedRacial.getCertainProfs().contains(prof1));
        assertTrue(savedRacial.getCertainProfs().contains(prof2));
        assertTrue(savedRacial.getCertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
    
    @Test
    public void racialUncertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Racial racial = new Racial(-1, "Kääpiön kestävyys", 1, false, 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        racial.addUncertainProf(prof1);
        racial.addUncertainProf(prof2);
        racial.addUncertainProf(prof3);
        this.dbDao.create(racial);
        conn.close();
        
        SQLRacialDatabaseDao racialDao = new SQLRacialDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Racial> racials = racialDao.list(Racial.class);
        Racial savedRacial = racials.get(0);
        
        assertTrue(savedRacial.getUncertainProfs().contains(prof1));
        assertTrue(savedRacial.getUncertainProfs().contains(prof2));
        assertTrue(savedRacial.getUncertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
}
