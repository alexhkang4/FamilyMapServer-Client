package Handler;

import Request.RegisterRequest;
import Response.LoginRegisterResponse;
import Service.RegisterUser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UserRegisterHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream requestBody = httpExchange.getRequestBody();
                Gson gson = new Gson();
                RegisterRequest registerRequest = gson.fromJson(convertToString(requestBody), RegisterRequest.class);
                requestBody.close();
                RegisterUser registerUser = new RegisterUser();
                LoginRegisterResponse registerResponse = registerUser.register(registerRequest);
                String stringBody;
                stringBody = gson.toJson(registerResponse);
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
