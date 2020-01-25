package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.PersonM;
import Request.RegisterRequest;
import Response.AllPersonResponse;
import Response.LoginRegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetAllPersonTest {
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
    public void RetAllPersonPass() {
        PersonM bestPerson = new PersonM("146343705", "hkang3", "Alex",
                "Kang", "m", "", "", "");
        PersonM bestPerson2 = new PersonM("345345789", "hkang3", "Boba",
                "Doe", "f", "", "", "");
        PersonM bestPerson3 = new PersonM("113343222", "hkang3", "Tee",
                "Mo", "m", "", "", "");
        AllPersonResponse allPersonResponse = new AllPersonResponse();
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
            eDao.insertPerson(bestPerson2);
            eDao.insertPerson(bestPerson3);
            db.closeConnection(true);
            RetAllPerson retAllPerson = new RetAllPerson();
            allPersonResponse = retAllPerson.retAllPerson(registerResponse.getAuthToken());
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        ArrayList<PersonM> personMArrayList = allPersonResponse.getData();
        assertEquals(personMArrayList.size(), 34);
    }
}
