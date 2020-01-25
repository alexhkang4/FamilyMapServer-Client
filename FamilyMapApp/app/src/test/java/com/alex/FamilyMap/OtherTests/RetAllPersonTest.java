package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import Shared.Model.Login;
import Shared.Response.AllPersonResponse;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class RetAllPersonTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void retAllPersonPass() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        assertEquals("hkang3", loginResponse.getUserName());
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson(loginResponse.getAuthToken(), loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.addToDataCache(allPersonResponse);
        assertEquals(31, dataCache.getPersonMap().size());
    }

    @Test
    public void retAllPersonFail() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        assertEquals("hkang3", loginResponse.getUserName());
        AllPersonResponse allPersonResponse = serverProxy.retAllPerson("wrongAuthToken", loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.addToDataCache(allPersonResponse);
        assertEquals(0,dataCache.getPersonMap().size());
    }
}
