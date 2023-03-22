package parcel.delivery.app.order.messaging.consumer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class OrderStatusChangedConsumer implements Consumer<OrderStatusChangedEvent> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void accept(OrderStatusChangedEvent orderStatusChangedEvent) {
        orderRepository.updateStatus(orderStatusChangedEvent.id(), orderStatusChangedEvent.status());
    }
}
