/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLRaceDatabaseDao;
import hahmogeneraattori.domain.Race;
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
public class RaceDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public RaceDaoTest() {
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
    public void raceCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Race race = new Race(-1, "goljatti");
        this.dbDao.create(race);
        int id = race.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Race WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int raceFound = 0;
        while (rs.next()) {
            raceFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, raceFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void raceWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Race race = new Race(-1, "goljatti");
        this.dbDao.create(race);
        Race raceWithSameName = new Race(-1, "goljatti");
        this.dbDao.create(raceWithSameName);
        int id = raceWithSameName.getId();
        conn.close();
        
        assertEquals(-1, id);
        this.dbDao.initialize();
    }
    
    @Test
    public void raceNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Race race = new Race(-1, "goljatti");
        this.dbDao.create(race);

        Race newRace = new Race(race.getId(), "corgi");
        this.dbDao.update(newRace);
        int id = race.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Race WHERE id = ?;");
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
    public void raceCanBeDeleted() throws SQLException {
        Connection conn = dbDao.openConnection();
        
        Race race = new Race(-1, "goljatti");
        this.dbDao.create(race);
        int id = race.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Race WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int raceFound = 0;
        while (rs1.next()) {
            raceFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(race);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Race WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int raceNotFound = 1;
        while (rs2.next()) {
            raceNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, raceFound);
        assertEquals(0, raceNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllRacesFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Race race = new Race(-1, "goljatti");
        this.dbDao.create(race);
        Race anotherRace = new Race(-1, "daavid");
        this.dbDao.create(anotherRace);
        conn.close();
        
        SQLRaceDatabaseDao raceDao = new SQLRaceDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Race> races = raceDao.list(Race.class);       
        
        assertTrue(races.contains(race));
        assertTrue(races.contains(anotherRace));
        this.dbDao.initialize();
    }
}
