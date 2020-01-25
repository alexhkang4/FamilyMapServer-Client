package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.EventM;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetEventTest {
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
    public void RetEventPass() {
        EventM bestEvent = new EventM("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        EventResponse eventResponse = new EventResponse();
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
            EventDAO eDao = new EventDAO(conn);
            bestEvent.setAssociatedUsername(registerResponse.getUserName());
            eDao.insertEvent(bestEvent);
            String eventID = bestEvent.getEventID();
            RetEvent retEvent = new RetEvent();
            db.closeConnection(true);
            eventResponse = retEvent.retEvent(registerResponse.getAuthToken(), eventID);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        assertEquals(bestEvent.getEventID(), eventResponse.getEventID());
        assertEquals(bestEvent.getYear(), eventResponse.getYear());
        assertEquals(bestEvent.getEventType(), eventResponse.getEventType());
    }

    @Test
    public void retEventFail() {
        EventM bestEvent = new EventM("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        EventResponse eventResponse = new EventResponse();
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
            EventDAO eDao = new EventDAO(conn);
            eDao.insertEvent(bestEvent);
            RetEvent retEvent = new RetEvent();
            db.closeConnection(true);
            eventResponse = retEvent.retEvent(registerResponse.getAuthToken(), "someinvalidID");
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        assertEquals("Error: Invalid parameters.", eventResponse.getMessage());
    }
}
