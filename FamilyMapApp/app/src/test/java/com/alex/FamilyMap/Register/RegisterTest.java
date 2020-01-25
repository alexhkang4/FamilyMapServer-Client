package com.alex.FamilyMap.Register;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Proxy.ServerProxy;

import org.junit.Before;
import org.junit.Test;

import Shared.Model.Login;
import Shared.Response.LoginRegisterResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RegisterTest {
    @Before
    public void setUp() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clearDatabase();
    }

    @Test
    public void registerPass() {
        ServerProxy serverProxy = new ServerProxy();
        Login registerRequest = new Login();
        registerRequest.setUserName("hkang4");
        registerRequest.setPassword("123123");
        registerRequest.setEmail("hkang4@gmail.com");
        registerRequest.setFirstName("Alexis");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("f");
        registerRequest.setServerHost("127.0.1.1");
        registerRequest.setServerPort("8080");
        LoginRegisterResponse registerResponse = serverProxy.register(registerRequest);
        assertEquals("hkang4", registerResponse.getUserName());
    }

    @Test
    public void registerFail() {
        ServerProxy serverProxy = new ServerProxy();
        Login registerRequest = new Login();
        registerRequest.setUserName("hkang3");
        registerRequest.setPassword("123123");
        registerRequest.setEmail("hkang3@gmail.com");
        registerRequest.setFirstName("Alex");
        registerRequest.setLastName("Kang");
        registerRequest.setGender("m");
        registerRequest.setServerHost("127.0.1.1");
        registerRequest.setServerPort("8080");
        LoginRegisterResponse registerResponse = serverProxy.register(registerRequest);
        assertNotNull(registerResponse.getMessage());
    }
}
