package salving.server.http.io;

public interface HttpRequestHandler {
    HttpResponse accept(HttpRequest request);
}
