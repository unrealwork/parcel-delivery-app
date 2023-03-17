package parcel.delivery.app.order.helper;

import lombok.experimental.UtilityClass;
import parcel.delivery.app.order.domain.Order;

import java.math.BigDecimal;

import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;

@UtilityClass
public class OrderDomainTestConstants {
    public static final String CREATED_BY = "john@doe.com";
    public static final String CREATED_BY_ALT = "jane@doe.com";
    public static final String DESCRIPTION = "Test description";
    public static final String DESTINATION = "20 W 34th St., New York, NY 10001, USA";
    public static final String DESTINATION_ALT = "11 Aleksandr Pushkin St, T'bilisi 0105";
    public static final Order ORDER = Order.builder()
            .createdBy(CREATED_BY)
            .status(INITIAL)
            .description(DESCRIPTION)
            .weight(BigDecimal.ONE)
            .destination(DESTINATION)
            .build();
}
