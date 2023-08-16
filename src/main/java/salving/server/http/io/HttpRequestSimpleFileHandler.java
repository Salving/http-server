package salving.server.http.io;

import lombok.Getter;
import lombok.Setter;
import salving.server.http.HttpStatus;
import salving.server.http.HttpVersion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class HttpRequestSimpleFileHandler implements HttpRequestHandler {

    @Getter
    @Setter
    private Path folder;

    public HttpRequestSimpleFileHandler(Path folder) {
        this.folder = folder;
    }

    public HttpRequestSimpleFileHandler() {
        this(Path.of("/"));
    }

    @Override
    public HttpResponse accept(HttpRequest request) {
        Path filePath = getFilePath(request.getUri());

        try {
            if (Files.exists(filePath)) {
                return buildFileResponse(filePath);
            } else if (Files.exists(getFilePath("404.html"))) {
                return buildFileResponse(getFilePath("404.html"));
            }
        } catch (IOException e) {
            System.out.println("Exception while dispatching file: " + request.getUri());
        }

        return new HttpResponse(request.getVersion(), HttpStatus.of(404), Map.of(),
                "<h1>404 Not found</h1>".getBytes());
    }

    private HttpResponse buildFileResponse(Path filePath) throws IOException {
        String contentType;
        contentType = guessContentType(filePath);
        byte[] content = Files.readAllBytes(filePath);

        Map<String, String> headers = Map.of("Content-type", contentType, "Content-length",
                String.valueOf(content.length));

        return HttpResponse.builder()
                .version(HttpVersion.FIRST)
                .status(HttpStatus.Success.OK)
                .headers(headers)
                .content(content)
                .build();
    }

    private Path getFilePath(String path) {
        if (path.equals("/")) {
            path = "index.html";
        } else if (path.startsWith("/")) {
            path = path.replaceFirst("/", "");
        }

        return folder.resolve(path);
    }

    private String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }
}
