package DataAccess;

import Model.AuthTokenM;

import java.sql.*;

public class AuthTokenDAO {
    private Connection conn;
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertAuthToken(AuthTokenM authTokenM) throws DataAccessException {
        String sql = "INSERT INTO AuthTokens (Token, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authTokenM.getAuthToken());
            stmt.setString(2, authTokenM.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public AuthTokenM findAuthToken(String authToken) throws DataAccessException {
        AuthTokenM authTokenM;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authTokenM = new AuthTokenM( rs.getString("Token"), rs.getString("Username"));
                return authTokenM;
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

    public void clearAuthToken() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM AuthTokens";
            // Execute deletion
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}
