package Handler;

import Request.LoginRequest;
import Response.LoginRegisterResponse;
import Service.Login;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UserLoginHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream requestBody = httpExchange.getRequestBody();
                Gson gson = new Gson();
                LoginRequest loginRequest = gson.fromJson(convertToString(requestBody), LoginRequest.class);
                requestBody.close();
                Login login = new Login();
                LoginRegisterResponse loginResponse = login.login(loginRequest);
                String stringBody = gson.toJson(loginResponse);
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = httpExchange.getResponseBody();
                responseBody.write(stringBody.getBytes());
                responseBody.close();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
