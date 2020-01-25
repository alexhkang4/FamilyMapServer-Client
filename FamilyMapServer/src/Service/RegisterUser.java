package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.PersonM;
import Model.UserM;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginRegisterResponse;

import java.sql.Connection;
import java.util.UUID;

public class RegisterUser {
    boolean isSuccess = false;
    Connection conn;
    public LoginRegisterResponse register (RegisterRequest registerRequest) {
        Database db = new Database();
        LoginRegisterResponse registerResponse = new LoginRegisterResponse();
        try {
            conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            UserM userM = userDAO.findUser(registerRequest.getUserName());
            if (userM != null) {
                registerResponse.setMessage("Error: Username already taken.");
            }
            else if (!registerRequest.getGender().equals("f") && !registerRequest.getGender().equals("m")) {
                registerResponse.setMessage("Error: Gender must be f or m.");
            }
            else {
                String personID = UUID.randomUUID().toString().substring(0 ,7);
                userM = new UserM(registerRequest.getUserName(), registerRequest.getPassword() , registerRequest.getEmail(),
                        registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getGender(), personID);
                userDAO.insertUser(userM);
                PersonM personM = new PersonM(personID, registerRequest.getUserName(), registerRequest.getFirstName(),
                        registerRequest.getLastName(), registerRequest.getGender(), null, null, null);
                PersonDAO personDAO = new PersonDAO(conn);
                personDAO.insertPerson(personM);
                Login login = new Login();
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUserName(registerRequest.getUserName());
                loginRequest.setPassword(registerRequest.getPassword());
                db.closeConnection(true);
                registerResponse = login.login(loginRequest);
                Fill fill = new Fill();
                fill.fill(registerRequest.getUserName(), 4);
                db.openConnection();
                isSuccess = true;
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            registerResponse.setMessage("Error: Invalid input.");
        }
        return registerResponse;
    }

}