package parcel.delivery.app.delivery.dto;

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
public record DeliveryDto(UUID orderId, @NotNull String orderedBy,
                          @DecimalMin("-180.0") @DecimalMax(value = "180.0", inclusive = false) @Digits(integer = 3, fraction = 6) BigDecimal longitude,
                          @DecimalMin("-90.0") @DecimalMax(value = "90.0") @Digits(integer = 3, fraction = 6) BigDecimal latitude,
                          @NotNull DeliveryStatus status, Instant createdAt,
                          Instant updatedAt) implements Serializable {
}
