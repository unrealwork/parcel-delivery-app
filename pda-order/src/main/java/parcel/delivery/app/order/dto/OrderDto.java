package parcel.delivery.app.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.order.domain.Order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A DTO for the {@link Order} entity
 */
@Builder
@Schema(accessMode = Schema.AccessMode.READ_ONLY,
        name = "Order description",
        description = "Describes user's order")
public record OrderDto(@Schema(description = """
        Unique identifier of the order
         """, example = "4843a6c6-28e7-4a85-8f8d-f7e343389350")
                       UUID id,

                       @Schema(description = """
                               Some additional notes about the order
                                """, example = "Call 15 minutes before")
                       @NotBlank String description,
                       @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer = 3, fraction = 3)
                       @Schema(description = """
                               Weight of a parcel in kg. Allowed fractional digit count 3.
                                """, example = "1.22")
                       BigDecimal weight,
                       @NotNull
                       @Schema(description = """
                               Status of the order
                                """, example = "ACCEPTED")
                       OrderStatus status,

                       @Schema(description = """
                               Email of the user that created the order
                                """, example = "john@doe.com")
                       @Email @NotNull String createdBy,


                       @Schema(description = """
                               Email of the courier who was assigned to deliver the order
                                """, example = "jack@doe.com")
                       @Email String assignedTo,

                       @Schema(description = """
                               Destination of the order. Address of a location.
                                """, example = "221b Baker St, London NW1 6XE, United Kingdom")
                       @NotBlank String destination,
                       @Schema(description = """
                               Datetime when the order was created
                                """, example = "2023-03-23T18:35:05.785Z")
                       Instant createdAt,

                       @Schema(description = """
                               Datetime when the order was updated
                                """, example = "2023-03-23T18:35:05.785Z")
                       Instant updatedAt) implements Serializable {
}
