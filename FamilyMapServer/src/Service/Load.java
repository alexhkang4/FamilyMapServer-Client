package Service;

import DataAccess.*;
import Model.EventM;
import Model.PersonM;
import Model.UserM;
import Request.LoadRequest;
import Response.LoadResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class Load {
    public LoadResponse load(LoadRequest loadRequest) {
        Database db = new Database();
        Clear clear = new Clear();
        clear.clear();
        ArrayList<UserM> userMArrayList = loadRequest.getUsers();
        ArrayList<PersonM> personMArrayList = loadRequest.getPersons();
        ArrayList<EventM> eventMArrayList = loadRequest.getEvents();
        LoadResponse loadResponse = new LoadResponse();
        boolean isSuccess = false;

        try {
            Connection conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            for (UserM user : userMArrayList) {
                userDAO.insertUser(user);
            }
            PersonDAO personDAO = new PersonDAO(conn);
            for (PersonM person : personMArrayList) {
                personDAO.insertPerson(person);
            }
            EventDAO eventDAO = new EventDAO(conn);
            for (EventM event : eventMArrayList) {
                eventDAO.insertEvent(event);
            }
            db.closeConnection(true);
            isSuccess = true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            loadResponse.setMessage("Error inserting into the Database.");
        }
        if (isSuccess == true) {
            loadResponse.setMessage("Successfully added " + userMArrayList.size() + " users, " + personMArrayList.size() +
                    " persons, and " + eventMArrayList.size() + " events to the database.");
        }
        return loadResponse;
    }
}
