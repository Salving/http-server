package salving.server.http;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface HttpStatus {
    static HttpStatus of(int statusCode) {
        for (Informational status : Informational.values()) {
            if (status.getStatusCode() == statusCode) return status;
        }

        for (Success status : Success.values()) {
            if (status.getStatusCode() == statusCode) return status;
        }

        for (Redirection status : Redirection.values()) {
            if (status.getStatusCode() == statusCode) return status;
        }

        for (ClientError status : ClientError.values()) {
            if (status.getStatusCode() == statusCode) return status;
        }

        for (ServerError status : ServerError.values()) {
            if (status.getStatusCode() == statusCode) return status;
        }

        return null;
    }

    int getStatusCode();
    String getMessage();

    @Getter
    enum Informational implements HttpStatus {
        CONTINUE(100),
        SWITCHING_PROTOCOLS(101);

        private final int statusCode;

        Informational(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String getMessage() {
            return "%d %s".formatted(this.statusCode, this.toString());
        }
    }

    @Getter
    enum Success implements HttpStatus {
        OK(200),
        CREATED(201),
        ACCEPTED(202),
        NON_AUTHORITATIVE_INFORMATION(203),
        NO_CONTENT(204),
        RESET_CONTENT(205),
        PARTIAL_CONTENT(206);

        private final int statusCode;

        Success(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String getMessage() {
            return "%d %s".formatted(this.statusCode, this.toString());
        }
    }

    @Getter
    enum Redirection implements HttpStatus {
        MULTIPLE_CHOICES(300),
        MOVED_PERMANENTLY(301),
        FOUND(302),
        SEE_OTHER(303),
        NOT_MODIFIED(304),
        USE_PROXY(305),
        UNUSED(306),
        TEMPORARY_REDIRECT(307);

        private final int statusCode;

        Redirection(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String getMessage() {
            return "%d %s".formatted(this.statusCode, this.toString());
        }
    }

    @Getter
    enum ClientError implements HttpStatus {
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        PAYMENT_REQUIRED(402),
        FORBIDDEN(403),
        NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405),
        NOT_ACCEPTABLE(406),
        PROXY_AUTHENTICATION_REQUIRED(407),
        REQUEST_TIMEOUT(408),
        CONFLICT(409),
        GONE(410),
        LENGTH_REQUIRED(411),
        PRECONDITION_FAILED(412),
        REQUEST_ENTITY_TOO_LARGE(413),
        REQUEST_URL_TOO_LONG(414),
        UNSUPPORTED_MEDIA_TYPE(415),
        REQUESTED_RANGE_NOT_SATISFIABLE(416),
        EXPECTATION_FAILED(417);

        private final int statusCode;

        ClientError(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String getMessage() {
            return "%d %s".formatted(this.statusCode, this.toString());
        }
    }

    @Getter
    enum ServerError implements HttpStatus {
        INTERNAL_SERVER_ERROR(500),
        NOT_IMPLEMENTED(501),
        BAD_GATEWAY(502),
        SERVICE_UNAVAILABLE(503),
        GATEWAY_TIMEOUT(504),
        HTTP_VERSION_NOT_SUPPORTED(505);

        private final int statusCode;

        ServerError(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String getMessage() {
            return "%d %s".formatted(this.statusCode, this.toString());
        }
    }
}
