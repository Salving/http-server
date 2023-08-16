package salving;

import salving.server.http.HttpServer;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer("public");
        server.start(8080);
    }
}
