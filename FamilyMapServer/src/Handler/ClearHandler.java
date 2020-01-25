package Handler;

import Response.ClearResponse;
import Service.Clear;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                Clear clear = new Clear();
                ClearResponse clearResponse = clear.clear();
                String stringBody;
                Gson gson = new Gson();
                stringBody = gson.toJson(clearResponse);
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
