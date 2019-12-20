/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Race;
import hahmogeneraattori.domain.Proficiency;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Luokka huolehtii tietokantataulua 'Race' koskevan tietokantatoiminnallisuuden
 *
 * @author sampo
 */
public class SQLRaceDatabaseDao implements GeneratorDatabaseDao {

    private List<Race> races;
    private String connection;
    private String user;
    private String password;

    /**
     * Konstruktorissa luokalle annetaan tietokantayhteyden tiedot ja tuodaan
     * tietokantataulussa 'Race' käynnistäessä olevat tiedot listaan
     *
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#initialize()
     *
     * @param connPath tietokantatiedosto
     * @param user käyttäjä
     * @param pswd salasana
     *
     * @throws SQLException
     */
    public SQLRaceDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.races = new ArrayList<>();

        initialize();
    }

    /**
     * Tietokantatauluun 'Race' lisätään uusi race (rotu)
     *
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#openConnection()
     *
     * @param obj lisättävä race
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        Race race = (Race) obj;

        if (!this.races.contains(race)) {
            Connection conn = openConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Race "
                    + "(name) VALUES (?);");
            stmt.setString(1, race.getName());
            stmt.executeUpdate();
            stmt.close();

            race.setId(getRaceId(race, conn));

            this.races.add(race);
            conn.close();
        }
    }

    /**
     * Tietokantatauluun 'Race' päivitetään tietty rotu (race)
     *
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#openConnection()
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#updateRaceToRaces(Race)
     *
     * @param obj päivitettävä race
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        Race race = (Race) obj;

        Connection conn = openConnection();

        updateRaceToRaces(race);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Race "
                + "SET name = ? WHERE id = ?;");
        stmt.setString(1, race.getName());
        stmt.setInt(2, race.getId());
        stmt.executeUpdate();
        stmt.close();

        conn.close();
    }

    /**
     * Tietokantataulusta 'Race' poistetaan tietty rotu (race)
     *
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#openConnection()
     *
     * @param obj poistettava race
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        Race race = (Race) obj;
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Race "
                + "WHERE id = ?;");
        stmt.setInt(1, race.getId());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        this.races.remove(race);
    }

    /**
     * Tietokantataulun 'Race' sisältö palautetaan listana
     *
     * @param c Racea vastaava luokka
     *
     * @return taulun sisältö listana
     */
    @Override
    public List list(Class c) {
        return this.races;
    }

    /**
     * Haetaan tietokantataulun 'Race' sisältö luokan hallinnoimaan listaan
     *
     * @see hahmogeneraattori.dao.SQLRaceDatabaseDao#openConnection()
     *
     * @throws SQLException
     */
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Race");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Race newRace = new Race(rs.getInt(1), rs.getString(2));

            this.races.add(newRace);
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
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }

    /**
     * Metodi hakee tietokannasta tietyn rodun (race) indeksin
     *
     * @param race haettava race
     * @param conn tietokantayhteys
     *
     * @return classin indeksi
     *
     * @throws SQLException
     */
    public int getRaceId(Race race, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Race WHERE name = ?;");
        stmt.setString(1, race.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        rs.close();
        return id;
    }

    /**
     * Metodi päivittää tietyn rodun (race) luokan hallinnoimaan listaan
     *
     * @param race päivitettävä race
     */
    public void updateRaceToRaces(Race race) {
        for (Race oldRace : this.races) {
            if (oldRace.getId() == race.getId()) {
                oldRace.setName(race.getName());
                break;
            }
        }
    }
}
