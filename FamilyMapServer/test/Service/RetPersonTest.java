package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.PersonM;
import Request.RegisterRequest;
import Response.LoginRegisterResponse;
import Response.PersonResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetPersonTest {
    Database db;
    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void RetPersonPass() {
        PersonM bestPerson = new PersonM("146343705", "hkang3", "Alex",
                "Kang", "m", "", "", "");
        PersonResponse personResponse = new PersonResponse();
        try {
            RegisterUser registerUser = new RegisterUser();
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUserName("hkang3");
            registerRequest.setPassword("hkang3123");
            registerRequest.setEmail("hkang3@gmail.com");
            registerRequest.setFirstName("Alex");
            registerRequest.setLastName("Kang");
            registerRequest.setGender("m");
            LoginRegisterResponse registerResponse = registerUser.register(registerRequest);
            Connection conn = db.openConnection();
            PersonDAO eDao = new PersonDAO(conn);
            eDao.insertPerson(bestPerson);
            String personID = bestPerson.getPersonID();
            RetPerson retPerson = new RetPerson();
            db.closeConnection(true);
            personResponse = retPerson.retPerson(registerResponse.getAuthToken(), personID);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        assertEquals(bestPerson.getPersonID(), personResponse.getPersonID());
        assertEquals(bestPerson.getGender(), personResponse.getGender());
        assertEquals(bestPerson.getAssociatedUsername(), personResponse.getAssociatedUsername());
    }

    @Test
    public void RetPersonFail() {
        PersonM bestPerson = new PersonM("146343705", "hkang3", "Alex",
                "Kang", "m", "", "", "");
        PersonResponse personResponse = new PersonResponse();
        try {
            RegisterUser registerUser = new RegisterUser();
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUserName("hkang3");
            registerRequest.setPassword("hkang3123");
            registerRequest.setEmail("hkang3@gmail.com");
            registerRequest.setFirstName("Alex");
            registerRequest.setLastName("Kang");
            registerRequest.setGender("m");
            LoginRegisterResponse registerResponse = registerUser.register(registerRequest);
            Connection conn = db.openConnection();
            PersonDAO eDao = new PersonDAO(conn);
            eDao.insertPerson(bestPerson);
            RetPerson retPerson = new RetPerson();
            db.closeConnection(true);
            personResponse = retPerson.retPerson(registerResponse.getAuthToken(), "somerandomid");
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        assertEquals("Error: PersonID does not exist.", personResponse.getMessage());
    }
}
