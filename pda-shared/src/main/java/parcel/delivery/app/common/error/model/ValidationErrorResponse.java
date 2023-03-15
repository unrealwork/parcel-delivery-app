package parcel.delivery.app.common.error.model;

import java.util.Map;

public record ValidationErrorResponse(int id, String message, Map<String, String> fieldErrors) {
    private static final String DEFAULT_MESSAGE = "Request is not valid";

    public ValidationErrorResponse(int id, Map<String, String> fieldErrors) {
        this(id, DEFAULT_MESSAGE, fieldErrors);
    }
}
