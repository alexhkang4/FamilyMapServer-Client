package Service;

import DataAccess.Database;
import Request.RegisterRequest;
import Response.FillResponse;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FillTest {
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
    public void FillFail() {
        Fill fill = new Fill();
        FillResponse fillResponse = fill.fill("Username", 2);
        assertEquals("Error: There are no user associated with this username.", fillResponse.getMessage());
    }

    @Test
    public void fillPass() {
        RegisterUser registerUser = new RegisterUser();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("hkang3123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
       registerUser.register(registerRequest);
        Fill fill = new Fill();
        FillResponse fillResponse = fill.fill("hkang3", 2);
        assertEquals("Successfully added 7 persons and 25 events to the database.", fillResponse.getMessage());
    }
}
