/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.sql.*;
import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.domain.Proficiency;

/**
 *
 * @author sampo
 */
public class GeneratorDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    
    
    public GeneratorDaoTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.properties = new Properties();
        this.properties.load(new FileInputStream("fakeConfig.properties"));
        this.testConnectionPath = this.properties.getProperty("spring.datasource.url");
        this.testUser = this.properties.getProperty("spring.datasource.username");
        this.testPswd = properties.getProperty("spring.datasource.password");
    }
    
    @Test
    public void proficiencyCanBeCreated() throws SQLException {
        SQLGeneratorDatabaseDao dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);

        Connection conn = dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill");
        dbDao.create(prof);
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
        conn.close();
        
        assertEquals(1, profFound);
        dbDao.initialize();
    }
    
    @Test
    public void proficiencyNameCanBeModified() throws SQLException {
        SQLGeneratorDatabaseDao dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);
        
        Connection conn = dbDao.openConnection();

        Proficiency prof = new Proficiency(-1, "taito", "Skill");
        dbDao.create(prof);

        Proficiency newProf = new Proficiency(prof.getId(), "corgi", prof.getType());
        dbDao.updateProf(newProf, conn);
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
        conn.close();
        
        assertEquals("corgi", name);
        dbDao.initialize();
    }
    
    @Test
    public void proficiencyTypeCanBeModified() throws SQLException {
        SQLGeneratorDatabaseDao dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);
        
        Connection conn = dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill");
        dbDao.create(prof);
        
        Proficiency newProf = new Proficiency(prof.getId(), prof.getName(), "Tool");
        dbDao.update(newProf);
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
        conn.close();
        
        assertEquals("Tool", type);
        dbDao.initialize();
    }
    
    @Test
    public void proficiencyCanBeDeleted() throws SQLException {
        SQLGeneratorDatabaseDao dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);
        
        Connection conn = dbDao.openConnection();
        
        Proficiency prof = new Proficiency(-1, "taito", "Skill");
        dbDao.create(prof);
        int id = prof.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Proficiency WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int profFound = 0;
        while (rs1.next()) {
            profFound = rs1.getInt(1);
        }
        
        dbDao.delete(prof);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Proficiency WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int profNotFound = 1;
        while (rs2.next()) {
            profNotFound = rs2.getInt(1);
        }
        stmt1.close();
        stmt2.close();
        conn.close();
        
        assertEquals(1, profFound);
        assertEquals(0, profNotFound);
        dbDao.initialize();
    }
}
