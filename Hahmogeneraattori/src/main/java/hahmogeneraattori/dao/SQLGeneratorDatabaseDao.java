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

        Connection conn = openConnection();

        initializeProfs(conn);
        initializeRacials(conn);
        initializeClasses(conn);
        initializeBackgrounds(conn);
        initializeFeats(conn);

        conn.close();
    }

    @Override
    public void create(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            createProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            //createRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            //createClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            //createBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            //createFeat(feat, conn);
        }
        conn.close();
    }

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

    @Override
    public void update(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            updateProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            //createRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            //createClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            //createBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            //createFeat(feat, conn);
        }
        conn.close();
    }

    public void updateProf(Proficiency newProf, Connection conn) throws SQLException {
        if (!this.profs.contains(newProf)) {
            int profId = newProf.getId();
            List<Proficiency> oldList = this.profs;
            for (Proficiency prof : oldList) {
                if (prof.getId() == profId) {
                    this.profs.remove(prof);
                    this.profs.add(newProf);
                }
            }

            PreparedStatement stmt = conn.prepareStatement("UPDATE Proficiency "
                    + "SET name = ?, type = ? WHERE id = ?;");
            stmt.setString(1, newProf.getName());
            stmt.setString(2, newProf.getType());
            stmt.setInt(3, newProf.getId());

            stmt.executeUpdate();
            stmt.close();

        } else {
            deleteProf(newProf, conn);
        }
    }

    @Override
    public void delete(Object obj) throws SQLException {
        Connection conn = openConnection();

        if (obj.getClass() == Proficiency.class) {
            Proficiency prof = (Proficiency) obj;
            deleteProf(prof, conn);
        } else if (obj.getClass() == Racial.class) {
            Racial racial = (Racial) obj;
            //deleteRacial(racial, conn);
        } else if (obj.getClass() == RpgClass.class) {
            RpgClass rpgclass = (RpgClass) obj;
            //deleteClass(rpgclass, conn);
        } else if (obj.getClass() == Background.class) {
            Background bg = (Background) obj;
            //deleteBackground(bg, conn);
        } else if (obj.getClass() == Feat.class) {
            Feat feat = (Feat) obj;
            //deleteFeat(feat, conn);
        }
        conn.close();
    }

    public void deleteProf(Proficiency prof, Connection conn) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Proficiency "
                + "WHERE id = ?;");
        stmt.setInt(1, prof.getId());
        
        stmt.executeUpdate();
        stmt.close();

        this.profs.remove(prof);
    }

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

    public List<Proficiency> listProfs() {
        return this.profs;
    }

    public List<Racial> listRacials() {
        return this.racials;
    }

    public List<RpgClass> listClasses() {
        return this.classes;
    }

    public List<Background> listBackgrounds() {
        return this.backgrounds;
    }

    public List<Feat> listFeats() {
        return this.feats;
    }

    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:./generatordb", "sa", "");
    }

    public final void initializeProfs(Connection conn) throws SQLException {
        PreparedStatement stmtProf = conn.prepareStatement("SELECT * FROM Proficiency");
        ResultSet rsProf = stmtProf.executeQuery();
        while (rsProf.next()) {
            this.profs.add(new Proficiency(rsProf.getInt(1), rsProf.getString(2),
                    rsProf.getString(3)));
        }
        stmtProf.close();
    }

    public final void initializeRacials(Connection conn) throws SQLException {
        PreparedStatement stmtRacial = conn.prepareStatement("SELECT * FROM Racial");
        ResultSet rsRacial = stmtRacial.executeQuery();
        while (rsRacial.next()) {
            this.racials.add(new Racial(rsRacial.getInt(1), rsRacial.getString(2)));
        }
        stmtRacial.close();
    }

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

    public final void initializeBackgrounds(Connection conn) throws SQLException {
        PreparedStatement stmtBg = conn.prepareStatement("SELECT * FROM Background");
        ResultSet rsBg = stmtBg.executeQuery();
        while (rsBg.next()) {
            this.backgrounds.add(new Background(rsBg.getInt(1), rsBg.getString(2)));
        }
        stmtBg.close();
    }

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
}
