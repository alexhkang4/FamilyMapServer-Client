package Handler;

import Response.FillResponse;
import Service.Fill;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {
                String urlPath = httpExchange.getRequestURI().toString();
                String[] requestData = urlPath.split("/");
                String generations;
                String username;
                if (requestData.length == 3) {
                    generations = "4";
                    username = requestData[2];
                }
                else {
                    username = requestData[2];
                    generations = requestData[3];
                }

                Fill fill = new Fill();
                FillResponse fillResponse = fill.fill(username, Integer.parseInt(generations));
                String stringBody;
                Gson gson = new Gson();
                stringBody = gson.toJson(fillResponse);
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
