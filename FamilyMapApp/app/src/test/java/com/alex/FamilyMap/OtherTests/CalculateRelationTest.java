package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import Shared.Model.Login;
import Shared.Response.AllEventResponse;
import Shared.Response.AllPersonResponse;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class CalculateRelationTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void calculateRelationPass() {
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
        assertEquals(31, dataCache.getPersonMap().size());
        assertEquals(91, dataCache.getEventMap().size());
    }

    @Test
    public void calculateRelationFail() {
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
        assertEquals(0,dataCache.getPersonMap().size());
        assertEquals(0,dataCache.getEventMap().size());
    }
}
