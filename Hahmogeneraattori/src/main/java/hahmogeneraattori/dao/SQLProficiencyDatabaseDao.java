/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.List;
import java.util.ArrayList;

/**
 * Luokka huolehtii tietokantataulua 'Proficiency' koskevan
 * tietokantatoiminnallisuuden
 *
 * @author sampo
 */
public class SQLProficiencyDatabaseDao implements GeneratorDatabaseDao {

    private List<Proficiency> profs;
    private String connection;
    private String user;
    private String password;

    /**
     * Konstruktorissa luokalle annetaan tietokantayhteyden tiedot ja tuodaan
     * tietokantataulussa 'Proficiency' käynnistäessä olevat tiedot listaan
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#initialize()
     *
     * @param connPath tietokantatiedosto
     * @param user käyttäjä
     * @param pswd salasana
     *
     * @throws SQLException
     */
    public SQLProficiencyDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.profs = new ArrayList<>();

        initialize();
    }

    /**
     * Tietokantatauluun 'Proficiency' lisätään uusi taito (proficiency)
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#openConnection()
     *
     * @param obj lisättävä proficiency
     *
     * @throws SQLException
     */
    @Override
    public void create(Object obj) throws SQLException {
        Proficiency prof = (Proficiency) obj;

        if (!this.profs.contains(prof)) {
            Connection conn = openConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Proficiency "
                    + "(name, type) VALUES (?, ?);");
            stmt.setString(1, prof.getName());
            stmt.setString(2, prof.getType());
            stmt.executeUpdate();
            stmt.close();

            prof.setId(getProfId(prof, conn));

            conn.close();

            this.profs.add(prof);
        }
    }

    /**
     * Tietokantatauluun 'Proficiency' päivitetään tietty taito (proficiency)
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#openConnection()
     *
     * @param obj päivitettävä proficiency
     *
     * @throws SQLException
     */
    @Override
    public void update(Object obj) throws SQLException {
        Proficiency prof = (Proficiency) obj;

        if (!this.profs.contains(prof)) {
            Connection conn = openConnection();

            updateProfToProfs(prof);

            PreparedStatement stmt = conn.prepareStatement("UPDATE Proficiency "
                    + "SET name = ?, type = ? WHERE id = ?;");
            stmt.setString(1, prof.getName());
            stmt.setString(2, prof.getType());
            stmt.setInt(3, prof.getId());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
    }

    /**
     * Tietokantataulusta 'Proficiency' poistetaan tietty taito (proficiency)
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#openConnection()
     *
     * @param obj poistettava proficiency
     *
     * @throws SQLException
     */
    @Override
    public void delete(Object obj) throws SQLException {
        Proficiency prof = (Proficiency) obj;
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Proficiency "
                + "WHERE id = ?;");
        stmt.setInt(1, prof.getId());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        this.profs.remove(prof);
    }

    /**
     * Tietokantataulun 'Proficiency' sisältö palautetaan listana
     *
     * @param c Proficiencyä vastaava luokka
     *
     * return taulun sisältö listana
     */
    @Override
    public List list(Class c) {
        return this.profs;
    }

    /**
     * Haetaan tietokantataulun 'Proficiency' sisältö luokan hallinnoimaan
     * listaan
     *
     * @see hahmogeneraattori.dao.SQLProficiencyDatabaseDao#openConnection()
     *
     * @throws SQLException
     */
    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmtProf = conn.prepareStatement("SELECT * FROM Proficiency");
        ResultSet rsProf = stmtProf.executeQuery();
        while (rsProf.next()) {
            this.profs.add(new Proficiency(rsProf.getInt(1), rsProf.getString(2),
                    rsProf.getString(3)));
        }
        stmtProf.close();
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
     * Metodi hakee tietokannasta tietyn taidon (proficiency) indeksin
     * 
     * @param prof haettava proficiency
     * @param conn tietokantayhteys
     * 
     * @return proficiencyn indeksi
     * 
     * @throws SQLException 
     */
    public int getProfId(Proficiency prof, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Proficiency WHERE name = ? AND type = ?;");
        stmt.setString(1, prof.getName());
        stmt.setString(2, prof.getType());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();

        return id;
    }

    /**
     * Metodi päivittää tietyn taidon (proficiency) luokan hallinnoimaan listaan
     * 
     * @param prof päivitettävä proficiency
     */
    public void updateProfToProfs(Proficiency prof) {
        for (Proficiency oldProf : this.profs) {
            if (oldProf.getId() == prof.getId()) {
                oldProf.setName(prof.getName());
                oldProf.setType(prof.getType());
                break;
            }
        }
    }
}
