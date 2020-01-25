package com.alex.FamilyMap.Data;

import com.alex.FamilyMap.Data.DataCache;

import java.util.ArrayList;
import java.util.List;

import Model.EventM;
import Model.PersonM;

public class Filter {
    private DataCache dataCache;
    private ArrayList<EventM> allEvents;
    private ArrayList<EventM> fatherSide;
    private ArrayList<EventM> motherSide;
    private ArrayList<EventM> femaleEvents;
    private ArrayList<EventM> maleEvents;

    public ArrayList<EventM> getFatherSide() {
        return fatherSide;
    }

    public ArrayList<EventM> getMotherSide() {
        return motherSide;
    }

    public ArrayList<EventM> getFemaleEvents() {
        return femaleEvents;
    }

    public ArrayList<EventM> getMaleEvents() {
        return maleEvents;
    }


    public Filter() {
        dataCache = DataCache.getInstance();
        allEvents = new ArrayList<>(dataCache.getEventMap().values());
        fatherSide = new ArrayList<>();
        motherSide = new ArrayList<>();
        femaleEvents = new ArrayList<>();
        maleEvents = new ArrayList<>();
        filterByFatherMother();
        filterByGender();
    }

    private void filterByFatherMother() {
        PersonM loggedInPerson = dataCache.getPerson(dataCache.getLoggedInPersonID());
        PersonM father = dataCache.getFather(loggedInPerson);
        List<EventM> tempFatherList = dataCache.getAllEventsOfPerson(father);
        fatherSide.addAll(tempFatherList);
        recursiveFatherFilter(father);
        PersonM mother = dataCache.getMother(loggedInPerson);
        List<EventM> tempMotherList = dataCache.getAllEventsOfPerson(mother);
        motherSide.addAll(tempMotherList);
        recursiveMotherFilter(mother);
    }
    private void recursiveFatherFilter(PersonM father) {
        PersonM fatherFather = dataCache.getFather(father);
        PersonM fatherMother = dataCache.getMother(father);
        if (fatherFather == null || fatherMother == null) {
            return;
        }
        for (EventM event : dataCache.getAllEventsOfPerson(fatherFather)) {
            fatherSide.add(event);
        }
        for (EventM event : dataCache.getAllEventsOfPerson(fatherMother)) {
            fatherSide.add(event);
        }
        recursiveFatherFilter(fatherFather);
        recursiveFatherFilter(fatherMother);
    }

    private void recursiveMotherFilter(PersonM mother) {
        PersonM motherFather = dataCache.getFather(mother);
        PersonM motherMother = dataCache.getMother(mother);
        if (motherFather == null || motherMother == null) {
            return;
        }
        for (EventM event : dataCache.getAllEventsOfPerson(motherFather)) {
            motherSide.add(event);
        }
        for (EventM event : dataCache.getAllEventsOfPerson(motherMother)) {
            motherSide.add(event);
        }
        recursiveMotherFilter(motherFather);
        recursiveMotherFilter(motherMother);
    }

    private void filterByGender() {
        for (EventM event : allEvents) {
            PersonM person = dataCache.getPerson(event.getPersonID());
            if (person.getGender().equals("m")) {
                maleEvents.add(event);
            }
            else {
                femaleEvents.add(event);
            }
        }
    }
}
