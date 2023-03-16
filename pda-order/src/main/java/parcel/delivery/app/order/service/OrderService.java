package parcel.delivery.app.order.service;

import org.springframework.lang.NonNull;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> ordersForUser(@NonNull String username);

    OrderDto create(CreateOrderRequest order);
}
