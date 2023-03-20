package parcel.delivery.app.delivery.dto;

import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Courier} entity
 */
public record CourierDto(String userId, @NotNull CourierStatus status, Instant createdAt,
                         Instant updatedAt) implements Serializable {
}
