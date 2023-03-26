package parcel.delivery.app.common.error.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(name = "Error response",
        accessMode = Schema.AccessMode.READ_ONLY,
        description = "Contain details of a error happened during request"
)
public record ErrorResponse(
        @Schema(description = "Code of the error. Usually corresponds to Http Status code",
                example = "403"
        )
        int code,
        @Schema(description = "Describes reason of the error",
                example = "Access denied to the API endpoint without VIEW_COURIERS privilege"
        )
        String message) {

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), message);
    }
}
