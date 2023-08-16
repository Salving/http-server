package salving.server.http.io;

import salving.server.RequestReader;
import salving.server.http.HttpVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestReader implements RequestReader<HttpRequest> {
    private final InputStream inputStream;

    public HttpRequestReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public HttpRequest read() throws IOException {
        String request = readRequest();

        String[] requestLines = request.split("\r\n");
        String[] requestLine = requestLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestLines[1].split(" ")[1];

        Map<String, String> headers = parseHeaders(requestLines);

        return HttpRequest.builder()
                .method(HttpRequestMethod.valueOf(method))
                .version(HttpVersion.fromString(version)
                        .orElseThrow(() -> new IOException("Invalid HTTP version")))
                .host(host)
                .uri(path)
                .headers(headers)
                .build();
    }

    private String readRequest() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line)
                    .append("\r\n");
        }

        return requestBuilder.toString();
    }

    private Map<String, String> parseHeaders(String[] requestLines) {
        Map<String, String> headers = new HashMap<>();
        for (int h = 2; h < requestLines.length; h++) {
            String[] header = requestLines[h].split(": ");
            String name = header[0];
            String value = header[1];
            headers.put(name, value);
        }
        return headers;
    }
}
