package parcel.delivery.app.delivery.dto;

import parcel.delivery.app.delivery.domain.Delivery;

import java.math.BigDecimal;

/**
 * A Projection for the {@link Delivery} entity
 */
public interface LongLat {
    BigDecimal getLongitude();

    BigDecimal getLatitude();
}
