package DataAccess;

import Model.AuthTokenM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthTokenDAOTest {
    Database db;
    AuthTokenM bestAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestAuthToken = new AuthTokenM("82795880-0ea6-4bcc-96ce-ec0c28f649f1", "hkang3");
        //and make sure to initialize our tables since we don't know if our database files exist yet
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
    public void insertPass() throws Exception {
        AuthTokenM compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.insertAuthToken(bestAuthToken);
            compareTest = eDao.findAuthToken(bestAuthToken.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            eDao.insertAuthToken(bestAuthToken);
            eDao.insertAuthToken(bestAuthToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        AuthTokenM compareTest = bestAuthToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO eDao = new AuthTokenDAO(conn);
            compareTest = eDao.findAuthToken(bestAuthToken.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }
}
