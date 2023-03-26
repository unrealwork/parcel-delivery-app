package parcel.delivery.app.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A DTO for the {@link Delivery} entity
 */

@Schema(name = "Delivery description",
        accessMode = Schema.AccessMode.READ_ONLY,
        description = "Contains details about delivery of an order"
)
public record DeliveryDto(
        @Schema(description = "Unique order identifier, unique for each delivery as well",
                example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
        )
        UUID orderId, @NotNull String orderedBy,
        @DecimalMin("-180.0") @DecimalMax(value = "180.0", inclusive = false) @Digits(integer = 3, fraction = 6)
        @Schema(description = "Longitude of a coordinates of delivery's location",
                example = "51.523788"
        )
        BigDecimal longitude,
        @DecimalMin("-90.0") @DecimalMax(value = "90.0") @Digits(integer = 3, fraction = 6)
        @Schema(description = "Latitude of a coordinates of delivery's location",
                example = "51.523788"
        )
        BigDecimal latitude,
        @Schema(description = "Status of a delivery process",
                example = "-0.158611"
        )
        @NotNull DeliveryStatus status,
        @Schema(description = """
                Datetime when the delivery details was added
                 """, example = "2023-03-23T18:35:05.785Z")
        Instant createdAt,
        @Schema(description = """
                Datetime when the delivery details were modified last time
                 """, example = "2023-03-23T18:35:05.785Z")
        Instant updatedAt) implements Serializable {
}
