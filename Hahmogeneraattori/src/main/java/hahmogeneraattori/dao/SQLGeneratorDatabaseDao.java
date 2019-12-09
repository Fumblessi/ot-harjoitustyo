/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Luokkaan sisältyy kaikki generaattorin käyttämä tietokantatoiminnallisuus
 *
 * @author sampo
 */
public class SQLGeneratorDatabaseDao implements GeneratorDatabaseDao {

    private List<Proficiency> profs;
    private List<Racial> racials;
    private List<RpgClass> classes;
    private List<Background> backgrounds;
    private List<Feat> feats;
    private String connection;
    private String user;
    private String password;

    /**
     * Luokan konstruktorissa tuodaan tietokannan sisältö luokan sisältämiin
     * listoihin
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeLists(Connection)
     *
     * @throws SQLException
     */
    public SQLGeneratorDatabaseDao(String connPath, String user, String psw) throws SQLException {
        this.profs = new ArrayList<>();
        this.racials = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.backgrounds = new ArrayList<>();
        this.feats = new ArrayList<>();
        this.connection = connPath;
        this.user = user;
        this.password = psw;

        Connection conn = openConnection();

        //initializeLists(conn);
        //HUOM! Jos initialisoit koko tietokannan initialize()-metodilla, 
        //initializeLists()-metodia ei saa suorittaa konstruktorissa!
        conn.close();
    }

    /**
     * Metodi lisää tietokantaan halutun objektin, jos se on tietokantataulujen
     * sisältämien tietojen muotoinen
     *
     * @param obj käyttäjän syöttämä tietokantaan lisättävä objekti
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createProf(Proficiency,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createRacial(Racial,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createClass(RpgClass,
     * Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createBackground(Background,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#createFeat(Feat,
     * Connection)
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            createProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            createRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            createClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            createBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            createFeat(feat, conn);
        }
        conn.close();
    }

    /**
     * Metodi lisää tietokantatauluun 'Proficiency' uuden proficiencyn (taidon,
     * joka generoidulla hahmolla voi olla). Jos tietokanta jo sisältää kyseisen
     * proficiencyn, ei kuitenkaan luoda uutta.
     *
     * @param prof käyttäjän tietokantatauluun lisäämä Proficiency-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createProf(Proficiency prof, Connection conn) throws SQLException {
        if (!this.profs.contains(prof)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Proficiency "
                    + "(name, type) VALUES (?, ?);");
            stmt.setString(1, prof.getName());
            stmt.setString(2, prof.getType());
            stmt.executeUpdate();
            stmt.close();

            PreparedStatement idStmt = conn.prepareStatement("SELECT id FROM "
                    + "Proficiency WHERE name = ? AND type = ?;");
            idStmt.setString(1, prof.getName());
            idStmt.setString(2, prof.getType());
            ResultSet id = idStmt.executeQuery();
            while (id.next()) {
                prof.setId(id.getInt(1));
            }
            idStmt.close();

            this.profs.add(prof);
        }
    }

    /**
     * Metodi lisää tietokantatauluun 'Racial' uuden racialin (ominaisuuden,
     * joka generoidulla hahmolla voi olla). Jos tietokanta jo sisältää kyseisen
     * racialin, ei kuitenkaan luoda uutta.
     *
     * @param racial käyttäjän tietokantatauluun lisäämä Racial-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createRacial(Racial racial, Connection conn) throws SQLException {
        if (!this.racials.contains(racial)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Racial "
                    + "(name, stats, feat) VALUES (?, ?, ?);");
            stmt.setString(1, racial.getName());
            stmt.setInt(2, racial.getStats());
            stmt.setBoolean(3, racial.getFeat());
            stmt.executeUpdate();
            stmt.close();

            PreparedStatement idStmt = conn.prepareStatement("SELECT id FROM "
                    + "Racial WHERE name = ?;");
            idStmt.setString(1, racial.getName());
            ResultSet id = idStmt.executeQuery();
            while (id.next()) {
                racial.setId(id.getInt(1));
            }
            idStmt.close();

            this.racials.add(racial);

            for (Proficiency prof : racial.getRacialProfs()) {
                if (!this.profs.contains(prof)) {
                    createProf(prof, conn);
                }
                PreparedStatement connectProf = conn.prepareStatement("INSERT "
                        + "INTO RacialProficiency (racial_id, prof_id) VALUES "
                        + "(?, ?);");
                connectProf.setInt(1, racial.getId());
                connectProf.setInt(2, prof.getId());
                connectProf.close();
            }
        }
    }

    /**
     * Metodi lisää tietokantatauluun 'Class' uuden classin (hahmoluokan, joka
     * generoidulla hahmolla voi olla). Jos tietokanta jo sisältää kyseisen
     * classin, ei kuitenkaan luoda uutta.
     *
     * @param rpgclass käyttäjän tietokantatauluun lisäämä Class-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createClass(RpgClass rpgclass, Connection conn) throws SQLException {

    }

    /**
     * Metodi lisää tietokantatauluun 'Background' uuden backgroundin (taustan,
     * joka generoidulla hahmolla voi olla). Jos tietokanta jo sisältää kyseisen
     * backgroundin, ei kuitenkaan luoda uutta.
     *
     * @param bg käyttäjän tietokantatauluun lisäämä Background-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createBackground(Background bg, Connection conn) throws SQLException {

    }

    /**
     * Metodi lisää tietokantatauluun 'Feat' uuden featin (erikoistaito, joka
     * generoidulla hahmolla voi olla). Jos tietokanta jo sisältää kyseisen
     * featin, ei kuitenkaan luoda uutta.
     *
     * @param feat käyttäjän tietokantatauluun lisäämä Feat-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createFeat(Feat feat, Connection conn) throws SQLException {

    }

    /**
     * Metodi päivittää tietokannassa olevan instanssin käyttäjän muokkaamasta
     * objektista
     *
     * @param obj käyttäjän syöttämä päivitetty objekti
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#updateProf(Proficiency,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#updateRacial(Racial,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#updateClass(RpgClass,
     * Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#updateBackground(Background,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#updateFeat(Feat,
     * Connection)
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            updateProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            updateRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            updateClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            updateBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            updateFeat(feat, conn);
        }
        conn.close();
    }

    /**
     * Metodi päivittää tietokantataulussa 'Proficiency' olevan proficiencyn
     * käyttäjän muuttamaan muotoon. Jos yritetään päivittää proficiency
     * samanlaiseksi, kuin joku toinen tietokannassa oleva proficiency, ei
     * kuitenkaan luoda toista samanlaista tietokantaan
     *
     * @param prof käyttäjän päivittämä Proficiency-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void updateProf(Proficiency prof, Connection conn) throws SQLException {
        if (!this.profs.contains(prof)) {
            for (Proficiency oldProf : this.profs) {
                if (oldProf.getId() == prof.getId()) {
                    oldProf.setName(prof.getName());
                    oldProf.setType(prof.getType());
                    break;
                }
            }

            PreparedStatement stmt = conn.prepareStatement("UPDATE Proficiency "
                    + "SET name = ?, type = ? WHERE id = ?;");
            stmt.setString(1, prof.getName());
            stmt.setString(2, prof.getType());
            stmt.setInt(3, prof.getId());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    /**
     * Metodi päivittää tietokantataulussa 'Racial' olevan racialin käyttäjän
     * muuttamaan muotoon. Jos yritetään päivittää racial samanlaiseksi, kuin
     * joku toinen tietokannassa oleva racial, ei kuitenkaan luoda toista
     * samanlaista tietokantaan
     *
     * @param racial käyttäjän päivittämä Racial-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void updateRacial(Racial racial, Connection conn) throws SQLException {

    }

    /**
     * Metodi päivittää tietokantataulussa 'Class' olevan classin käyttäjän
     * muuttamaan muotoon. Jos yritetään päivittää class samanlaiseksi, kuin
     * joku toinen tietokannassa oleva class, ei kuitenkaan luoda toista
     * samanlaista tietokantaan
     *
     * @param rpgclass käyttäjän päivittämä RpgClass-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void updateClass(RpgClass rpgclass, Connection conn) throws SQLException {

    }

    /**
     * Metodi päivittää tietokantataulussa 'Background' olevan backgroundin
     * käyttäjän muuttamaan muotoon. Jos yritetään päivittää background
     * samanlaiseksi, kuin joku toinen tietokannassa oleva background, ei
     * kuitenkaan luoda toista samanlaista tietokantaan
     *
     * @param bg käyttäjän päivittämä Background-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void updateBackground(Background bg, Connection conn) throws SQLException {

    }

    /**
     * Metodi päivittää tietokantataulussa 'Feat' olevan featin käyttäjän
     * muuttamaan muotoon. Jos yritetään päivittää feat samanlaiseksi, kuin joku
     * toinen tietokannassa oleva feat, ei kuitenkaan luoda toista samanlaista
     * tietokantaan
     *
     * @param feat käyttäjän päivittämä Feat-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void updateFeat(Feat feat, Connection conn) throws SQLException {

    }

    /**
     * Metodi poistaa tietokannasta käyttäjän haluaman objektin
     *
     * @param obj käyttäjän valitsema poistettava objekti
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#deleteProf(Proficiency,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#deleteRacial(Racial,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#deleteClass(RpgClass,
     * Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#deleteBackground(Background,
     * Connection)
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#deleteFeat(Feat,
     * Connection)
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            deleteProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            deleteRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            deleteClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            deleteBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            deleteFeat(feat, conn);
        }
        conn.close();
    }

    /**
     * Metodi poistaa 'Proficiency'-tietokantataulusta käyttäjän valitseman
     * proficiencyn
     *
     * @param prof poistetava Proficiency-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteProf(Proficiency prof, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Proficiency "
                + "WHERE id = ?;");
        stmt.setInt(1, prof.getId());

        stmt.executeUpdate();
        stmt.close();

        this.profs.remove(prof);
    }

    /**
     * Metodi poistaa 'Racial'-tietokantataulusta käyttäjän valitseman racialin
     *
     * @param racial poistetava Racial-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteRacial(Racial racial, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Racial "
                + "WHERE id = ?;");
        stmt.setInt(1, racial.getId());

        stmt.executeUpdate();
        stmt.close();

        this.racials.remove(racial);
    }

    /**
     * Metodi poistaa 'Class'-tietokantataulusta käyttäjän valitseman classin
     *
     * @param rpgclass poistetava RpgClass-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteClass(RpgClass rpgclass, Connection conn) throws SQLException {

    }

    /**
     * Metodi poistaa 'Background'-tietokantataulusta käyttäjän valitseman
     * backgroundin
     *
     * @param bg poistetava Background-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteBackground(Background bg, Connection conn) throws SQLException {

    }

    /**
     * Metodi poistaa 'Feat'-tietokantataulusta käyttäjän valitseman featin
     *
     * @param feat poistetava Feat-olio
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteFeat(Feat feat, Connection conn) throws SQLException {

    }

    /**
     * Metodi palautaa listana tietyn tietokantataulun sisällön, tai null, jos
     * pyydetään sellaista luokkaa, mitä vastaavaa tietokantataulua ei ole
     *
     * @param c syötetty luokka, jonka mukainen tietokantataulu halutaan listata
     *
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#listProfs()
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#listRacials()
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#listClasses()
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#listBackgrounds()
     * @see hahmogeneraattori.dao.SQLGeneratorDatabaseDao#listFeats()
     *
     * @return lista tietyn tietokantataulun sisällöstä
     */
    @Override
    public List list(Class c) {
        if (c == Proficiency.class) {
            return listProfs();
        } else if (c == Racial.class) {
            return listRacials();
        } else if (c == RpgClass.class) {
            return listClasses();
        } else if (c == Background.class) {
            return listBackgrounds();
        } else if (c == Feat.class) {
            return listFeats();
        }
        return null;
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Proficiency' sisällön
     *
     * @return tietokantataulun 'Proficiency' sisältö
     */
    public List<Proficiency> listProfs() {
        return this.profs;
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Racial' sisällön
     *
     * @return tietokantataulun 'Racial' sisältö
     */
    public List<Racial> listRacials() {
        return this.racials;
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Class' sisällön
     *
     * @return tietokantataulun 'Class' sisältö
     */
    public List<RpgClass> listClasses() {
        return this.classes;
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Background' sisällön
     *
     * @return tietokantataulun 'Background' sisältö
     */
    public List<Background> listBackgrounds() {
        return this.backgrounds;
    }

    /**
     * Metodi palauttaa listana tietokantataulun 'Feat' sisällön
     *
     * @return tietokantataulun 'Feat' sisältö
     */
    public List<Feat> listFeats() {
        return this.feats;
    }

    /**
     * Metodi avaa tietokantayhteyden tietokantaan generatordb.mv.db
     *
     * @return tietokantayhteys
     *
     * @throws SQLException
     */
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }

    /**
     * Metodi hakee tietokantataulujen sisällöt luokan käyttämiin listoihin
     *
     * @param conn tietokantayhteys
     *
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeProfs(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeRacials(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeClasses(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeBackgrounds(Connection)
     * @see
     * hahmogeneraattori.dao.SQLGeneratorDatabaseDao#initializeFeats(Connection)
     *
     * @throws SQLException
     */
    public final void initializeLists(Connection conn) throws SQLException {
        initializeProfs(conn);
        initializeRacials(conn);
        initializeClasses(conn);
        initializeBackgrounds(conn);
        initializeFeats(conn);
    }

    /**
     * Metodi hakee tietokantataulun 'Proficiency' sisällön listaan profs
     * Proficiency-muotoisina olioina
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public final void initializeProfs(Connection conn) throws SQLException {
        PreparedStatement stmtProf = conn.prepareStatement("SELECT * FROM Proficiency");
        ResultSet rsProf = stmtProf.executeQuery();
        while (rsProf.next()) {
            this.profs.add(new Proficiency(rsProf.getInt(1), rsProf.getString(2),
                    rsProf.getString(3)));
        }
        stmtProf.close();
    }

    /**
     * Metodi hakee tietokantataulun 'Racial' sisällön listaan racials
     * Racial-muotoisina olioina
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public final void initializeRacials(Connection conn) throws SQLException {
        PreparedStatement stmtRacial = conn.prepareStatement("SELECT * FROM Racial");
        ResultSet rsRacial = stmtRacial.executeQuery();
        while (rsRacial.next()) {
            Racial newRacial = new Racial(rsRacial.getInt(1), rsRacial.getString(2), rsRacial.getInt(3),
                    rsRacial.getBoolean(4));
            PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                    + "Proficiency LEFT JOIN RacialProficiency ON "
                    + "RacialProficiency.prof_id = Proficiency.id WHERE "
                    + "racial_id = ?;");
            getProfs.setInt(1, rsRacial.getInt(1));
            ResultSet rsProfs = getProfs.executeQuery();

            while (rsProfs.next()) {
                newRacial.addRacialProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                        rsProfs.getString(3)));
            }
            getProfs.close();

            this.racials.add(newRacial);
        }
        stmtRacial.close();
    }

    /**
     * Metodi hakee tietokantataulun 'Class' sisällön listaan classes
     * RpgClass-muotoisina olioina
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public final void initializeClasses(Connection conn) throws SQLException {
        PreparedStatement stmtClass = conn.prepareStatement("SELECT * FROM Class");
        ResultSet rsClass = stmtClass.executeQuery();
        while (rsClass.next()) {
            PreparedStatement stmtSubclass = conn.prepareStatement("SELECT * FROM"
                    + " SubClass WHERE class_id = ?");
            int id = rsClass.getInt(1);
            stmtSubclass.setInt(1, id);
            ResultSet rsSubclass = stmtSubclass.executeQuery();
            RpgClass rpgClass = new RpgClass(id, rsClass.getString(2));
            ArrayList<String> subclasses = new ArrayList<>();
            while (rsSubclass.next()) {
                subclasses.add(rsSubclass.getString(3));
            }
            rpgClass.setSubclasses(subclasses);
            this.classes.add(rpgClass);
        }
        stmtClass.close();
    }

    /**
     * Metodi hakee tietokantataulun 'Background' sisällön listaan backgrounds
     * Background-muotoisina olioina
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public final void initializeBackgrounds(Connection conn) throws SQLException {
        PreparedStatement stmtBg = conn.prepareStatement("SELECT * FROM Background");
        ResultSet rsBg = stmtBg.executeQuery();
        while (rsBg.next()) {
            this.backgrounds.add(new Background(rsBg.getInt(1), rsBg.getString(2)));
        }
        stmtBg.close();
    }

    /**
     * Metodi hakee tietokantataulun 'Feat' sisällön listaan feats
     * Feat-muotoisina olioina
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public final void initializeFeats(Connection conn) throws SQLException {
        PreparedStatement stmtFeat = conn.prepareStatement("SELECT * FROM Feat");
        ResultSet rsFeat = stmtFeat.executeQuery();
        while (rsFeat.next()) {
            Feat feat = new Feat(rsFeat.getInt(1), rsFeat.getString(2));
            feat.setStatsFromString(rsFeat.getString(3));
            this.feats.add(feat);
        }
        stmtFeat.close();
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
    }

    /**
     * Metodi luo uudet tietokantataulut
     *
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void createTables(Connection conn) throws SQLException {
        createProfTable(conn);
        createBackgroundTable(conn);
        createBgProfTable(conn);
        createClassTable(conn);
        createSubclassTable(conn);
        createClassProfTable(conn);
        createFeatTable(conn);
        createFeatProfTable(conn);
        createRacialTable(conn);
        createRacialProfTable(conn);
    }

    public void createProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Proficiency(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "type VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    public void createBackgroundTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Background(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    public void createBgProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE BackgroundProficiency(bg_id INTEGER, prof_id INTEGER, "
                + "FOREIGN KEY (bg_id) REFERENCES Background(id), FOREIGN KEY (prof_id) "
                + "REFERENCES Proficiency(id));").executeUpdate();
    }

    public void createClassTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Class(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }

    public void createSubclassTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE SubClass(id INTEGER AUTO_INCREMENT, class_id INTEGER, "
                + "name VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id), FOREIGN KEY "
                + "(class_id) REFERENCES Class(id));").executeUpdate();
    }
    
    public void createClassProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE ClassProficiency(class_id INTEGER, prof_id INTEGER, "
                + "FOREIGN KEY (class_id) REFERENCES Class(id), FOREIGN KEY (prof_id) "
                + "REFERENCES Proficiency(id));").executeUpdate();
    }
    
    public void createFeatTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Feat(id INTEGER AUTO_INCREMENT, name VARCHAR(255), "
                + "stats VARCHAR(255), PRIMARY KEY (id), UNIQUE KEY (id));").executeUpdate();
    }
    
    public void createFeatProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE FeatProficiency(feat_id INTEGER, prof_id INTEGER, "
                + "FOREIGN KEY (feat_id) REFERENCES Feat(id), FOREIGN KEY (prof_id) "
                + "REFERENCES Proficiency(id));").executeUpdate();
    }
    
    public void createRacialTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE Racial(id INTEGER AUTO_INCREMENT, "
                + "name VARCHAR(255), stats INTEGER, feat BOOLEAN, PRIMARY KEY (id), "
                + "UNIQUE KEY (id));").executeUpdate();
    }
    
    public void createRacialProfTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE TABLE RacialProficiency(racial_id INTEGER, prof_id INTEGER, "
                + "FOREIGN KEY (racial_id) REFERENCES Racial(id), FOREIGN KEY (prof_id) "
                + "REFERENCES Proficiency(id));").executeUpdate();
    }
}
