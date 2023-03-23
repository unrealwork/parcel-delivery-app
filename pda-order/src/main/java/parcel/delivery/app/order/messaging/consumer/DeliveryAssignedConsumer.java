package parcel.delivery.app.order.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.messaging.events.DeliveryAssignedEvent;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.function.Consumer;

import static parcel.delivery.app.common.domain.OrderStatus.PENDING;

@Component
@RequiredArgsConstructor
public class DeliveryAssignedConsumer implements Consumer<DeliveryAssignedEvent> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void accept(DeliveryAssignedEvent event) {
        orderRepository.updateStatusAndCourier(event.orderId(), PENDING, event.courier());
    }
}
