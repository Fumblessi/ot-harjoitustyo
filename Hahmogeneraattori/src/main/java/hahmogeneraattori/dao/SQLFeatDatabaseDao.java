/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Feat;
import hahmogeneraattori.domain.Proficiency;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka huolehtii tietokantataulua 'Feat' koskevan tietokantatoiminnallisuuden
 *
 * @author sampo
 */
public class SQLFeatDatabaseDao implements GeneratorDatabaseDao {

    private List<Feat> feats;
    private String connection;
    private String user;
    private String password;

    /**
     * Konstruktorissa luokalle annetaan tietokantayhteyden tiedot ja tuodaan
     * tietokantataulussa 'Feat' käynnistäessä olevat tiedot listaan
     *
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#initialize()
     *
     * @param connPath tietokantatiedosto
     * @param user käyttäjä
     * @param pswd salasana
     *
     * @throws SQLException
     */
    public SQLFeatDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.feats = new ArrayList<>();

        initialize();
    }

    /**
     * Tietokantatauluun 'Feat' lisätään uusi feat (erityistaito)
     *
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#setFeatAttributesToStatement(PreparedStatement,
     * Feat)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#addProficiencies(Feat,
     * Connection)
     *
     * @param obj lisättävä feat
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        Feat feat = (Feat) obj;

        if (!this.feats.contains(feat)) {
            Connection conn = openConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Feat "
                    + "(name, stats, randomProfs, randomLangs, extraProfs, "
                    + "extraProfType) VALUES (?, ?, ?, ?, ?, ?);");
            setFeatAttributesToStatement(stmt, feat);
            stmt.executeUpdate();
            stmt.close();

            feat.setId(getFeatId(feat, conn));

            this.feats.add(feat);

            addProficiencies(feat, conn);

            conn.close();
        }
    }

    /**
     * Tietokantatauluun 'Feat' päivitetään tietty erityistaito (feat)
     *
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#openConnection()
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#updateFeatToFeats(Feat)
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#setFeatAttributesToStatement(PreparedStatement,
     * Feat)
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#deleteFeatProficiencies(Feat,
     * Connection)
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#addProficiencies(Feat,
     * Connection)
     *
     * @param obj päivitettävä feat
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        Feat feat = (Feat) obj;

        Connection conn = openConnection();

        updateFeatToFeats(feat);
        PreparedStatement stmt = conn.prepareStatement("UPDATE Feat "
                + "SET name = ?, stats = ?, randomProfs = ?, randomLangs = ?, "
                + "extraProfs = ?, extraProfType = ? WHERE id = ?;");
        setFeatAttributesToStatement(stmt, feat);
        stmt.setInt(7, feat.getId());
        stmt.executeUpdate();
        stmt.close();

        deleteFeatProficiencies(feat, conn);
        addProficiencies(feat, conn);
        conn.close();
    }

    /**
     * Tietokantataulusta 'Feat' poistetaan tietty erityistaio (Feat)
     *
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#deleteFeatProficiencies(Feat,
     * Connection)
     *
     * @param obj poistettava feat
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        Feat feat = (Feat) obj;
        Connection conn = openConnection();

        deleteFeatProficiencies(feat, conn);

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Feat "
                + "WHERE id = ?;");
        stmt.setInt(1, feat.getId());

        stmt.executeUpdate();
        stmt.close();
        conn.close();
        this.feats.remove(feat);
    }

    /**
     * Tietokantataulun 'Feat' sisältö palautetaan listana
     *
     * @param c Feattia vastaava luokka
     *
     * @return taulun sisältö listana
     */
    @Override
    public List list(Class c) {
        return this.feats;
    }

    /**
     * Haetaan tietokantataulun 'Feat' sisältö luokan hallinnoimaan listaan
     *
     * @see hahmogeneraattori.dao.SQLFeatDatabaseDao#openConnection()
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#getCertainProficiencies(Feat,
     * Connection)
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#getUncertainProficiencies(Feat,
     * Connection)
     *
     * @throws SQLException
     */
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Feat;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Feat feat = new Feat(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7));

            getCertainProficiencies(feat, conn);
            getUncertainProficiencies(feat, conn);

            this.feats.add(feat);
        }
        stmt.close();
        rs.close();
        conn.close();
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
     * Metodi hakee tietokannasta tietyn erityistaidon (feat) indeksin
     *
     * @param feat haettava feat
     * @param conn tietokantayhteys
     *
     * @return featin indeksi
     *
     * @throws SQLException
     */
    public int getFeatId(Feat feat, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Feat WHERE name = ?;");
        stmt.setString(1, feat.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        return id;
    }

    /**
     * Lisää erityistaitoon (feat) liittyvät varmat ja epävarmat taidot niitä
     * vastaaviin liitostauluihin
     *
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#addCertainProficiencies(Feat,
     * Connection)
     * @see
     * hahmogeneraattori.dao.SQLFeatDatabaseDao#addUncertainProficiencies(Feat,
     * Connection)
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void addProficiencies(Feat feat, Connection conn) throws SQLException {
        addCertainProficiencies(feat, conn);
        addUncertainProficiencies(feat, conn);
    }

    /**
     * Lisätään erityistaitoon liittyvät varmat taidot niitä vastaavaan
     * liitostauluun
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void addCertainProficiencies(Feat feat, Connection conn) throws SQLException {
        for (Proficiency prof : feat.getCertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO FeatProficiency (feat_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, feat.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, true);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }

    /**
     * Lisätään erityistaitoon liittyvät epävarmat taidot niitä vastaavaan
     * liitostauluun
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void addUncertainProficiencies(Feat feat, Connection conn) throws SQLException {
        for (Proficiency prof : feat.getUncertainProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO FeatProficiency (feat_id, prof_id, certain) VALUES "
                    + "(?, ?, ?);");
            connectProf.setInt(1, feat.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.setBoolean(3, false);
            connectProf.executeUpdate();
            connectProf.close();
        }
    }

    /**
     * Haetaan erityistaitoon liittyvät varmat taidot tietokannasta
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void getCertainProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN FeatProficiency ON "
                + "FeatProficiency.prof_id = Proficiency.id WHERE "
                + "feat_id = ? AND certain = ?;");
        getProfs.setInt(1, feat.getId());
        getProfs.setBoolean(2, true);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            feat.addCertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
        rsProfs.close();
    }

    /**
     * Haetaan erityistaitoon liittyvät epävarmat taidot tietokannasta
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void getUncertainProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN FeatProficiency ON "
                + "FeatProficiency.prof_id = Proficiency.id WHERE "
                + "feat_id = ? AND certain = ?;");
        getProfs.setInt(1, feat.getId());
        getProfs.setBoolean(2, false);
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            feat.addUncertainProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3), rsProfs.getString(4)));
        }
        getProfs.close();
        rsProfs.close();
    }

    /**
     * Poistetaan erityistaitoon liittyvät taidot tietokannasta
     *
     * @param feat erityistaito
     * @param conn tietokantayhteys
     *
     * @throws SQLException
     */
    public void deleteFeatProficiencies(Feat feat, Connection conn) throws SQLException {
        PreparedStatement deleteProfs = conn.prepareStatement("DELETE FROM "
                + "FeatProficiency WHERE feat_id = ?;");
        deleteProfs.setInt(1, feat.getId());
        deleteProfs.executeUpdate();
        deleteProfs.close();
    }

    /**
     * Asetetaan tietokantakyselylle erityistaidon parametrit
     *
     * @param stmt SQL-lause
     * @param feat erityistaito
     *
     * @throws SQLException
     */
    public void setFeatAttributesToStatement(PreparedStatement stmt, Feat feat) throws SQLException {
        stmt.setString(1, feat.getName());
        stmt.setString(2, feat.getStats());
        stmt.setInt(3, feat.getRandomProfs());
        stmt.setInt(4, feat.getRandomLangs());
        stmt.setInt(5, feat.getExtraProfs());
        stmt.setString(6, feat.getExtraProfType());
    }

    /**
     * Metodi päivittää tietyn erityistaidon (feat) luokan hallinnoimaan listaan
     *
     * @param feat päivitettävä feat
     */
    public void updateFeatToFeats(Feat feat) {
        for (Feat oldFeat : this.feats) {
            if (oldFeat.getId() == feat.getId()) {
                oldFeat.setName(feat.getName());
                oldFeat.setStats(feat.getStats());
                oldFeat.setRandomProfs(feat.getRandomProfs());
                oldFeat.setRandomLangs(feat.getRandomLangs());
                oldFeat.setExtraProfs(feat.getExtraProfs());
                oldFeat.setExtraProfType(feat.getExtraProfType());
                oldFeat.setCertainProfs(feat.getCertainProfs());
                oldFeat.setUncertainProfs(feat.getUncertainProfs());
                break;
            }
        }
    }
}
