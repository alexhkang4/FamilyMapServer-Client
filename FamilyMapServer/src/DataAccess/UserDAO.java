package DataAccess;

import Model.UserM;

import java.sql.*;

public class UserDAO {
    private Connection conn;
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertUser(UserM myUser) throws DataAccessException {
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myUser.getUserName());
            stmt.setString(2, myUser.getPassword());
            stmt.setString(3, myUser.getEmail());
            stmt.setString(4, myUser.getFirstName());
            stmt.setString(5, myUser.getLastName());
            stmt.setString(6, myUser.getGender());
            stmt.setString(7, myUser.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public UserM findUser(String username) throws DataAccessException {
        UserM person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new UserM(rs.getString("Username"), rs.getString("Password"), rs.getString("Email"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"), rs.getString("PersonID"));
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

    public void clearUser() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Users";
            // Execute deletion
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}
