package Service;

import DataAccess.*;
import Model.EventM;
import Model.PersonM;
import Model.UserM;
import Response.ClearResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;


public class ClearTest {
    private Database db;
    private UserM bestUser;
    private PersonM bestPerson;
    private EventM bestEvent;
    Clear clear = new Clear();
    ClearResponse clearResponse = new ClearResponse();
    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new UserM("hkang3", "kanab1!", "alexhkang3@gmail.com",
                "Alex", "Kang", "m", "146343705");
        bestPerson = new PersonM("146343705", "hkang3", "Alex",
                "Kang", "m", "", "", "");
        bestEvent = new EventM("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void clearPass() {
        UserM compareUserTest = bestUser;
        PersonM comparePersonTest = bestPerson;
        EventM compareEventTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.insertPerson(bestPerson);
            UserDAO userDAO = new UserDAO(conn);
            userDAO.insertUser(bestUser);
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.insertEvent(bestEvent);
            db.closeConnection(true);
            clearResponse = clear.clear();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        assertEquals("Clear succeeded.", clearResponse.getMessage());

    }
}
