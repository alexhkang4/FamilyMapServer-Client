package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthTokenM;
import Model.PersonM;
import Response.AllPersonResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class RetAllPerson {
    public AllPersonResponse retAllPerson(String authToken) {
        Database db = new Database();
        Connection conn;
        AllPersonResponse allPersonResponse = new AllPersonResponse();
        try {
            conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            AuthTokenM authTokenM = authTokenDAO.findAuthToken(authToken);
            if (authTokenM == null) {
                allPersonResponse.setMessage("Error: Invalid auth token.");
            }
            else {
                PersonDAO personDAO = new PersonDAO(conn);
                ArrayList<PersonM> personArray = personDAO.findAllPerson(authTokenM.getUsername());
                allPersonResponse.setData(personArray);
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            allPersonResponse.setMessage("Error: Invalid parameters.");
        }
        return allPersonResponse;
    }
}
