package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthTokenM;
import Model.PersonM;
import Response.PersonResponse;

import java.sql.Connection;

public class RetPerson {
    public PersonResponse retPerson(String authToken, String personID) {
        Database db = new Database();
        Connection conn;
        PersonResponse personResponse = new PersonResponse();
        try {
            conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            AuthTokenM authTokenM = authTokenDAO.findAuthToken(authToken);
            if (authTokenM == null) {
                personResponse.setMessage("Error: Invalid auth token.");
            }
            else {
                PersonDAO personDAO = new PersonDAO(conn);
                PersonM personM = personDAO.findPerson(personID);
                if (personM == null) {
                    personResponse.setMessage("Error: PersonID does not exist.");
                }
                else if (!personM.getAssociatedUsername().equals(authTokenM.getUsername())) {
                    personResponse.setMessage("Error: PersonID does not exist.");
                }
                else {
                    personResponse.setAssociatedUsername(personM.getAssociatedUsername());
                    personResponse.setPersonID(personM.getPersonID());
                    personResponse.setFirstName(personM.getFirstName());
                    personResponse.setLastName(personM.getLastName());
                    personResponse.setGender(personM.getGender());
                    personResponse.setFatherID(personM.getFatherID());
                    personResponse.setMotherID(personM.getMotherID());
                    personResponse.setSpouseID(personM.getSpouseID());
                }
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            personResponse.setMessage("Error: Invalid parameters.");
        }
        return personResponse;
    }
}
