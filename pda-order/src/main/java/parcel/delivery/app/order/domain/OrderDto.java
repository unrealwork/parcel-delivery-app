package parcel.delivery.app.order.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A DTO for the {@link Order} entity
 */
public record OrderDto(UUID id, @NotNull OrderStatus status, @NotBlank String createdBy,
                       Instant createdAt,
                       Instant updatedAt) implements Serializable {
}
