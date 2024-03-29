package parcel.delivery.app.delivery.messaging.processor;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.messaging.events.DeliveryAcceptedEvent;
import parcel.delivery.app.common.messaging.events.OrderCreatedEvent;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderCreatedProcessor implements Function<OrderCreatedEvent, DeliveryAcceptedEvent> {
    private final DeliveryRepository deliveryRepository;


    @Override
    @Transactional
    public DeliveryAcceptedEvent apply(OrderCreatedEvent event) {
        deliveryRepository.findById(event.orderId())
                .ifPresentOrElse(this::resetStatusToInitial,
                        () -> createDeliver(event));
        return new DeliveryAcceptedEvent(event.orderId());
    }

    private void resetStatusToInitial(Delivery d) {
        deliveryRepository.updateStatus(d.getOrderId(), DeliveryStatus.INITIAL);
    }

    private void createDeliver(OrderCreatedEvent event) {
        Delivery delivery = Delivery.builder()
                .orderedBy(event.createdBy())
                .status(DeliveryStatus.INITIAL)
                .orderId(event.orderId())
                .build();
        this.deliveryRepository.save(delivery);
    }
}
