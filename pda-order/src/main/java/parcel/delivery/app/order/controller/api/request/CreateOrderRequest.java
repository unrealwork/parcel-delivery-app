package parcel.delivery.app.order.controller.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Schema(name = "Create order request",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        description = "Data that should be provided for order creation")
public record CreateOrderRequest(
        @NotBlank
        @Schema(description = "Some additional notes for consideration",
                example = "Call 15 minute before")
        String description,
        @NotBlank
        @Schema(description = """
                Destination of the order. Address of a location.
                 """, example = "221b Baker St, London NW1 6XE, United Kingdom")
        String destination,
        @NotNull @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer = 3, fraction = 3)
        @Schema(description = """
                Weight of a parcel in kg. Allowed fractional digit count 3.
                 """, example = "1.22")
        BigDecimal weight) implements Serializable {
}
