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
 *
 * @author sampo
 */
public class SQLRaceDatabaseDao implements GeneratorDatabaseDao {
    
    private List<Race> races;
    private String connection;
    private String user;
    private String password;
    
    public SQLRaceDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.races = new ArrayList<>();

        initialize();
    }
    
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
    
    @Override
    public List list(Class c) {
        return this.races;
    }
    
    @Override
    public void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmt= conn.prepareStatement("SELECT * FROM Race");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Race newRace = new Race(rs.getInt(1), rs.getString(2));

            this.races.add(newRace);
        }
        stmt.close();
        conn.close();
    }
    
    @Override
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }
    
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
        return id;
    }
    
    public void updateRaceToRaces(Race race) {
        for (Race oldRace : this.races) {
            if (oldRace.getId() == race.getId()) {
                oldRace.setName(race.getName());
                break;
            }
        }
    }
}
