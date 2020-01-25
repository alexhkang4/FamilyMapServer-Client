package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.EventM;
import Request.RegisterRequest;
import Response.AllEventResponse;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetAllEventTest {
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
    public void RetAllEventPass() {
        EventM bestEvent = new EventM("Biking_123A", "hkang3", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        EventM bestEvent2 = new EventM("Shopping_food", "hkang3", "113343222",
                10.55f, 4.3f, "USA", "Provo",
                "Shopping", 2019);
        EventM bestEvent3 = new EventM("Sleeping22", "hkang3", "113343222",
                10.55f, 4.99f, "USA", "Provo",
        "Sleeping", 2019);
        AllEventResponse allEventResponse = new AllEventResponse();
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
            eDao.insertEvent(bestEvent2);
            eDao.insertEvent(bestEvent3);
            db.closeConnection(true);
            RetAllEvent retAllEvent = new RetAllEvent();
            allEventResponse = retAllEvent.retAllEvent(registerResponse.getAuthToken());
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        ArrayList<EventM> eventMArrayList = allEventResponse.getData();
        assertEquals(eventMArrayList.size(), 94);
    }

    @Test
    public void RetAllEventFail() {
        EventM bestEvent = new EventM("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        EventM bestEvent2 = new EventM("Shopping_food", "teemo37", "113343222",
                10.55f, 4.3f, "USA", "Provo",
                "Shopping", 2019);
        EventM bestEvent3 = new EventM("Sleeping22", "teemo37", "113343222",
                10.55f, 4.99f, "USA", "Provo",
                "Sleeping", 2019);
        AllEventResponse allEventResponse = new AllEventResponse();
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
            eDao.insertEvent(bestEvent2);
            eDao.insertEvent(bestEvent3);
            db.closeConnection(true);
            RetAllEvent retAllEvent = new RetAllEvent();
            allEventResponse = retAllEvent.retAllEvent("someramdomauthToken");
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        assertEquals("Error: Invalid auth token.", allEventResponse.getMessage());
    }
}
