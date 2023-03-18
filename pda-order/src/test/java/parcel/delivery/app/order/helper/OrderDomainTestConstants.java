package parcel.delivery.app.order.helper;

import lombok.experimental.UtilityClass;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.order.domain.Order;

import java.math.BigDecimal;

import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;

@UtilityClass
public class OrderDomainTestConstants {
    public static final String ASSIGNED_TO = "jack@doe.com";
    public static final String DESCRIPTION = "Test description";
    public static final String DESTINATION = "20 W 34th St., New York, NY 10001, USA";
    public static final String DESTINATION_ALT = "11 Aleksandr Pushkin St, T'bilisi 0105";
    public static final Order ORDER = Order.builder()
            .createdBy(WithUserRole.USERNAME)
            .status(INITIAL)
            .description(DESCRIPTION)
            .weight(BigDecimal.ONE)
            .destination(DESTINATION)
            .build();

    public static final Order ORDER_ALT = Order.builder()
            .createdBy(WithAdminRole.USERNAME)
            .status(INITIAL)
            .description(DESCRIPTION)
            .weight(BigDecimal.ONE)
            .destination(DESTINATION_ALT)
            .build();
}
