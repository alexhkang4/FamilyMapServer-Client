package Handler;

import Response.AllPersonResponse;
import Response.PersonResponse;
import Service.RetAllPerson;
import Service.RetPerson;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RetPersonHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Headers requestHeaders = httpExchange.getRequestHeaders();
            if (requestHeaders.containsKey("Authorization")) {
                String authToken = requestHeaders.getFirst("Authorization");
                if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                    String urlPath = httpExchange.getRequestURI().toString();
                    String[] requestData = urlPath.split("/");
                    if (requestData.length == 2) {
                        RetAllPerson retAllPerson = new RetAllPerson();
                        Gson gson = new Gson();
                        AllPersonResponse allPersonResponse = retAllPerson.retAllPerson(authToken);
                        String stringBody;
                        stringBody = gson.toJson(allPersonResponse);
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream responseBody = httpExchange.getResponseBody();
                        responseBody.write(stringBody.getBytes());
                        responseBody.close();
                    }
                    else {
                        String personID = requestData[2];
                        RetPerson retPerson = new RetPerson();
                        Gson gson = new Gson();
                        PersonResponse personResponse = retPerson.retPerson(authToken, personID);
                        String stringBody;
                        stringBody = gson.toJson(personResponse);
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream responseBody = httpExchange.getResponseBody();
                        responseBody.write(stringBody.getBytes());
                        responseBody.close();
                    }
                }
                else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    httpExchange.getResponseBody().close();
                }
            }
            else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                httpExchange.getResponseBody().close();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            httpExchange.getResponseBody().close();
        }
    }
}
