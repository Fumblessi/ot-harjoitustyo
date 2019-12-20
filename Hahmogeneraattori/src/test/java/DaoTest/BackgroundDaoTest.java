/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLBackgroundDatabaseDao;
import hahmogeneraattori.domain.Background;
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
public class BackgroundDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public BackgroundDaoTest() {
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
    public void backgroundCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Background bg = new Background(-1, "Maalari", "");
        this.dbDao.create(bg);
        int id = bg.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Background WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int bgFound = 0;
        while (rs.next()) {
            bgFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, bgFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void backgroundWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Background bg = new Background(-1, "Maalari", "Maalaustaito");
        this.dbDao.create(bg);
        Background bgWithSameName = new Background(-1, "Maalari", "Taalausmaito");
        this.dbDao.create(bgWithSameName);
        int id = bgWithSameName.getId();
        conn.close();
        
        assertEquals(-1, id);
        this.dbDao.initialize();
    }
    
    @Test
    public void backgroundNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Background bg = new Background(-1, "Maalari", "Maalaustaito");
        this.dbDao.create(bg);

        Background newBg = new Background(bg.getId(), "Koirakouluttaja", "Maalaustaito");
        this.dbDao.update(newBg);
        int id = bg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Background WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String name = "";
        while (rs.next()) {
            name = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Koirakouluttaja", name);
        this.dbDao.initialize();
    }
    
    @Test
    public void backgroundFeatureCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        Background bg = new Background(-1, "Maalari", "Maalaustaito");
        this.dbDao.create(bg);

        Background newBg = new Background(bg.getId(), "Maalari", "Taalausmaito");
        this.dbDao.update(newBg);
        int id = bg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT feature FROM "
                + "Background WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String feature = "";
        while (rs.next()) {
            feature = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Taalausmaito", feature);
        this.dbDao.initialize();
    }
    
    @Test
    public void backgroundCanBeDeleted() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Background bg = new Background(-1, "Maalari", "Taidemaalaus");
        this.dbDao.create(bg);
        int id = bg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Background WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int bgFound = 0;
        while (rs1.next()) {
            bgFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(bg);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Background WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int bgNotFound = 1;
        while (rs2.next()) {
            bgNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, bgFound);
        assertEquals(0, bgNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllBackgroundsFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        Background bg = new Background(-1, "Maalari", "");
        this.dbDao.create(bg);
        Background anotherBg = new Background(-1, "Soutaja", "");
        this.dbDao.create(anotherBg);
        conn.close();
        
        SQLBackgroundDatabaseDao bgDao = new SQLBackgroundDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<Background> bgs = bgDao.list(Background.class);       
        
        assertTrue(bgs.contains(bg));
        assertTrue(bgs.contains(anotherBg));
        this.dbDao.initialize();
    }
}