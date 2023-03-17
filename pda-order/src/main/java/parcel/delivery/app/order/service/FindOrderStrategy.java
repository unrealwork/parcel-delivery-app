package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class FindOrderStrategy implements Function<UUID, Order> {
    private final OrderRepository orderRepository;

    @Override
    public Order apply(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(id);
        }
        return order.get();
    }
}
