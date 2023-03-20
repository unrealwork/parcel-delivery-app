package parcel.delivery.app.delivery.domain;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Courier} entity
 */
public record CourierDto(String userId, @NotNull CourierStatus status, Instant createdAt,
                         Instant updatedAt) implements Serializable {
}
