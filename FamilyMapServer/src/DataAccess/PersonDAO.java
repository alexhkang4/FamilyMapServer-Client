package DataAccess;

import Model.PersonM;

import java.sql.*;
import java.util.ArrayList;

public class PersonDAO {
    private Connection conn;
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertPerson(PersonM myPerson) throws DataAccessException {
        String sql = "INSERT INTO Persons (PersonID, Username, FirstName, LastName, Gender, FatherID, " +
                "MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myPerson.getPersonID());
            stmt.setString(2, myPerson.getAssociatedUsername());
            stmt.setString(3, myPerson.getFirstName());
            stmt.setString(4, myPerson.getLastName());
            stmt.setString(5, myPerson.getGender());
            stmt.setString(6, myPerson.getFatherID());
            stmt.setString(7, myPerson.getMotherID());
            stmt.setString(8, myPerson.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public PersonM findPerson(String personID) throws DataAccessException {
        PersonM person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new PersonM(rs.getString("PersonID"), rs.getString("UserName"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public void clearPerson() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Persons";
            // Execute deletion
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }

    public ArrayList<PersonM> findAllPerson(String username) throws DataAccessException {
        ArrayList<PersonM> personMArrayList = new ArrayList<>();
        PersonM personM;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                personM = new PersonM(rs.getString("personID"), rs.getString("Username"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                personMArrayList.add(personM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return personMArrayList;
    }

    public void removePersonFamily(String username) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting data related to person");
        }
    }
}
