package parcel.delivery.app.order.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A DTO for the {@link Order} entity
 */
public record OrderDto(UUID id, @NotBlank String description,
                       @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer = 3, fraction = 3)
                       BigDecimal weight,
                       @NotNull OrderStatus status,

                       @Email @NotNull String createdBy,

                       @Email String assignedTo,
                       @NotNull String destination,
                       Instant createdAt,
                       Instant updatedAt) implements Serializable {
}
