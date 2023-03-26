package parcel.delivery.app.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import parcel.delivery.app.delivery.domain.Delivery;

import java.math.BigDecimal;

/**
 * A Projection for the {@link Delivery} entity
 */
@Schema(name = "Location coordinates",
        description = "Describes a location coordinates",
        accessMode = Schema.AccessMode.READ_ONLY
)
public interface LongLat {
    @Schema(description = "Longitude of the coordinates",
            example = "51.523788"
    )
    BigDecimal getLongitude();

    @Schema(description = "Latitude of the coordinates",
            example = "-0.158611"
    )
    BigDecimal getLatitude();
}
