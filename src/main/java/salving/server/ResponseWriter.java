package salving.server;

import java.io.IOException;

public interface ResponseWriter<T> {
    void write(T data) throws IOException;
}
