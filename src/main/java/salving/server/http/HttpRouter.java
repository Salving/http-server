package salving.server.http;

import lombok.Data;
import salving.server.http.io.HttpRequest;
import salving.server.http.io.HttpRequestHandler;
import salving.server.http.io.HttpResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpRouter implements HttpRequestHandler {
    private final List<Route> routes;

    public HttpRouter() {
        this.routes = new LinkedList<>();
    }

    public HttpRouter(HttpRequestHandler defaultHandler) {
        this();
        routes.add(new Route(defaultHandler, request -> true));
    }

    @Override
    public HttpResponse accept(HttpRequest request) {
        System.out.println("Handling request: "+ request.getUri());

        for (Route route : routes) {
            if (route.match(request)) return route.handle(request);
        }

        HttpResponse fallbackResponse = new HttpResponse(request.getVersion(), HttpStatus.of(404), Map.of(),
                "<h1>404 Not found</h1>".getBytes());

        return fallbackResponse;
    }

    public void route(Route route) {
        routes.add(0, route);
    }

    public void route(HttpRequestMatcher matcher, HttpRequestHandler handler) {
        route(new Route(handler, matcher));
    }

    public void route(String path, HttpRequestHandler handler) {
        PathHttpRequestMatcher matcher = new PathHttpRequestMatcher(path);
        route(new Route(handler, matcher));
    }

    @Data
    public static class Route {
        private final HttpRequestHandler handler;
        private final HttpRequestMatcher matcher;

        public boolean match(HttpRequest request) {
            return matcher.match(request);
        }

        public HttpResponse handle(HttpRequest request) {
            return handler.accept(request);
        }
    }


    public interface HttpRequestMatcher {
        boolean match(HttpRequest request);
    }

    public static class PathHttpRequestMatcher implements HttpRequestMatcher {
        private final String path;

        public PathHttpRequestMatcher(String path) {
            this.path = path;
        }

        @Override
        public boolean match(HttpRequest request) {
            return Objects.equals(request.getUri(), path);
        }
    }
}
