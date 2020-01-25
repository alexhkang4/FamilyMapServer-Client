package Request;

import Model.EventM;
import Model.PersonM;
import Model.UserM;

import java.util.ArrayList;

public class LoadRequest {
    private ArrayList<UserM> users;
    private ArrayList<PersonM> persons;
    private ArrayList<EventM> events;

    public ArrayList<UserM> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<UserM> users) {
        this.users = users;
    }
    public ArrayList<PersonM> getPersons() {
        return persons;
    }
    public void setPersons(ArrayList<PersonM> persons) {
        this.persons = persons;
    }
    public ArrayList<EventM> getEvents() {
        return events;
    }
    public void setEvents(ArrayList<EventM> events) {
        this.events = events;
    }
}
