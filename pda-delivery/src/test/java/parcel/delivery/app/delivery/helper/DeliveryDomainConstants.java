package parcel.delivery.app.delivery.helper;

import lombok.experimental.UtilityClass;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class DeliveryDomainConstants {
    public static final UUID ORDER_ID = UUID.fromString("4843a6c6-28e7-4a85-8f8d-f7e343389350");
    public static final UUID NON_EXISTING_ORDER_ID = UUID.fromString("4843a6c6-28e7-4a85-8f8d-f7e343389351");
    public static final Delivery DELIVERY = Delivery.builder()
            .orderedBy(WithUserRole.USERNAME)
            .orderId(ORDER_ID)
            .longitude(BigDecimal.ONE)
            .latitude(BigDecimal.ONE)
            .status(DeliveryStatus.INITIAL)
            .build();
    public static final Courier COURER = Courier.builder()
            .status(CourierStatus.UNAVAILABLE)
            .userId(WithCourierRole.USERNAME)
            .build();
}
