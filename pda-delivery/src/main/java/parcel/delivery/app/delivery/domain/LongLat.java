package parcel.delivery.app.delivery.domain;

import java.math.BigDecimal;

/**
 * A Projection for the {@link Delivery} entity
 */
public interface LongLat {
    BigDecimal getLongitude();

    BigDecimal getLatitude();
}
