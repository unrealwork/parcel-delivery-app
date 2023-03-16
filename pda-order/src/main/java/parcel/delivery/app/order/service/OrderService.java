package parcel.delivery.app.order.service;

import org.springframework.lang.NonNull;
import parcel.delivery.app.order.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.error.OrderNotFoundException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> ordersForUser(@NonNull String username);

    OrderDto create(CreateOrderRequest order);

    void changeStatus(UUID id, ChangeStatusRequest changeStatusRequest) throws OrderNotFoundException;
}
