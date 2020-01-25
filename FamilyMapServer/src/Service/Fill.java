package Service;

import DataAccess.*;
import GenerateData.Generator;
import GenerateData.Locations;
import Model.EventM;
import Model.PersonM;
import Model.UserM;
import Response.FillResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;

public class Fill {
    public FillResponse fill(String username, int generations) {
        Connection conn;
        Database db = new Database();
        FillResponse fillResponse = new FillResponse();
        try {
            conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            UserM userM = userDAO.findUser(username);
            if (userM == null) {
                fillResponse.setMessage("Error: There are no user associated with this username.");
            }
            else if (generations < 0) {
                fillResponse.setMessage("Error: Generations need to bea non-negative number.");
            }
            else {
                PersonDAO personDAO = new PersonDAO(conn);
                PersonM personM = personDAO.findPerson(userM.getPersonID());
                String personID = personM.getPersonID();
                String associatedUsername = personM.getAssociatedUsername();
                String firstName = personM.getFirstName();
                String lastName = personM.getLastName();
                String gender = personM.getGender();
                personDAO.removePersonFamily(personM.getAssociatedUsername());
                EventDAO eventDAO = new EventDAO(conn);
                eventDAO.removeEventFamily(personM.getAssociatedUsername());
                personM = new PersonM(personID, associatedUsername, firstName, lastName, gender, null, null, null);
                String fatherID = UUID.randomUUID().toString().substring(0 ,7);
                String motherID = UUID.randomUUID().toString().substring(0 ,7);
                personM.setFatherID(fatherID);
                personM.setMotherID(motherID);
                personDAO.insertPerson(personM);

                Generator generator = new Generator();
                Locations locations = generator.generateLocation();
                int birthYear = 1995;
                EventM eventM = generateBirth(personM, locations, birthYear);
                eventDAO.insertEvent(eventM);
                generateParents(personM, conn, 1995, 0, generations, fatherID, motherID);
                int numPeople = 1;
                int index = 1;
                for (int i = 0; i < generations; i++) {
                    index *= 2;
                    numPeople += index;
                }
                int numEvents = 1;
                for (int i = 1; i < generations; i++) {
                    index *= 6;
                    numEvents += index;
                }
                fillResponse.setMessage("Successfully added "  + numPeople + " persons and " + numEvents + " events to the database.");
            }
            db.closeConnection(true);
        } catch (DataAccessException | IOException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return fillResponse;
    }

    private EventM generateBirth(PersonM personM, Locations locations, int birthYear) {
        int year = birthYear;
        String eventID = UUID.randomUUID().toString().substring(0 ,7);
        String eventType = "Birth";
        EventM eventM = new EventM(eventID, personM.getAssociatedUsername(), personM.getPersonID(), locations.getLatitude(),
                                    locations.getLongitude(), locations.getCountry(), locations.getCity(), eventType, year);
        return eventM;
    }

    private EventM generateDeath(PersonM personM, Locations locations, int death) {
        int year = death;
        String eventID = UUID.randomUUID().toString().substring(0 ,7);
        String eventType = "Death";
        EventM eventM = new EventM(eventID, personM.getAssociatedUsername(), personM.getPersonID(), locations.getLatitude(),
                locations.getLongitude(), locations.getCountry(), locations.getCity(), eventType, year);
        return eventM;
    }

    private EventM generateMarriage(PersonM personM, Locations locations, int marriageYear) {
        int year = marriageYear;
        String eventID = UUID.randomUUID().toString().substring(0 ,7);
        String eventType = "Marriage";
        EventM eventM = new EventM(eventID, personM.getAssociatedUsername(), personM.getPersonID(), locations.getLatitude(),
                locations.getLongitude(), locations.getCountry(), locations.getCity(), eventType, year);
        return eventM;

    }

    private PersonM generateFemalePerson(String username) throws IOException, DataAccessException {
        Generator generator = new Generator();
        String femaleName = generator.generateFemaleName();
        String lastName = generator.generateSirName();
        String personID = UUID.randomUUID().toString().substring(0 ,7);
        String gender = "f";
        PersonM personM = new PersonM(personID, username, femaleName, lastName, gender, null, null ,null);
        return personM;
    }

    private PersonM generateMalePerson(String username) throws IOException, DataAccessException {
        Generator generator = new Generator();
        String maleName = generator.generateMaleName();
        String lastName = generator.generateSirName();
        String personID = UUID.randomUUID().toString().substring(0 ,7);
        String gender = "m";
        PersonM personM = new PersonM(personID, username, maleName, lastName, gender, null, null, null);
        return personM;
    }

    private void generateParents(PersonM personM, Connection conn, int birthYear, int lvl, int generations, String fatherID, String motherID) throws IOException, DataAccessException {
        if (lvl >= generations) {
            return;
        }
        birthYear = birthYear - 20;
        PersonM mother = generateFemalePerson(personM.getAssociatedUsername());
        mother.setPersonID(motherID);
        PersonM father = generateMalePerson(personM.getAssociatedUsername());
        father.setPersonID(fatherID);
        Generator generator = new Generator();
        Locations locations = generator.generateLocation();
        EventM birthDateMom = generateBirth(mother, locations, birthYear);
        EventM birthDateDad = generateBirth(father, locations, birthYear);
        int marriageYear = birthYear + 20;
        EventM marriageDateMom = generateMarriage(mother, locations, marriageYear);
        EventM marriageDateDad = generateMarriage(father, locations, marriageYear);
        int deathYear = marriageYear + 20;
        EventM deathDateMom = generateDeath(mother, locations, deathYear);
        EventM deathDateDad = generateDeath(father, locations, deathYear);
        mother.setSpouseID(father.getPersonID());
        father.setSpouseID(mother.getPersonID());

        String fatherSFather = UUID.randomUUID().toString().substring(0 ,7);
        String fatherSMother = UUID.randomUUID().toString().substring(0 ,7);
        String motherSFather = UUID.randomUUID().toString().substring(0 ,7);
        String motherSMother = UUID.randomUUID().toString().substring(0 ,7);
        if (lvl <= generations - 2) {
            father.setFatherID(fatherSFather);
            father.setMotherID(fatherSMother);
            mother.setFatherID(motherSFather);
            mother.setMotherID(motherSMother);
        }

        PersonDAO personDAO = new PersonDAO(conn);
        personDAO.insertPerson(father);
        personDAO.insertPerson(mother);

        EventDAO eventDAO = new EventDAO(conn);
        eventDAO.insertEvent(birthDateMom);
        eventDAO.insertEvent(birthDateDad);
        eventDAO.insertEvent(marriageDateMom);
        eventDAO.insertEvent(marriageDateDad);
        eventDAO.insertEvent(deathDateMom);
        eventDAO.insertEvent(deathDateDad);

        lvl++;

        generateParents(mother, conn, birthYear, lvl, generations, motherSFather, motherSMother);
        generateParents(father, conn, birthYear, lvl, generations, fatherSFather, fatherSMother);
    }
}
