package salving.server.http.io;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import salving.server.http.HttpStatus;
import salving.server.http.HttpVersion;

import java.util.Map;

@Data
@Builder
@RequiredArgsConstructor
public class HttpResponse {
    private final HttpVersion version;
    private final HttpStatus status;
    private final Map<String, String> headers;
    private final byte[] content;
}
