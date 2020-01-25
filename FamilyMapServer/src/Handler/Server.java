package Handler;

import DataAccess.DataAccessException;
import DataAccess.Database;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void  main(String args[]) {
        int port = 8080;
        try {
            startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        Database database = new Database();
        try {
            database.openConnection();
            database.createTables();
            database.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        server.start();
    }
    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
        server.createContext("/user/register", new UserRegisterHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new RetPersonHandler());
        server.createContext("/user/login", new UserLoginHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/event", new RetEventHandler());
    }
}
