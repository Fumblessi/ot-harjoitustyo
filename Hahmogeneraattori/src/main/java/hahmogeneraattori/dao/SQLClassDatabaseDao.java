/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

import hahmogeneraattori.domain.Proficiency;
import hahmogeneraattori.domain.RpgClass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sampo
 */
public class SQLClassDatabaseDao implements GeneratorDatabaseDao {

    private List<RpgClass> classes;
    private String connection;
    private String user;
    private String password;

    public SQLClassDatabaseDao(String connPath, String user, String pswd) throws SQLException {
        this.connection = connPath;
        this.user = user;
        this.password = pswd;
        this.classes = new ArrayList<>();

        initialize();
    }

    @Override
    public void create(Object obj) throws SQLException {
        RpgClass rpgclass = (RpgClass) obj;

        if (!this.classes.contains(rpgclass)) {
            Connection conn = openConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Class "
                    + "(name) VALUES (?);");
            stmt.setString(1, rpgclass.getName());
            stmt.executeUpdate();
            stmt.close();

            rpgclass.setId(getClassId(rpgclass, conn));

            this.classes.add(rpgclass);

            addClassProficiencies(rpgclass, conn);
            addSubclasses(rpgclass, conn);

            conn.close();
        }
    }

    @Override
    public void update(Object obj) throws SQLException {
        RpgClass rpgclass = (RpgClass) obj;

        Connection conn = openConnection();

        updateClassToClasses(rpgclass);

        PreparedStatement stmt = conn.prepareStatement("UPDATE Class "
                + "SET name = ?, WHERE id = ?;");
        stmt.setString(1, rpgclass.getName());;
        stmt.setInt(2, rpgclass.getId());
        stmt.executeUpdate();
        stmt.close();

        deleteClassProficiencies(rpgclass, conn);
        addClassProficiencies(rpgclass, conn);

        deleteSubclasses(rpgclass, conn);
        addSubclasses(rpgclass, conn);

        conn.close();
    }

    @Override
    public void delete(Object obj) throws SQLException {
        RpgClass rpgclass = (RpgClass) obj;
        Connection conn = openConnection();

        deleteClassProficiencies(rpgclass, conn);
        deleteSubclasses(rpgclass, conn);

        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Class "
                + "WHERE id = ?;");
        stmt.setInt(1, rpgclass.getId());

        stmt.executeUpdate();
        stmt.close();
        conn.close();

        this.classes.remove(rpgclass);
    }

    @Override
    public List list(Class c) {
        return this.classes;
    }

    @Override
    public final void initialize() throws SQLException {
        Connection conn = openConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Class");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            RpgClass rpgclass = new RpgClass(rs.getInt(1), rs.getString(2));

            getSubclasses(rpgclass, conn);
            getClassProficiencies(rpgclass, conn);

            this.classes.add(rpgclass);
        }
        stmt.close();
        conn.close();
    }

    @Override
    public final Connection openConnection() throws SQLException {
        return DriverManager.getConnection(this.connection, this.user, this.password);
    }

    public int getClassId(RpgClass rpgclass, Connection conn) throws SQLException {
        int id = -1;

        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM "
                + "Class WHERE name = ?;");
        stmt.setString(1, rpgclass.getName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        stmt.close();
        return id;
    }

    public void addClassProficiencies(RpgClass rpgclass, Connection conn) throws SQLException {
        for (Proficiency prof : rpgclass.getClassProfs()) {
            PreparedStatement connectProf = conn.prepareStatement("INSERT "
                    + "INTO ClassProficiency (class_id, prof_id) VALUES "
                    + "(?, ?);");
            connectProf.setInt(1, rpgclass.getId());
            connectProf.setInt(2, prof.getId());
            connectProf.executeUpdate();
            connectProf.close();
        }
    }

    public void deleteClassProficiencies(RpgClass rpgclass, Connection conn) throws SQLException {
        PreparedStatement deleteProfs = conn.prepareStatement("DELETE FROM "
                + "ClassProficiency WHERE class_id = ?;");
        deleteProfs.setInt(1, rpgclass.getId());
        deleteProfs.executeUpdate();
        deleteProfs.close();
    }

    public void getClassProficiencies(RpgClass rpgclass, Connection conn) throws SQLException {
        PreparedStatement getProfs = conn.prepareStatement("SELECT * FROM "
                + "Proficiency LEFT JOIN ClassProficiency ON "
                + "ClassProficiency.prof_id = Proficiency.id WHERE "
                + "class_id = ?;");
        getProfs.setInt(1, rpgclass.getId());
        ResultSet rsProfs = getProfs.executeQuery();

        while (rsProfs.next()) {
            rpgclass.addClassProf(new Proficiency(rsProfs.getInt(1), rsProfs.getString(2),
                    rsProfs.getString(3)));
        }
        getProfs.close();
    }

    public void addSubclasses(RpgClass rpgclass, Connection conn) throws SQLException {
        for (String subclass : rpgclass.getSubclasses()) {
            PreparedStatement connectSubclass = conn.prepareStatement("INSERT "
                    + "INTO Subclass (class_id, name) VALUES (?, ?);");
            connectSubclass.setInt(1, rpgclass.getId());
            connectSubclass.setString(2, subclass);
            connectSubclass.executeUpdate();
            connectSubclass.close();
        }
    }

    public void deleteSubclasses(RpgClass rpgclass, Connection conn) throws SQLException {
        PreparedStatement deleteSubclasses = conn.prepareStatement("DELETE FROM "
                + "Subclass WHERE class_id = ?;");
        deleteSubclasses.setInt(1, rpgclass.getId());
        deleteSubclasses.executeUpdate();
        deleteSubclasses.close();
    }

    public void getSubclasses(RpgClass rpgclass, Connection conn) throws SQLException {
        PreparedStatement stmtSubclass = conn.prepareStatement("SELECT * FROM"
                + " SubClass WHERE class_id = ?");
        stmtSubclass.setInt(1, rpgclass.getId());
        ResultSet rsSubclass = stmtSubclass.executeQuery();
        while (rsSubclass.next()) {
            rpgclass.addSubclass(rsSubclass.getString(3));
        }
        stmtSubclass.close();
    }

    public void updateClassToClasses(RpgClass rpgclass) {
        for (RpgClass oldClass : this.classes) {
            if (oldClass.getId() == rpgclass.getId()) {
                oldClass.setName(rpgclass.getName());
                oldClass.setSubclasses(rpgclass.getSubclasses());
                oldClass.setClassProfs(rpgclass.getClassProfs());
                break;
            }
        }
    }
}
