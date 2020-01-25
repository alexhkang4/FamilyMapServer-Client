package DataAccess;

import Model.EventM;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
    private Connection conn;
    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertEvent(EventM myEvent) throws DataAccessException {
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myEvent.getEventID());
            stmt.setString(2, myEvent.getAssociatedUsername());
            stmt.setString(3, myEvent.getPersonID());
            stmt.setDouble(4, myEvent.getLatitude());
            stmt.setDouble(5, myEvent.getLongitude());
            stmt.setString(6, myEvent.getCountry());
            stmt.setString(7, myEvent.getCity());
            stmt.setString(8, myEvent.getEventType());
            stmt.setInt(9, myEvent.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public EventM findEvent(String eventID) throws DataAccessException {
        EventM event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new EventM(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getDouble("Latitude"), rs.getDouble("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
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

    public void clearEvent() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Events";
            // Execute deletion
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }

    public ArrayList<EventM> findAllEvent(String username) throws DataAccessException {
        ArrayList<EventM> eventMArrayList = new ArrayList<>();
        EventM event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new EventM(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getDouble("Latitude"), rs.getDouble("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventMArrayList.add(event);
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
        return eventMArrayList;
    }

    public void removeEventFamily(String username) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting data related to events");
        }
    }
}
