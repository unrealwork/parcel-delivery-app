package parcel.delivery.app.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Courier} entity
 */
@Schema(name = "Courier description",
        accessMode = Schema.AccessMode.READ_ONLY,
        description = """
                Contain details about courier related to delivery process.
                """
)
public record CourierDto(
        @Schema(description = "Unique identifier of courier. Usually email",
                example = "jack@doe.com")
        String userId, @NotNull CourierStatus status,

        @Schema(description = """
                Datetime when the courier details was added
                 """, example = "2023-03-23T18:35:05.785Z")
        Instant createdAt,
        @Schema(description = """
                Datetime when the courier details was modified last time
                 """, example = "2023-03-23T18:35:05.785Z")
        Instant updatedAt) implements Serializable {
}
