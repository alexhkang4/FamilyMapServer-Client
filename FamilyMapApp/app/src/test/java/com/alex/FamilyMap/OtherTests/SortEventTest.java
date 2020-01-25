package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import Shared.Model.EventM;
import Shared.Model.Login;
import Shared.Model.PersonM;
import Shared.Response.AllEventResponse;
import Shared.Response.AllPersonResponse;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SortEventTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void SortEventPass() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.setLoggedInPersonID(loginResponse.getPersonID());
        assertEquals("hkang3", loginResponse.getUserName());
        AllEventResponse allEventResponse = serverProxy.retAllEvent(loginResponse.getAuthToken(), loginRequest);
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson(loginResponse.getAuthToken(), loginRequest);
        dataCache.addToDataCache(allEventResponse);
        dataCache.addToDataCache(allPersonResponse);
        PersonM father = dataCache.getFather(dataCache.getPerson(dataCache.getLoggedInPersonID()));
        List<EventM> eventList = dataCache.getAllEventsOfPerson(father);
        eventList = dataCache.orderEvents(eventList);
        assertEquals("birth", eventList.get(0).getEventType().toLowerCase());
        for(int i = 1; i < eventList.size() - 1; i++) {
            if (!eventList.get(i + 1).getEventType().toLowerCase().equals("death")) {
                assertTrue(eventList.get(i).getYear() >= eventList.get(i + 1).getYear());
            }
        }
        if (eventList.size() > 1) {
            assertEquals("death", eventList.get(eventList.size() - 1).getEventType().toLowerCase());
        }
    }

    @Test
    public void SortEventFail() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.setLoggedInPersonID(loginResponse.getPersonID());
        assertEquals("hkang3", loginResponse.getUserName());
        AllEventResponse allEventResponse = serverProxy.retAllEvent("wrongAuthToken", loginRequest);
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson(loginResponse.getAuthToken(), loginRequest);
        dataCache.addToDataCache(allEventResponse);
        dataCache.addToDataCache(allPersonResponse);
        PersonM father = dataCache.getFather(dataCache.getPerson(dataCache.getLoggedInPersonID()));
        List<EventM> eventList = dataCache.getAllEventsOfPerson(father);
        eventList = dataCache.orderEvents(eventList);
        assertEquals(0, eventList.size());
    }
}
