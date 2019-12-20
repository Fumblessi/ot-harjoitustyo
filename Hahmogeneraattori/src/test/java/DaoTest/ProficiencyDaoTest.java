/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLProficiencyDatabaseDao;
import hahmogeneraattori.domain.Proficiency;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sampo
 */
public class ProficiencyDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public ProficiencyDaoTest() {
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
    public void proficiencyCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        int id = prof.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Proficiency WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int profFound = 0;
        while (rs.next()) {
            profFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, profFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void proficiencyWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        Proficiency profWithSameName = new Proficiency(-1, "taito", "Language", 
        "Legendary");
        this.dbDao.create(profWithSameName);
        int id = prof.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT type, subtype FROM "
                + "Proficiency WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        String type = "";
        String subtype = "";
        while (rs.next()) {
            type = rs.getString(1);
            subtype = rs.getString(2);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals("Skill", type);
        assertEquals("", subtype);
        this.dbDao.initialize();
    }
    
    @Test
    public void proficiencyNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);

        Proficiency newProf = new Proficiency(prof.getId(), "corgi", prof.getType(), 
        prof.getSubtype());
        this.dbDao.update(newProf);
        int id = prof.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Proficiency WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String name = "";
        while (rs.next()) {
            name = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("corgi", name);
        this.dbDao.initialize();
    }
    
    @Test
    public void proficiencyTypeCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        
        Proficiency newProf = new Proficiency(prof.getId(), prof.getName(), "Tool", 
        prof.getSubtype());
        this.dbDao.update(newProf);
        int id = prof.getId();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT type FROM "
                + "Proficiency WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        String type = "";
        while (rs.next()) {
            type = rs.getString(1); 
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals("Tool", type);
        this.dbDao.initialize();
    }
    
    @Test
    public void profSubtypeCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        
        Proficiency newProf = new Proficiency(prof.getId(), prof.getName(), 
                prof.getType(), "Mestaritaito");
        this.dbDao.update(newProf);
        int id = prof.getId();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT subtype FROM "
                + "Proficiency WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        String subtype = "";
        while (rs.next()) {
            subtype = rs.getString(1); 
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals("Mestaritaito", subtype);
        this.dbDao.initialize();
    }
    
    @Test
    public void proficiencyCanBeDeleted() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        int id = prof.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Proficiency WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int profFound = 0;
        while (rs1.next()) {
            profFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(prof);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Proficiency WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int profNotFound = 1;
        while (rs2.next()) {
            profNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, profFound);
        assertEquals(0, profNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllProficienciesFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        Proficiency anotherProf = new Proficiency(-1, "Suomi", "Language", 
        "Legendary");
        this.dbDao.create(anotherProf);
        conn.close();
        
        SQLProficiencyDatabaseDao profDao = new SQLProficiencyDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Proficiency> profs = profDao.list(Proficiency.class);       
        
        assertTrue(profs.contains(prof));
        assertTrue(profs.contains(anotherProf));
        this.dbDao.initialize();
    }
}
