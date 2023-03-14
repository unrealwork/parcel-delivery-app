package parcel.delivery.app.common.security.error.model;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int code, String message) {
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), message);
    }
}
