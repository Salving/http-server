package salving.server;

import java.io.IOException;

public interface RequestReader<T> {
    T read() throws IOException;
}
