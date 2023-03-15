package parcel.delivery.app.order.service;

import org.springframework.lang.NonNull;
import parcel.delivery.app.order.domain.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> ordersForUser(@NonNull String username);

    OrderDto create(OrderDto order);
}
