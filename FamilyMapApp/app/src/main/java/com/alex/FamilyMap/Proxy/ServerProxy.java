package com.alex.FamilyMap.Proxy;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import Model.Login;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.AllEventResponse;
import Response.AllPersonResponse;
import Response.LoginRegisterResponse;

public class ServerProxy {
    public LoginRegisterResponse register(Login mLoginModel) {
        String urlStr = "http://" + mLoginModel.getServerHost() + ":" + mLoginModel.getServerPort() + "/user/register";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUserName(mLoginModel.getUserName());
            registerRequest.setPassword(mLoginModel.getPassword());
            registerRequest.setEmail(mLoginModel.getEmail());
            registerRequest.setGender(mLoginModel.getGender());
            registerRequest.setFirstName(mLoginModel.getFirstName());
            registerRequest.setLastName(mLoginModel.getLastName());
            Gson gson = new Gson();
            String stringBody = gson.toJson(registerRequest);
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(stringBody.getBytes());
            requestBody.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String responseBodyStr = convertToString(responseBody);
                LoginRegisterResponse registerResponse = gson.fromJson(responseBodyStr, LoginRegisterResponse.class);
                return registerResponse;
            }
            else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LoginRegisterResponse login(Login mLoginModel) {
        String urlStr = "http://" + mLoginModel.getServerHost() + ":" + mLoginModel.getServerPort() + "/user/login";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserName(mLoginModel.getUserName());
            loginRequest.setPassword(mLoginModel.getPassword());
            Gson gson = new Gson();
            String stringBody = gson.toJson(loginRequest);
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(stringBody.getBytes());
            requestBody.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String responseBodyStr = convertToString(responseBody);
                LoginRegisterResponse loginResponse = gson.fromJson(responseBodyStr, LoginRegisterResponse.class);
                return loginResponse;
            }
            else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AllPersonResponse retAllPerson(String authToken, Login mLoginModel) {
        String urlStr = "http://" + mLoginModel.getServerHost() + ":" + mLoginModel.getServerPort() + "/person";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authToken);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String responseBodyStr = convertToString(responseBody);
                Gson gson = new Gson();
                AllPersonResponse allPersonResponse = gson.fromJson(responseBodyStr, AllPersonResponse.class);
                return allPersonResponse;
            }
            else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AllEventResponse retAllEvent(String authToken, Login mLoginModel) {
        String urlStr = "http://" + mLoginModel.getServerHost() + ":" + mLoginModel.getServerPort() + "/event";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authToken);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String responseBodyStr = convertToString(responseBody);
                Gson gson = new Gson();
                AllEventResponse allEventResponse = gson.fromJson(responseBodyStr, AllEventResponse.class);
                return allEventResponse;
            }
            else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    String convertToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }
}
