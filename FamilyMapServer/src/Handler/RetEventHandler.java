package Handler;

import Response.AllEventResponse;
import Response.EventResponse;
import Service.RetAllEvent;
import Service.RetEvent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RetEventHandler extends HandlerParent implements HttpHandler {
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
                        RetAllEvent retAllEvent = new RetAllEvent();
                        Gson gson = new Gson();
                        AllEventResponse allEventResponse = retAllEvent.retAllEvent(authToken);
                        String stringBody;
                        stringBody = gson.toJson(allEventResponse);
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream responseBody = httpExchange.getResponseBody();
                        responseBody.write(stringBody.getBytes());
                        responseBody.close();
                    }
                    else {
                        String eventID = requestData[2];
                        RetEvent retEvent = new RetEvent();
                        Gson gson = new Gson();
                        EventResponse eventResponse = retEvent.retEvent(authToken, eventID);
                        String stringBody;
                        stringBody = gson.toJson(eventResponse);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
