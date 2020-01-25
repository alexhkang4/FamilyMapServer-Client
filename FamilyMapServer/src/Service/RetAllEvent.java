package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.AuthTokenM;
import Model.EventM;
import Response.AllEventResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class RetAllEvent {
    public AllEventResponse retAllEvent(String authToken) {
        Database db = new Database();
        Connection conn;
        AllEventResponse allEventResponse = new AllEventResponse();
        try {
            conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            AuthTokenM authTokenM = authTokenDAO.findAuthToken(authToken);
            if (authTokenM == null) {
                allEventResponse.setMessage("Error: Invalid auth token.");
            }
            else {
                EventDAO eventDAO = new EventDAO(conn);
                ArrayList<EventM> eventArray = eventDAO.findAllEvent(authTokenM.getUsername());
                allEventResponse.setData(eventArray);
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            allEventResponse.setMessage("Error: Invalid parameters.");
        }
        return allEventResponse;
    }

}
