/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.*;
import java.util.List;
import java.sql.*;

/**
 * Luokkaan kuuluu koko tietokantaan liittyvä yleistoiminnallisuus, ja se ohjaa
 * tiettyä tietokantataulua vastaavat tietokantakyselyt taulua vastaavalle
 * luokalle
 *
 * @author sampo
 */
public class SQLGeneratorDatabaseDao implements GeneratorDatabaseDao {

    private SQLProficiencyDatabaseDao profDao;
    private SQLRacialDatabaseDao racialDao;
    private SQLClassDatabaseDao classDao;
    private SQLBackgroundDatabaseDao bgDao;
    private SQLFeatDatabaseDao featDao;
    private SQLRaceDatabaseDao raceDao;
    private String connection;
    private String user;
    private String password;

    /**
     * Luokan konstruktorissa alustetaan eri tietokantatauluja hallinnoivat
     * luokat, ja luokalle annetaan tietokantayhteyden tiedot
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao
     * @see hahmogeneraattori.dao.SQLClassDatabaseDao
     * @see hahmogeneraattori.dao.SQLBackgroundDatabaseDao
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao
     *
     * @param connPath tietokantatiedosto
     * @param user käyttäjä
     * @param pswd salasana
     *
     * @throws SQLException
     */
    public SQLGeneratorDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.profDao = new SQLProficiencyDatabaseDao(connPath, user, pswd);
        this.racialDao = new SQLRacialDatabaseDao(connPath, user, pswd);
        this.classDao = new SQLClassDatabaseDao(connPath, user, pswd);
        this.bgDao = new SQLBackgroundDatabaseDao(connPath, user, pswd);
        this.featDao = new SQLFeatDatabaseDao(connPath, user, pswd);
        this.raceDao = new SQLRaceDatabaseDao(connPath, user, pswd);
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
    }

    /**
     * Metodi lisää halutun objektin tietokantaan, ohjaamalla sen objektin
     * tyypin mukaan oikean luokan lisättäväksi
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#create(Object)
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#create(Object)
     * @see hahmogeneraattori.dao.SQLClassDatabaseDao#create(Object)
     * @see hahmogeneraattori.dao.SQLBackgroundDatabaseDao#create(Object)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#create(Object)
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#create(Object)
     *
     * @param obj lisättävä objekti
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        if (obj.getClass() == Proficiency.class) {
            this.profDao.create(obj);
        } else if (obj.getClass() == Racial.class) {
            this.racialDao.create(obj);
        } else if (obj.getClass() == RpgClass.class) {
            this.classDao.create(obj);
        } else if (obj.getClass() == Background.class) {
            this.bgDao.create(obj);
        } else if (obj.getClass() == Feat.class) {
            this.featDao.create(obj);
        } else if (obj.getClass() == Race.class) {
            this.raceDao.create(obj);
        }
    }

    /**
     * Metodi päivittää halutun objektin tietokantaan, ohjaamalla sen objektin
     * tyypin mukaan oikean luokan päivitettäväksi
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#update(Object)
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#update(Object)
     * @see hahmogeneraattori.dao.SQLClassDatabaseDao#update(Object)
     * @see hahmogeneraattori.dao.SQLBackgroundDatabaseDao#update(Object)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#update(Object)
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#update(Object)
     *
     * @param obj päivitettävä objekti
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        if (obj.getClass() == Proficiency.class) {
            this.profDao.update(obj);
        } else if (obj.getClass() == Racial.class) {
            this.racialDao.update(obj);
        } else if (obj.getClass() == RpgClass.class) {
            this.classDao.update(obj);
        } else if (obj.getClass() == Background.class) {
            this.bgDao.update(obj);
        } else if (obj.getClass() == Feat.class) {
            this.featDao.update(obj);
        } else if (obj.getClass() == Race.class) {
            this.raceDao.update(obj);
        }
    }

    /**
     * Metodi poistaa halutun objektin tietokannasta, ohjaamalla sen objektin
     * tyypin mukaan oikean luokan poistettavaksi
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#delete(Object)
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#delete(Object)
     * @see hahmogeneraattori.dao.SQLClassDatabaseDao#delete(Object)
     * @see hahmogeneraattori.dao.SQLBackgroundDatabaseDao#delete(Object)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#delete(Object)
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#delete(Object)
     *
     * @param obj poistettava objekti
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        if (obj.getClass() == Proficiency.class) {
            this.profDao.delete(obj);
        } else if (obj.getClass() == Racial.class) {
            this.racialDao.delete(obj);
        } else if (obj.getClass() == RpgClass.class) {
            this.classDao.delete(obj);
        } else if (obj.getClass() == Background.class) {
            this.bgDao.delete(obj);
        } else if (obj.getClass() == Feat.class) {
            this.featDao.delete(obj);
        } else if (obj.getClass() == Race.class) {
            this.raceDao.delete(obj);
        }
    }

    /**
     * Metodi antaa listana syötettyä luokkaa vastaavan tietokantataulun
     * sisällön, ohjaamalla sen oikean luokan listattavaksi
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#list(Class)
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#list(Class)
     * @see hahmogeneraattori.dao.SQLClassDatabaseDao#list(Class)
     * @see hahmogeneraattori.dao.SQLBackgroundDatabaseDao#list(Class)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#list(Class)
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#list(Class)
     *
     * @param c luokka, jota vastaava tietokantataulu listataan
     *
     * @return lista taulun sisällöstä
     */
    @Override
    public List list(Class c) {
        if (c == Proficiency.class) {
            return this.profDao.list(c);
        } else if (c == Racial.class) {
            return this.racialDao.list(c);
        } else if (c == RpgClass.class) {
            return this.classDao.list(c);
        } else if (c == Background.class) {
            return this.bgDao.list(c);
        } else if (c == Feat.class) {
            return this.featDao.list(c);
        } else if (c == Race.class) {
            return this.raceDao.list(c);
        }
        return null;
    }

    /**
     * Metodi avaa tietokantayhteyden
     *
     * @return tietokantayhteys
     *
     * @throws SQLException
     */
    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }

    /**
     * Metodi resettaa koko tietokannan, eli poistaa jo mahdollisesti olemassa
     * olevan, ja luo uudet, tyhjät tietokantataulut
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#openConnection()
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#dropTables(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createTables(Connection)
     *
     * @throws SQLException
     */
    @Override
    public void initialize() throws SQLException {
        Connection conn = openConnection();
        dropTables(conn);
        createTables(conn);
        conn.close();
    }

    /**
     * Metodi poistaa tietokantataulut, jos ne ovat olemassa
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void dropTables(Connection conn) throws SQLException {
        conn.prepareStatement("DROP TABLE RacialProficiency IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE BackgroundProficiency IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE ClassProficiency IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE FeatProficiency IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Proficiency IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Racial IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Background IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE SubClass IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Class IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Feat IF EXISTS").executeUpdate();
        conn.prepareStatement("DROP TABLE Race IF EXISTS").executeUpdate();
    }

    /**
     * Metodi luo uudet tietokantataulut
     *
     * @param conn tietokantayhteys
     *
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createProfTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createBackgroundTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createClassTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createSubclassTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createClassProfTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createFeatTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createFeatProfTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createRacialTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createRacialProfTable(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createRaceTable(Connection)
     *
     * @throws SQLException
     */
    public void createTables(Connection conn) throws SQLException {
        createProfTable(conn);
        createClassTable(conn);
        createSubclassTable(conn);
        createClassProfTable(conn);
        createFeatTable(conn);
        createFeatProfTable(conn);
        createRacialTable(conn);
        createRacialProfTable(conn);
        createRaceTable(conn);
        createBackgroundTable(conn);
    }

    /**
     * Luodaan tietokantataulu 'Proficiency'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Proficiency(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "type VARCHAR(255), subtype VARCHAR(255), PRIMARY KEY (id), "
                + "UNIQUE KEY (id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Background'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createBackgroundTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Background(id INTEGER AUTO_INCREMENT, "
                + "name VARCHAR(255), feature VARCHAR(8000), "
                + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Class'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createClassTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Class(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "randomProfs INTEGER, randomLangs INTEGER, extraProfs INTEGER, "
                + "extraProfType VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Subclass'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createSubclassTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE SubClass(id INTEGER AUTO_INCREMENT, class_id INTEGER, "
                + "name VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id), FOREIGN KEY "
                + "(class_id) REFERENCES Class(id));").executeUpdate();
    }

    /**
     * Luodaan liitostaulu 'ClassProficiency'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createClassProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE ClassProficiency(class_id INTEGER, prof_id INTEGER, "
                + "certain BOOLEAN, FOREIGN KEY (class_id) REFERENCES Class(id), "
                + "FOREIGN KEY (prof_id) REFERENCES Proficiency(id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Feat'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createFeatTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Feat(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "stats VARCHAR(255), randomProfs INTEGER, randomLangs INTEGER, "
                + "extraProfs INTEGER, extraProfType VARCHAR(255), PRIMARY KEY (id), "
                + "UNIQUE KEY (id));").executeUpdate();
    }

    /**
     * Luodaan liitostaulu 'FeatProficiency'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createFeatProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE FeatProficiency(feat_id INTEGER, prof_id INTEGER, "
                + "certain BOOLEAN, FOREIGN KEY (feat_id) REFERENCES Feat(id), "
                + "FOREIGN KEY (prof_id) REFERENCES Proficiency(id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Racial'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createRacialTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Racial(id INTEGER AUTO_INCREMENT, "
                + "name VARCHAR(255), stats INTEGER, feat BOOLEAN, "
                + "randomProfs INTEGER, randomLangs INTEGER, extraProfs INTEGER, "
                + "extraProfType VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    /**
     * Luodaan liitostaulu 'RacialProficiency'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createRacialProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE RacialProficiency(racial_id INTEGER, "
                + "prof_id INTEGER, certain BOOLEAN, FOREIGN KEY (racial_id) "
                + "REFERENCES Racial(id), FOREIGN KEY (prof_id) REFERENCES "
                + "Proficiency(id));").executeUpdate();
    }

    /**
     * Luodaan tietokantataulu 'Race'
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createRaceTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Race(id INTEGER AUTO_INCREMENT, "
                + "name VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }
}
