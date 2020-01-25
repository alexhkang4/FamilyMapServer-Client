package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.AuthTokenM;
import Model.EventM;
import Response.EventResponse;

import java.sql.Connection;

public class RetEvent {
    public EventResponse retEvent(String authToken, String eventID) {
        Database db = new Database();
        Connection conn;
        EventResponse eventResponse = new EventResponse();
        try {
            conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            AuthTokenM authTokenM = authTokenDAO.findAuthToken(authToken);
            if (authTokenM == null) {
                eventResponse.setMessage("Error: Invalid auth token.");
                db.closeConnection(true);
            }
            else {
                EventDAO eventDAO = new EventDAO(conn);
                EventM eventM = eventDAO.findEvent(eventID);
                if (!eventM.getAssociatedUsername().equals(authTokenM.getUsername())) {
                    eventResponse.setMessage("Error: This user has no authorization.");
                    db.closeConnection(true);
                }
                else if (eventM == null) {
                    eventResponse.setMessage("Error: EventID does not exist.");
                    db.closeConnection(true);
                }
                else {
                    eventResponse.setAssociatedUsername(eventM.getAssociatedUsername());
                    eventResponse.setEventID(eventM.getEventID());
                    eventResponse.setPersonID(eventM.getPersonID());
                    eventResponse.setLatitude(eventM.getLatitude());
                    eventResponse.setLongitude(eventM.getLongitude());
                    eventResponse.setCountry(eventM.getCountry());
                    eventResponse.setCity(eventM.getCity());
                    eventResponse.setEventType(eventM.getEventType());
                    eventResponse.setYear(eventM.getYear());
                    db.closeConnection(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            eventResponse.setMessage("Error: Invalid parameters.");
        }
        return eventResponse;
    }
}