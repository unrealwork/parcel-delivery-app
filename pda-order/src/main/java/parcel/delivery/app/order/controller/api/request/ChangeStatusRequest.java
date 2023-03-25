package parcel.delivery.app.order.controller.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.common.domain.OrderStatus;

@Schema(name = "Change order's status request",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        description = "Allows to change order's status"
)
public record ChangeStatusRequest(
        @Schema(description = "New status of the order",
                example = "ACCEPTED")
        @NotNull OrderStatus status) {
}
