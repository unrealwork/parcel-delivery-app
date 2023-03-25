package parcel.delivery.app.delivery.controller.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Assign courier request",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        description = "Performs assignment of a delivery to courier")
public record AssignCourierRequest(@NotNull @Email
                                   @Schema(description = "Unique identifier of courier's user")
                                   String courierId) {
}
