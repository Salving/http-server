package salving.server.http;

import java.util.Optional;

public enum HttpVersion {
    FIRST {
        @Override
        public String toString() {
            return "HTTP/1.1";
        }
    }, SECOND {
        @Override
        public String toString() {
            return "HTTP/2";
        }
    }, THIRD {
        @Override
        public String toString() {
            return "HTTP/3";
        }
    };



    public static Optional<HttpVersion> fromString(String string) {
        switch (string) {
            case "HTTP/1.1" -> {
                return Optional.of(FIRST);
            }
            case "HTTP/2" -> {
                return Optional.of(SECOND);
            }
            case "HTTP/3" -> {
                return Optional.of(THIRD);
            }
            default -> {
                return Optional.empty();
            }
        }
    }
}
