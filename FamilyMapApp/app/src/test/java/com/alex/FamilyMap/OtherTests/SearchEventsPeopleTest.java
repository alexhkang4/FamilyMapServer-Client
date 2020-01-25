package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import Shared.Model.Login;
import Shared.Model.PersonM;
import Shared.Response.AllEventResponse;
import Shared.Response.AllPersonResponse;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class SearchEventsPeopleTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void searchEventsPeoplePass() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.setLoggedInPersonID(loginResponse.getPersonID());

        AllEventResponse allEventResponse = serverProxy.retAllEvent(loginResponse.getAuthToken(), loginRequest);
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson(loginResponse.getAuthToken(), loginRequest);
        dataCache.addToDataCache(allEventResponse);
        dataCache.addToDataCache(allPersonResponse);
        PersonM person = dataCache.getPerson(loginResponse.getPersonID());
        assertNotNull(person);
        assertNotNull(dataCache.getFather(person));
        assertNotNull(dataCache.getMother(person));
        assertNotNull(dataCache.getSpouse(dataCache.getFather(person)));
        assertEquals(3, dataCache.getAllEventsOfPerson(dataCache.getFather(person)).size());
    }

    @Test
    public void searchEventsPeopleFail() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.setLoggedInPersonID(loginResponse.getPersonID());

        AllEventResponse allEventResponse = serverProxy.retAllEvent("wrongAuthToken", loginRequest);
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson(loginResponse.getAuthToken(), loginRequest);
        dataCache.addToDataCache(allEventResponse);
        dataCache.addToDataCache(allPersonResponse);
        PersonM person = dataCache.getPerson("wrongPersonID");
        assertNull(dataCache.getFather(person));
        assertNull(dataCache.getMother(person));
        assertNull(dataCache.getSpouse(person));
        assertNull(person);
        assertEquals(0,dataCache.getEventMap().size());

    }
}
