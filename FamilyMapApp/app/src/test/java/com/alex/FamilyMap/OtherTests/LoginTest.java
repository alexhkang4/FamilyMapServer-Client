package com.alex.FamilyMap.OtherTests;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import Shared.Model.Login;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void LoginPass() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("123123");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        assertEquals("hkang3", loginResponse.getUserName());
    }

    @Test
    public void LoginFail() {
        ServerProxy serverProxy = new ServerProxy();
        Login loginRequest = new Login();
        loginRequest.setUserName("hkang3");
        loginRequest.setPassword("wrongPassword");
        loginRequest.setServerHost("127.0.1.1");
        loginRequest.setServerPort("8080");
        LoginRegisterResponse loginResponse = serverProxy.login(loginRequest);
        assertNotNull(loginResponse.getMessage());
    }
}
