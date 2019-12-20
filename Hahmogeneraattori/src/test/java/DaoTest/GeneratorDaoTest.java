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
import hahmogeneraattori.domain.Background;
import hahmogeneraattori.domain.Feat;
import hahmogeneraattori.domain.Proficiency;
import hahmogeneraattori.domain.Race;
import hahmogeneraattori.domain.Racial;
import hahmogeneraattori.domain.RpgClass;

/**
 *
 * @author sampo
 */
public class GeneratorDaoTest {
    
    Properties properties;
    String testConnectionPath;
    String testUser;
    String testPswd;
    SQLGeneratorDatabaseDao dbDao;
    
    
    public GeneratorDaoTest() {
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
    public void initializeRemovesEverything() throws SQLException {
        Proficiency prof = new Proficiency(-1, "taito", "Skill", "");
        this.dbDao.create(prof);
        
        Race race = new Race(-1, "goljatti");       
        this.dbDao.create(race);
        
        Racial racial = new Racial(-1, "Kääpiön kestävyys", 1, false, 1, 1, 1, "Skill");
        racial.addCertainProf(prof);
        this.dbDao.create(racial);
        
        RpgClass rpg = new RpgClass(-1, "Ritari", 1, 1, 1, "Skill");
        rpg.addUncertainProf(prof);
        this.dbDao.create(rpg);
        
        Background bg = new Background(-1, "Maalari", "");
        this.dbDao.create(bg);
        
        Feat feat = new Feat(-1, "Supervoimat", "STR/DEX", 1, 1, 1, "Skill");
        feat.addCertainProf(prof);
        this.dbDao.create(feat);
        
        this.dbDao.initialize();
        this.dbDao = new SQLGeneratorDatabaseDao(this.testConnectionPath, 
            this.testUser, this.testPswd);
        
        Connection conn = this.dbDao.openConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "RacialProficiency;");
        ResultSet rs1 = stmt1.executeQuery();
        int racialProfFound = 1;
        while (rs1.next()) {
            racialProfFound = rs1.getInt(1);
        }
        
        stmt1.close();
        rs1.close();
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "ClassProficiency;");
        ResultSet rs2 = stmt2.executeQuery();
        int classProfFound = 1;
        while (rs2.next()) {
            classProfFound = rs2.getInt(1);
        }
        
        stmt2.close();
        rs2.close();
        
        PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) FROM "
                + "FeatProficiency;");
        ResultSet rs3 = stmt3.executeQuery();
        int featProfFound = 1;
        while (rs3.next()) {
            featProfFound = rs3.getInt(1);
        }
        
        stmt3.close();
        rs3.close();
        conn.close();
        
        assertTrue(this.dbDao.list(Race.class).isEmpty());
        assertTrue(this.dbDao.list(Proficiency.class).isEmpty());
        assertTrue(this.dbDao.list(Racial.class).isEmpty());
        assertTrue(this.dbDao.list(RpgClass.class).isEmpty());
        assertTrue(this.dbDao.list(Background.class).isEmpty());
        assertTrue(this.dbDao.list(Feat.class).isEmpty());
        assertEquals(0, racialProfFound);
        assertEquals(0, classProfFound);
        assertEquals(0, featProfFound);
    }
}
