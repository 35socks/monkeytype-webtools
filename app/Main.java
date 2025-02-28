import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 9974;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new RequestHandler());
        server.setExecutor(null);

        System.out.println("Server started on port " + port);
        server.start();
    }

    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            // Handle CORS preflight request
            if (method.equalsIgnoreCase("OPTIONS")) {
                sendCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1); // No content response
                return;
            }

            // Read request body
            Scanner scanner = new Scanner(exchange.getRequestBody(), StandardCharsets.UTF_8);
            String body = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();

            // Print request details
            System.out.println("Received Request:");
            System.out.println("Method: " + method);
            System.out.println("Path: " + exchange.getRequestURI());
            System.out.println("Body: " + body);
            System.out.println("-----------------------");

            // Send response
            String response = "Request received!";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            sendCorsHeaders(exchange); // Ensure CORS headers are always sent
            exchange.sendResponseHeaders(200, response.length());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }

        private void sendCorsHeaders(HttpExchange exchange) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        }
    }
}