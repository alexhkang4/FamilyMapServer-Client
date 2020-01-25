package Service;

import DataAccess.Database;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
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
    public void loginFail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("hkang3123");
        Login login = new Login();
        LoginRegisterResponse loginResponse = login.login(loginRequest);
        assertEquals("Error: Username does not exist.", loginResponse.getMessage());
        RegisterUser registerUser = new RegisterUser();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("hkang3123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
        registerUser.register(registerRequest);
        loginRequest.setPassword("idontknow");
        loginResponse = login.login(loginRequest);
        assertEquals("Error: Password does not match.", loginResponse.getMessage());
    }

    @Test
    public void loginPass() {
        RegisterUser registerUser = new RegisterUser();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("hkang3123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
        registerUser.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("hkang3123");
        Login login = new Login();
        LoginRegisterResponse loginResponse = login.login(loginRequest);
        assertEquals("hkang3", loginResponse.getUserName());
    }
}
