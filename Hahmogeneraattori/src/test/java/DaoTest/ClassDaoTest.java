/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DaoTest;

import hahmogeneraattori.dao.SQLGeneratorDatabaseDao;
import hahmogeneraattori.dao.SQLClassDatabaseDao;
import hahmogeneraattori.domain.Proficiency;
import hahmogeneraattori.domain.RpgClass;
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
public class ClassDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    public ClassDaoTest() {
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
    public void classCanBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);
        int id = rpg.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Class WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int classFound = 0;
        while (rs.next()) {
            classFound = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, classFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void classWithSameNameCanNotBeCreated() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);
        RpgClass rpgWithSameName = new RpgClass(-1, "Ritari", 0, 0, 0, "Language");
        this.dbDao.create(rpgWithSameName);
        int id = rpg.getId();

        PreparedStatement stmt = conn.prepareStatement("SELECT randomProfs, "
                + "randomLangs, extraProfs, extraProfType FROM Class WHERE id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int randomProfs = -10;
        int randomLangs = -10;
        int extraProfs = -10;
        String extraProfType = "Fudge";
        while (rs.next()) {
            randomProfs = rs.getInt(1);
            randomLangs = rs.getInt(2);
            extraProfs = rs.getInt(3);
            extraProfType = rs.getString(4);
        }
        stmt.close();
        rs.close();
        conn.close();
        
        assertEquals(1, randomProfs);
        assertEquals(1, randomLangs);
        assertEquals(1, extraProfs);
        assertEquals("Skill", extraProfType);
        this.dbDao.initialize();
    }
    
    @Test
    public void classNameCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);

        RpgClass newRpg = new RpgClass(rpg.getId(), "Soturi", 1, 1, 1, "Skill");
        this.dbDao.update(newRpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT name FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String name = "";
        while (rs.next()) {
            name = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Soturi", name);
        this.dbDao.initialize();
    }
    
    @Test
    public void classRandomProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);

        RpgClass newRpg = new RpgClass(rpg.getId(), "Ritari", 9, 1, 1, "Skill");
        this.dbDao.update(newRpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomProfs FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndProfs = 0;
        while (rs.next()) {
            rndProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(9, rndProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void classRandomLangsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);

        RpgClass newRpg = new RpgClass(rpg.getId(), "Ritari", 1, 17, 1, "Skill");
        this.dbDao.update(newRpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT randomLangs FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int rndLangs = 0;
        while (rs.next()) {
            rndLangs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(17, rndLangs);
        this.dbDao.initialize();
    }
    
    @Test
    public void classExtraProfsCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);

        RpgClass newRpg = new RpgClass(rpg.getId(), "Ritari", 1, 1, 3, "Skill");
        this.dbDao.update(newRpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfs FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        int xtraProfs = 0;
        while (rs.next()) {
            xtraProfs = rs.getInt(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals(3, xtraProfs);
        this.dbDao.initialize();
    }
    
    @Test
    public void classExtraProfTypeCanBeModified() throws SQLException {
        Connection conn = this.dbDao.openConnection();

        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);

        RpgClass newRpg = new RpgClass(rpg.getId(), "Ritari", 1, 1, 1, "Language");
        this.dbDao.update(newRpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT extraProfType FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs = stmt1.executeQuery();
        String type = "";
        while (rs.next()) {
            type = rs.getString(1); 
        }
        stmt1.close();
        rs.close();
        conn.close();
        
        assertEquals("Language", type);
        this.dbDao.initialize();
    }
    
    @Test
    public void classCanBeDeleted() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);
        int id = rpg.getId();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Class WHERE id = ?;");
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();
        int classFound = 0;
        while (rs1.next()) {
            classFound = rs1.getInt(1);
        }
        
        this.dbDao.delete(rpg);
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "Class WHERE id = ?;");
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        int classNotFound = 1;
        while (rs2.next()) {
            classNotFound = rs2.getInt(1);
        }
        stmt1.close();
        rs1.close();
        stmt2.close();
        rs2.close();
        conn.close();
        
        assertEquals(1, classFound);
        assertEquals(0, classNotFound);
        this.dbDao.initialize();
    }
    
    @Test
    public void initializeGetsAllClassesFromDb() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        this.dbDao.create(rpg);
        RpgClass anotherRpg = new RpgClass(-1, "Soturi", 2, 2, 2, "Dill");
        this.dbDao.create(anotherRpg);
        conn.close();
        
        SQLClassDatabaseDao classDao = new SQLClassDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<RpgClass> classes = classDao.list(RpgClass.class);       
        
        assertTrue(classes.contains(rpg));
        assertTrue(classes.contains(anotherRpg));
        this.dbDao.initialize();
    }
    
    @Test
    public void classCertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        rpg.addCertainProf(prof1);
        rpg.addCertainProf(prof2);
        rpg.addCertainProf(prof3);
        this.dbDao.create(rpg);
        conn.close();
        
        SQLClassDatabaseDao classDao = new SQLClassDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<RpgClass> classes = classDao.list(RpgClass.class);
        RpgClass savedRpg = classes.get(0);
        
        assertTrue(savedRpg.getCertainProfs().contains(prof1));
        assertTrue(savedRpg.getCertainProfs().contains(prof2));
        assertTrue(savedRpg.getCertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
    
    @Test
    public void classUncertainProficienciesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        Proficiency prof1 = new Proficiency(-1, "taito", "Skill", "");
        Proficiency prof2 = new Proficiency(-1, "kieli", "Language", "");
        Proficiency prof3 = new Proficiency(-1, "save", "Save", "");
        this.dbDao.create(prof1);
        this.dbDao.create(prof2);
        this.dbDao.create(prof3);
        rpg.addUncertainProf(prof1);
        rpg.addUncertainProf(prof2);
        rpg.addUncertainProf(prof3);
        this.dbDao.create(rpg);
        conn.close();
        
        SQLClassDatabaseDao classDao = new SQLClassDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<RpgClass> classes = classDao.list(RpgClass.class);
        RpgClass savedRpg = classes.get(0);
        
        assertTrue(savedRpg.getUncertainProfs().contains(prof1));
        assertTrue(savedRpg.getUncertainProfs().contains(prof2));
        assertTrue(savedRpg.getUncertainProfs().contains(prof3));
        this.dbDao.initialize();
    }
    
    @Test
    public void classSubclassesAreSaved() throws SQLException {
        Connection conn = this.dbDao.openConnection();
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        rpg.addSubclass("Taisteluritari");
        rpg.addSubclass("Kokkiritari");
        rpg.addSubclass("Musta ritari");
        this.dbDao.create(rpg);
        conn.close();
        
        SQLClassDatabaseDao classDao = new SQLClassDatabaseDao(
                this.testConnectionPath, this.testUser, this.testPswd);
        
        List<RpgClass> classes = classDao.list(RpgClass.class);
        RpgClass savedRpg = classes.get(0);
        
        assertTrue(savedRpg.getSubclasses().contains("Taisteluritari"));
        assertTrue(savedRpg.getSubclasses().contains("Kokkiritari"));
        assertTrue(savedRpg.getSubclasses().contains("Musta ritari"));
        this.dbDao.initialize();
    }
}