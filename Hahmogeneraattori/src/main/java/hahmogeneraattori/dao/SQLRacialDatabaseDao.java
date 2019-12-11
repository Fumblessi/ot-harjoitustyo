/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Racial;
import hahmogeneraattori.domain.Proficiency;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Luokka huolehtii tietokantataulua 'Racial' koskevan
 * tietokantatoiminnallisuuden
 *
 * @author sampo
 */
public class SQLRacialDatabaseDao implements GeneratorDatabaseDao {

    private List<Racial> racials;
    private String connection;
    private String user;
    private String password;

    /**
     * Konstruktorissa luokalle annetaan tietokantayhteyden tiedot ja tuodaan
     * tietokantataulussa 'Racial' käynnistäessä olevat tiedot listaan
     *
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#initialize()
     *
     * @param connPath tietokantatiedosto
     * @param user käyttäjä
     * @param pswd salasana
     *
     * @throws SQLException
     */
    public SQLRacialDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.racials = new ArrayList<>();

        initialize();
    }

    /**
     * Tietokantatauluun 'Racial' lisätään uusi ominaisuus (racial)
     *
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#openConnection()
     *
     * @param obj lisättävä racial
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        Racial racial = (Racial) obj;

        if (!this.racials.contains(racial)) {
            Connection conn = openConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Racial "
                    + "(name, stats, feat) VALUES (?, ?, ?);");
            stmt.setString(1, racial.getName());
            stmt.setInt(2, racial.getStats());
            stmt.setBoolean(3, racial.getFeat());
            stmt.executeUpdate();
            stmt.close();

            racial.setId(getRacialId(racial, conn));
            
            this.racials.add(racial);

            for (Proficiency prof : racial.getRacialProfs()) {
                PreparedStatement connectProf = conn.prepareStatement("INSERT "
                        + "INTO RacialProficiency (racial_id, prof_id) VALUES "
                        + "(?, ?);");
                connectProf.setInt(1, racial.getId());
                connectProf.setInt(2, prof.getId());
                connectProf.close();
            }
            conn.close();
        }
    }

    /**
     * Tietokantatauluun 'Racial' päivitetään tietty ominaisuus (racial)
     *
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#openConnection()
     *
     * @param obj päivitettävä racial
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        Racial racial = (Racial) obj;

        if (!this.racials.contains(racial)) {
            Connection conn = openConnection();

            updateRacialToRacials(racial);

            PreparedStatement stmt = conn.prepareStatement("UPDATE Racial "
                    + "SET name = ?, stats = ?, feat = ? WHERE id = ?;");
            stmt.setString(1, racial.getName());
            stmt.setInt(2, racial.getStats());
            stmt.setBoolean(3, racial.getFeat());
            stmt.setInt(4, racial.getId());
            stmt.executeUpdate();
            stmt.close();
            
            deleteRacialProficiencies(racial, conn);
            addRacialProficiencies(racial, conn);
                       
            conn.close();
        }
    }

    /**
     * Tietokantataulusta 'Racial' poistetaan tietty ominaisuus (racial)
     *
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#openConnection()
     *
     * @param obj poistettava racial
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        Racial racial = (Racial) obj;
        Connection conn = openConnection();

        deleteRacialProficiencies(racial, conn);
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Racial "
                + "WHERE id = ?;");
        stmt.setInt(1, racial.getId());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        this.racials.remove(racial);
    }

    /**
     * Tietokantataulun 'Racial' sisältö palautetaan listana
     *
     * @param c Racialia vastaava luokka
     *
     * return taulun sisältö listana
     */
    @Override
    public List list(Class c) {
        return this.racials;
    }

    /**
     * Haetaan tietokantataulun 'Racial' sisältö luokan hallinnoimaan listaan
     *
     * @see hahmogeneraattori.dao.SQLRacialDatabaseDao#openConnection()
     *
     * @throws SQLException
     */
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmtRacial = conn.prepareStatement("SELECT * FROM Racial");
        ResultSet rsRacial = stmtRacial.executeQuery();
        while (rsRacial.next()) {
            Racial newRacial = new Racial(rsRacial.getInt(1), rsRacial.getString(2), rsRacial.getInt(3),
                    rsRacial.getBoolean(4));

            addRacialProficiencies(newRacial, conn);

            this.racials.add(newRacial);
        }
        stmtRacial.close();
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
     * Metodi hakee tietokannasta tietyn ominaisuuden (racial) indeksin
     * 
     * @param racial haettava racial
     * @param conn tietokantayhteys
     * 
     * @return racialin indeksi
     * 
     * @throws SQLException 
     */
    public int getRacialId(Racial racial, Connection conn) throws SQLException {
        int id = -1;
        
        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Racial WHERE name = ?;");
        stmt.setString(1, racial.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        return id;
    }

    /**
     * Metodi hakee tietokannasta tietylle ominaisuudelle (racial) siihen 
     * kuuluvat taidot (proficiency)
     * 
     * @param racial haettava racial
     * @param conn tietokantayhteys
     * 
     * @throws SQLException 
     */
    public void addRacialProficiencies(Racial racial, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN RacialProficiency ON "
                + "RacialProficiency.prof_id = Proficiency.id WHERE "
                + "racial_id = ?;");
        getProfs.setInt(1, racial.getId());
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            racial.addRacialProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3)));
        }
        getProfs.close();
    }
    
    /**
     * Metodi poistaa tietokannasta tietylle ominaisuudelle (racial) siihen 
     * kuuluvat taidot (proficiency)
     * 
     * @param racial haettava racial
     * @param conn tietokantayhteys
     * 
     * @throws SQLException 
     */
    public void deleteRacialProficiencies(Racial racial, Connection conn) throws SQLException {
        PreparedStatement deleteProfs = conn.prepareStatement("DELETE FROM "
                + "RacialProficiency WHERE racial_id = ?;");
        deleteProfs.setInt(1, racial.getId());
        deleteProfs.executeUpdate();
        deleteProfs.close();
    }
    
    /**
     * Metodi päivittää tietyn ominaisuuden (racial) luokan hallinnoimaan 
     * listaan
     * 
     * @param racial päivitettävä racial
     */
    public void updateRacialToRacials(Racial racial) {
        for (Racial oldRacial : this.racials) {
            if (oldRacial.getId() == racial.getId()) {
                oldRacial.setName(racial.getName());
                oldRacial.setStats(racial.getStats());
                oldRacial.setFeat(racial.getFeat());
                oldRacial.setRacialProfs(racial.getRacialProfs());
                break;
            }
        }
    }
}
