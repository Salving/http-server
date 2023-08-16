package salving.server.http.io;

import lombok.Builder;
import lombok.Data;
import salving.server.http.HttpVersion;

import java.util.Map;

@Data
@Builder
public class HttpRequest {
    private final HttpRequestMethod method;
    private final String uri;
    private final HttpVersion version;
    private final String host;
    private final Map<String, String> headers;
}
