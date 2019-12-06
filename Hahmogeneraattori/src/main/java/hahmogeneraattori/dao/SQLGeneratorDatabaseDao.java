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

/**
 *
 * @author sampo
 */
public class SQLGeneratorDatabaseDao implements GeneratorDatabaseDao {

    private List<Proficiency> profs;
    private List<Racial> racials;
    private List<RpgClass> classes;
    private List<Background> backgrounds;
    private List<Feat> feats;

    public SQLGeneratorDatabaseDao() throws SQLException {
        this.profs = new ArrayList<>();

        Connection conn = DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "");

        PreparedStatement stmtProf = conn.prepareStatement("SELECT * FROM Proficiency");
        PreparedStatement stmtRacial = conn.prepareStatement("SELECT * FROM Racial");
        PreparedStatement stmtClass = conn.prepareStatement("SELECT * FROM Class");
        PreparedStatement stmtBg = conn.prepareStatement("SELECT * FROM Background");
        PreparedStatement stmtFeat = conn.prepareStatement("SELECT * FROM Feat");
        ResultSet rsProf = stmtProf.executeQuery();
        ResultSet rsRacial = stmtRacial.executeQuery();
        ResultSet rsClass = stmtClass.executeQuery();
        ResultSet rsBg = stmtBg.executeQuery();
        ResultSet rsFeat = stmtFeat.executeQuery();

        while (rsProf.next()) {
            this.profs.add(new Proficiency(rsProf.getString(2), rsProf.getString(3)));
        }
        while (rsRacial.next()) {
            this.racials.add(new Racial(rsRacial.getString(2)));
        }
        while (rsClass.next()) {
            PreparedStatement stmtSubclass = conn.prepareStatement("SELECT * FROM"
                    + " SubClass WHERE class_id = ?");
            stmtSubclass.setInt(1, rsClass.getInt(1));
            ResultSet rsSubclass = stmtSubclass.executeQuery();
            RpgClass rpgClass = new RpgClass(rsClass.getString(2));
            ArrayList<String> subclasses = new ArrayList<>();
            while (rsSubclass.next()) {
                subclasses.add(rsSubclass.getString(3));
            }
            rpgClass.setSubclasses(subclasses);
            this.classes.add(rpgClass);
        }
        while (rsBg.next()) {
            this.backgrounds.add(new Background(rsBg.getString(2)));
        }
        while (rsFeat.next()) {
            Feat feat = new Feat(rsFeat.getString(2));
            feat.setStatsFromString(rsFeat.getString(3));
            this.feats.add(feat);
        }
        stmtProf.close();
        stmtRacial.close();
        stmtClass.close();
        stmtBg.close();
        stmtFeat.close();
        conn.close();
    }

    @Override
    public void create(Proficiency prof) throws SQLException {
        if (!this.profs.contains(prof)) {
            Connection conn = DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "");

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Proficiency "
                    + "(name, type) VALUES (?, ?)");
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
