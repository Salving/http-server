package salving.server.http.io;

import salving.server.ResponseWriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseWriter implements ResponseWriter<HttpResponse>, Closeable {
    static final byte[] CRLF = "\r\n".getBytes();
    private final OutputStream outputStream;

    public HttpResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(HttpResponse response) throws IOException {
        writeHead(response);
        writeHeaders(response);
        outputStream.write(CRLF);

        outputStream.write(response.getContent());

        outputStream.write("\r\n\r\n".getBytes());
        outputStream.flush();
    }

    private void writeHead(HttpResponse response) throws IOException {
        byte[] versionBytes = response.getVersion()
                .toString()
                .getBytes();

        byte[] statusMessageBytes = response.getStatus()
                .getMessage()
                .getBytes();

        outputStream.write(versionBytes);
        outputStream.write(" ".getBytes());
        outputStream.write(statusMessageBytes);
        outputStream.write(CRLF);
    }

    private void writeHeaders(HttpResponse response) throws IOException {
        for (var entry : response.getHeaders()
                .entrySet()) {
            String header = entry.getKey();
            String value = entry.getValue();
            outputStream.write(header.getBytes());
            outputStream.write(": ".getBytes());
            outputStream.write(value.getBytes());
            outputStream.write(CRLF);
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
