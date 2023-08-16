package salving.server.http;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;
import lombok.Setter;
import salving.server.http.io.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.Executors;

public class HttpServer {
    private final ListeningExecutorService executor;
    @Getter
    @Setter
    private HttpRouter router;

    public HttpServer() {
        this(new HttpRouter());
    }

    public HttpServer(String path) {
        this(new HttpRouter(new HttpRequestSimpleFileHandler(Path.of(path))));
    }

    public HttpServer(HttpRouter router) {
        executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors()));

        this.router = router;
    }

    public void start(int port) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Started http server");

                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("accepted socket");

                    ListenableFuture<?> task = executor.submit(() -> {
                        try {
                            handleClient(client);
                            client.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    task.addListener(() -> {
                        try {
                            if (!client.isClosed()) client.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, executor);

                }
            } catch (IOException e) {
                System.out.println("Server stopped with exception");
                throw new RuntimeException(e);
            }
        }, "Socket-Listener-Thread").start();
    }

    private void handleClient(Socket client) throws IOException {
        HttpRequestReader requestReader = new HttpRequestReader(client.getInputStream());
        HttpRequest request = requestReader.read();

        HttpResponse response = router.accept(request);

        HttpResponseWriter responseWriter = new HttpResponseWriter(client.getOutputStream());
        responseWriter.write(response);
    }
}
