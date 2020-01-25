package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.AuthTokenM;
import Model.UserM;
import Request.LoginRequest;
import Response.LoginRegisterResponse;

import java.sql.Connection;
import java.util.UUID;

public class Login {
    boolean isSuccess = false;
    Connection conn;
    public LoginRegisterResponse login(LoginRequest loginRequest) {
        String authToken = UUID.randomUUID().toString().substring(0 ,7);
        Database db = new Database();
        LoginRegisterResponse loginResponse = new LoginRegisterResponse();
        try {
            conn = db.openConnection();
            AuthTokenM authTokenM = new AuthTokenM(authToken, loginRequest.getUserName());
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            authTokenDAO.insertAuthToken(authTokenM);
            UserDAO userDAO = new UserDAO(conn);
            UserM userM = userDAO.findUser(loginRequest.getUserName());
            if (userM == null) {
                loginResponse.setMessage("Error: Username does not exist.");
            }
            else if (!(userM.getPassword().equals(loginRequest.getPassword()))) {
                loginResponse.setMessage("Error: Password does not match.");
            }
            else {
                loginResponse.setAuthToken(authToken);
                loginResponse.setUserName(loginRequest.getUserName());
                loginResponse.setPersonID(userM.getPersonID());
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
        }
        return loginResponse;
    }

}
