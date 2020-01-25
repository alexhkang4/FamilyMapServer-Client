package com.alex.FamilyMap.Data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.EventM;
import Model.PersonM;
import Response.AllEventResponse;
import Response.AllPersonResponse;

public class DataCache {
    private String loggedInPersonID;
    private Map<String, PersonM> personMap;
    private Map<String, EventM> eventMap;
    private ArrayList<EventM> eventList;
    private boolean life_story_toggle = true;
    private boolean family_tree_lines = true;
    private boolean spouse_lines = true;
    private boolean father_events = true;
    private boolean mother_events = true;
    private boolean male_events = true;
    private boolean female_events = true;
    private boolean isDataCacheEmpty = false;

    public DataCache() {
        personMap = new HashMap<>();
        eventMap = new HashMap<>();
        eventList = new ArrayList<>();
        loggedInPersonID = null;
    }
    private static DataCache instance;

    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }


        return instance;
    }
    public void addToDataCache(AllPersonResponse allPersonResponse) {
        isDataCacheEmpty = false;
        ArrayList<PersonM> personList = allPersonResponse.getData();
        if (personList == null) {
            return;
        }
        for(PersonM person : personList) {
            personMap.put(person.getPersonID(), person);
        }
    }

    public void addToDataCache(AllEventResponse allEventResponse) {
        isDataCacheEmpty = false;
        ArrayList<EventM> eventList = allEventResponse.getData();
        if (eventList == null) {
            return;
        }
        for(EventM event : eventList) {
            eventMap.put(event.getEventID(), event);
        }
        this.eventList = new ArrayList<>(eventMap.values());
    }
    public void removeFromEventList(ArrayList<EventM> newEventList) {
        ArrayList<EventM> tempEventList = new ArrayList<>(eventMap.values());
        for (EventM event : tempEventList) {
            for (EventM otherEvent : newEventList) {
                if (event.getEventID().equals(otherEvent.getEventID())) {
                    eventList.remove(event);
                }
            }
        }
    }
    public List<EventM> getEventList() {
        return eventList;
    }
    public void addEventList(ArrayList<EventM> newEventList) {
        ArrayList<EventM> tempEventList = new ArrayList<>(eventMap.values());
        for (EventM event : tempEventList) {
            for (EventM otherEvent : newEventList) {
                if (event.getEventID().equals(otherEvent.getEventID())) {
                    eventList.add(event);
                }
            }
        }
    }

    public PersonM getSpouse(PersonM thisPerson) {
        if (thisPerson == null) {
            return null;
        }
        for (PersonM person : personMap.values()) {
            if (thisPerson.getSpouseID() != null) {
                if (thisPerson.getSpouseID().equals(person.getPersonID())) {
                    return person;
                }
            }
        }
        return null;
    }

    public PersonM getFather(PersonM thisPerson) {
        if (thisPerson == null) {
            return null;
        }
        for (PersonM person : personMap.values()) {
            if (thisPerson.getFatherID() != null) {
                if (thisPerson.getFatherID().equals(person.getPersonID())) {
                    return person;
                }
            }
        }
        return null;
    }

    public PersonM getMother(PersonM thisPerson) {
        if (thisPerson == null) {
            return null;
        }
        for (PersonM person : personMap.values()) {
            if (thisPerson.getMotherID() != null) {
                if (thisPerson.getMotherID().equals(person.getPersonID())) {
                    return person;
                }
            }
        }
        return null;
    }

    public PersonM getChild(PersonM thisPerson) {
        if (thisPerson == null) {
            return null;
        }
        for (PersonM person : personMap.values()) {
            if (thisPerson.getPersonID().equals(person.getMotherID()) ||
                thisPerson.getPersonID().equals(person.getFatherID())) {
                return person;
            }
        }
        return null;
    }

    public List<EventM> getAllEventsOfPerson(PersonM person) {
        List<EventM> retList = new ArrayList<>();
        for (EventM event : eventList){
            if (event.getPersonID().equals(person.getPersonID())) {
                retList.add(event);
            }
        }
        retList = orderEvents(retList);
        return retList;
    }

    public List<PersonM> getAllPersonOfPerson(PersonM person) {
        List<PersonM> retList = new ArrayList<>();
        if (getFather(person) != null) {
            retList.add(getFather(person));
        }
        if (getMother(person) != null) {
            retList.add(getMother(person));
        }
        if (getSpouse(person) != null) {
            retList.add(getSpouse(person));
        }
        if (getChild(person) != null) {
            retList.add(getChild(person));
        }
        return retList;
    }

    public void clearDatabase() {
        loggedInPersonID = null;
        personMap.clear();
        eventMap.clear();
        eventList.clear();
        life_story_toggle = true;
        family_tree_lines = true;
        spouse_lines = true;
        father_events = true;
        mother_events = true;
        male_events = true;
        female_events = true;
        isDataCacheEmpty = true;
    }

    public List<EventM> orderEvents(List<EventM> eventList) {
        EventM birth = null;
        EventM death = null;
        List<EventM> tempEventList = new ArrayList<>(eventList);
        for (EventM event : eventList) {
            if(event.getEventType().toLowerCase().equals("birth")) {
                birth = event;
                tempEventList.remove(event);
            }
            else if (event.getEventType().toLowerCase().equals("death")) {
                death = event;
                tempEventList.remove(event);
            }
        }
        for (int i = 0; i < tempEventList.size(); i++) {
            for (int j = i + 1; j < tempEventList.size(); j++) {
                if (tempEventList.get(i).getYear() > tempEventList.get(j).getYear()) {
                    EventM temp = tempEventList.get(i);
                    tempEventList.set(i, tempEventList.get(j));
                    tempEventList.set(j, temp);
                }
            }
        }
        for (int i = 0; i < tempEventList.size(); i++) {
            for (int j = i + 1; j < tempEventList.size(); j++) {
                if (tempEventList.get(i).getYear() == tempEventList.get(j).getYear()) {
                    if (tempEventList.get(i).getEventType().compareTo(tempEventList.get(j).getEventType()) > 0) {
                        EventM temp = tempEventList.get(i);
                        tempEventList.set(i, tempEventList.get(j));
                        tempEventList.set(j, temp);
                    }
                }
            }
        }
        eventList.clear();
        if (birth != null) {
            eventList.add(birth);
        }
        eventList.addAll(tempEventList);
        if (death != null) {
            eventList.add(death);
        }
        return eventList;
    }

    public String getLoggedInPersonID() {
        return loggedInPersonID;
    }

    public void setLoggedInPersonID(String loggedInPersonID) {
        this.loggedInPersonID = loggedInPersonID;
    }

    public boolean isDataCacheEmpty() {
        return isDataCacheEmpty;
    }

    public PersonM getPerson(String personID) {
        return personMap.get(personID);
    }

    public EventM getEvent(String eventID) {
        return eventMap.get(eventID);
    }

    public Map<String, PersonM> getPersonMap() {
        return personMap;
    }

    public Map<String, EventM> getEventMap() {
        return eventMap;
    }

    public boolean isFamily_tree_lines() {
        return family_tree_lines;
    }

    public void setFamily_tree_lines(boolean family_tree_lines) {
        this.family_tree_lines = family_tree_lines;
    }

    public boolean isSpouse_lines() {
        return spouse_lines;
    }

    public void setSpouse_lines(boolean spouse_lines) {
        this.spouse_lines = spouse_lines;
    }

    public boolean isFather_events() {
        return father_events;
    }

    public void setFather_events(boolean father_events) {
        this.father_events = father_events;
    }

    public boolean isMother_events() {
        return mother_events;
    }

    public void setMother_events(boolean mother_events) {
        this.mother_events = mother_events;
    }

    public boolean isMale_events() {
        return male_events;
    }

    public void setMale_events(boolean male_events) {
        this.male_events = male_events;
    }

    public boolean isFemale_events() {
        return female_events;
    }

    public void setFemale_events(boolean female_events) {
        this.female_events = female_events;
    }

    public boolean isLife_story_toggle() {
        return life_story_toggle;
    }

    public void setLife_story_toggle(boolean life_story_toggle) {
        this.life_story_toggle = life_story_toggle;
    }
}
