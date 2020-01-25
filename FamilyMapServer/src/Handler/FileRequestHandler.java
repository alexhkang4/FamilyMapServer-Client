package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileRequestHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                String urlPath = httpExchange.getRequestURI().toString();
                if (urlPath.equals(null) || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String filePath = "web" + urlPath;
                File file = new File(filePath);
                if (file.exists()) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseBody = httpExchange.getResponseBody();
                    Files.copy(file.toPath(), responseBody);
                    responseBody.close();
                } else {
                    //404 error
                    String errorPath = "web/HTML/404.html";
                    Path path = FileSystems.getDefault().getPath(errorPath);
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseBody = httpExchange.getResponseBody();
                    Files.copy(path, responseBody);
                    responseBody.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
