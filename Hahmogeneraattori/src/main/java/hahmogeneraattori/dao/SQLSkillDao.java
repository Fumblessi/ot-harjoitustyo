/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author sampo
 */
public class SQLSkillDao implements SkillDao {

    private List<Proficiency> profs;

    public SQLSkillDao() throws SQLException {
        this.profs = new ArrayList<>();

        Connection conn = DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "");

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Skill");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            this.profs.add(new Proficiency(rs.getString(2), rs.getString(3)));
        }
        stmt.close();
        conn.close();
    }

    @Override
    public void create(Proficiency prof) throws SQLException {
        if (!this.profs.contains(prof)) {
            Connection conn = DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "");

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Skill (name, type) "
                    + "VALUES (?, ?)");
            stmt.setString(1, prof.getName());
            stmt.setString(2, prof.getType());

            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            this.profs.add(prof);
        } 
    }

    @Override
    public Proficiency read(Integer key) throws SQLException {
        return null;
    }

    @Override
    public Proficiency update(Proficiency prof) throws SQLException {
        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {

    }

    @Override
    public List<Proficiency> list() {
        return this.profs;
    }
}
