package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;
    public boolean isSuccess = false;
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }
    public void closeConnection(boolean commit) throws DataAccessException {
        if (conn == null) return;
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sqlEvent = "CREATE TABLE IF NOT EXISTS Events " +
                    "(" +
                    "EventID text not null unique, " +
                    "AssociatedUsername text not null, " +
                    "PersonID text not null, " +
                    "Latitude double not null, " +
                    "Longitude double not null, " +
                    "Country text not null, " +
                    "City text not null, " +
                    "EventType text not null, " +
                    "Year int not null, " +
                    "primary key (EventID), " +
                    "foreign key (AssociatedUsername) references Users(Username), " +
                    "foreign key (PersonID) references Persons(PersonID)" +
                    ")";
            String sqlPerson = "CREATE TABLE IF NOT EXISTS Persons " +
                    "(" +
                    "PersonID text not null unique, " +
                    "Username text not null, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Gender text not null, " +
                    "FatherID text, " +
                    "MotherID text, " +
                    "SpouseID text, " +
                    "primary key (PersonID), " +
                    "foreign key (Username) references Users(Username)" +
                    ")";
            String sqlAuthTokens = "CREATE TABLE IF NOT EXISTS AuthTokens " +
                    "(" +
                    "Token text not null unique, " +
                    "Username text not null, " +
                    "primary key (Token), " +
                    "foreign key (Username) references Users(Username)" +
                    ")";
            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users " +
                    "(" +
                    "Username text not null unique, " +
                    "Password text not null, " +
                    "Email text not null, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Gender text not null, " +
                    "PersonID text not null, " +
                    "primary key (Username), " +
                    "foreign key (PersonID) references Persons(PersonID)" +
                    ")";
            stmt.executeUpdate(sqlEvent);
            stmt.executeUpdate(sqlPerson);
            stmt.executeUpdate(sqlAuthTokens);
            stmt.executeUpdate(sqlUsers);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    public void clearTables() throws DataAccessException
    {
        Connection conn = this.openConnection();
        PersonDAO personDAO = new PersonDAO(conn);
        EventDAO eventDAO = new EventDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        UserDAO userDAO = new UserDAO(conn);
        personDAO.clearPerson();
        eventDAO.clearEvent();
        authTokenDAO.clearAuthToken();
        userDAO.clearUser();
        this.closeConnection(true);
        isSuccess = true;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}

