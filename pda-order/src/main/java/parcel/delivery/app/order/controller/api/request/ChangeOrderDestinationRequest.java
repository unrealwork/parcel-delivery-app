package parcel.delivery.app.order.controller.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Change order's destination request",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        description = "Data required for changing destination of the order")
public record ChangeOrderDestinationRequest(@NotBlank
                                            @Schema(description = "New destination of the order",
                                                    example = "20 W 34th St., New York, NY 10001, USA")
                                            String destination) {
}
