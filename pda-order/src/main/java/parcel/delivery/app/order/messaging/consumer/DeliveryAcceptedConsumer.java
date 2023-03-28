package parcel.delivery.app.order.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.events.DeliveryAcceptedEvent;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class DeliveryAcceptedConsumer implements Consumer<DeliveryAcceptedEvent> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void accept(DeliveryAcceptedEvent event) {
        orderRepository.updateStatus(event.orderId(), OrderStatus.ACCEPTED);
    }
}
