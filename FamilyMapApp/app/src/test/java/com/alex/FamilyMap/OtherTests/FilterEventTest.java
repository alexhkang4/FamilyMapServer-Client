package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Data.Filter;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


import Shared.Model.EventM;

import Shared.Model.Login;
import Shared.Response.AllEventResponse;
import Shared.Response.AllPersonResponse;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;


public class FilterEventTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void FilterEventPass() {
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

        Filter filter = new Filter();
        ArrayList<EventM> fatherEvents = filter.getFatherSide();
        dataCache.removeFromEventList(fatherEvents);
        assertEquals(46, dataCache.getEventList().size());
        dataCache.addEventList(fatherEvents);

        ArrayList<EventM> motherEvents = filter.getMotherSide();
        dataCache.removeFromEventList(motherEvents);
        assertEquals(46, dataCache.getEventList().size());
        dataCache.addEventList(motherEvents);

        ArrayList<EventM> maleEvents = filter.getMaleEvents();
        dataCache.removeFromEventList(maleEvents);
        assertEquals(45, dataCache.getEventList().size());
        dataCache.addEventList(maleEvents);

        ArrayList<EventM> femaleEvents = filter.getFemaleEvents();
        dataCache.removeFromEventList(femaleEvents);
        assertEquals(46, dataCache.getEventList().size());
        dataCache.addEventList(femaleEvents);
    }

    @Test
    public void FilterEventFail() {
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
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson("wrongAuthToken", loginRequest);
        dataCache.addToDataCache(allEventResponse);
        dataCache.addToDataCache(allPersonResponse);

        Filter filter = new Filter();
        ArrayList<EventM> fatherEvents = filter.getFatherSide();
        dataCache.removeFromEventList(fatherEvents);
        assertEquals(0,dataCache.getEventList().size());
        dataCache.addEventList(fatherEvents);

        ArrayList<EventM> motherEvents = filter.getMotherSide();
        dataCache.removeFromEventList(motherEvents);
        assertEquals(0,dataCache.getEventList().size());
        dataCache.addEventList(motherEvents);

        ArrayList<EventM> maleEvents = filter.getMaleEvents();
        dataCache.removeFromEventList(maleEvents);
        assertEquals(0,dataCache.getEventList().size());
        dataCache.addEventList(maleEvents);

        ArrayList<EventM> femaleEvents = filter.getFemaleEvents();
        dataCache.removeFromEventList(femaleEvents);
        assertEquals(0,dataCache.getEventList().size());
        dataCache.addEventList(femaleEvents);
    }
}
