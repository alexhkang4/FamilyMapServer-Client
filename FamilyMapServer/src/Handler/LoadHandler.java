package Handler;

import Request.LoadRequest;
import Response.LoadResponse;
import Service.Load;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream requestBody = httpExchange.getRequestBody();
                Gson gson = new Gson();
                LoadRequest loadRequest = gson.fromJson(convertToString(requestBody), LoadRequest.class);
                requestBody.close();
                Load load = new Load();
                LoadResponse loadResponse = load.load(loadRequest);
                String stringBody;
                stringBody = gson.toJson(loadResponse);
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
