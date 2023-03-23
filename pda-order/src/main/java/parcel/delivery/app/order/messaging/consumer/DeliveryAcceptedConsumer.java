package parcel.delivery.app.order.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.UUID;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class DeliveryAcceptedConsumer implements Consumer<UUID> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void accept(UUID uuid) {
        orderRepository.updateStatus(uuid, OrderStatus.ACCEPTED);
    }
}
