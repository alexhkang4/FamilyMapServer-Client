package Service;

import DataAccess.Database;
import Request.RegisterRequest;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
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
    public void registerFail() {
        RegisterUser registerUser = new RegisterUser();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("hkang3123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
        LoginRegisterResponse registerResponse = registerUser.register(registerRequest);
        registerResponse = registerUser.register(registerRequest);
        assertEquals("Error: Username already taken.", registerResponse.getMessage());
    }

    @Test
    public void registerPass() {
        RegisterUser registerUser = new RegisterUser();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("hkang3123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
        LoginRegisterResponse registerResponse = registerUser.register(registerRequest);
        assertEquals("hkang3", registerResponse.getUserName());
    }
}
