package Service;

import DataAccess.*;
import Model.EventM;
import Model.PersonM;
import Model.UserM;
import Request.LoadRequest;
import Response.LoadResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoadTest {
    private Database db;
    private UserM bestUser;
    private UserM bestUser2;
    private UserM bestUser3;
    private PersonM bestPerson;
    private PersonM bestPerson2;
    private PersonM bestPerson3;
    private EventM bestEvent;
    private EventM bestEvent2;
    private EventM bestEvent3;
    ArrayList<UserM> bestUserArray = new ArrayList<>();
    ArrayList<PersonM> bestPersonArray = new ArrayList<>();
    ArrayList<EventM> bestEventArray = new ArrayList<>();
    ArrayList<EventM> wrongArray = new ArrayList<>();
    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new UserM("hkang3", "kanab1!", "alexhkang3@gmail.com",
                "Alex", "Kang", "m", "146343705");
        bestUser2 = new UserM("boba", "iloveboba", "iloveboba11@gmail.com",
                "Boba", "Doe", "f", "345345789");
        bestUser3 = new UserM("teemo37", "bahobo123", "teemo37@gmail.com",
                "Tee", "Mo", "m", "113343222");
        bestUserArray.add(bestUser);
        bestUserArray.add(bestUser2);
        bestUserArray.add(bestUser3);
        bestPerson = new PersonM("146343705", "hkang3", "Alex",
                "Kang", "m", "", "", "");
        bestPerson2 = new PersonM("345345789", "boba", "Boba",
                "Doe", "f", "", "", "");
        bestPerson3 = new PersonM("113343222", "Teemo37", "Tee",
                "Mo", "m", "", "", "");
        bestPersonArray.add(bestPerson);
        bestPersonArray.add(bestPerson2);
        bestPersonArray.add(bestPerson3);
        bestEvent = new EventM("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestEvent2 = new EventM("Shopping_food", "teemo37", "113343222",
                10.55f, 4.3f, "USA", "Provo",
                "Shopping", 2019);
        bestEvent3 = new EventM("Sleeping22", "teemo37", "113343222",
                10.55f, 4.99f, "USA", "Provo",
                "Sleeping", 2019);
        bestEventArray.add(bestEvent);
        bestEventArray.add(bestEvent2);
        bestEventArray.add(bestEvent3);
        wrongArray.add(bestEvent);
        wrongArray.add(bestEvent);
        wrongArray.add(bestEvent);
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
    public void loadPass() {
        Load load = new Load();
        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setEvents(bestEventArray);
        loadRequest.setPersons(bestPersonArray);
        loadRequest.setUsers(bestUserArray);
        LoadResponse loadResponse = load.load(loadRequest);
        assertEquals("Successfully added " + bestUserArray.size() + " users, " + bestPersonArray.size() +
                " persons, and " + bestEventArray.size() + " events to the database.", loadResponse.getMessage());
    }

    @Test
    public void loadFail() {
        Load load = new Load();
        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setEvents(wrongArray);
        loadRequest.setPersons(bestPersonArray);
        loadRequest.setUsers(bestUserArray);
        LoadResponse loadResponse = load.load(loadRequest);
        assertEquals("Error inserting into the Database.", loadResponse.getMessage());

    }
}
