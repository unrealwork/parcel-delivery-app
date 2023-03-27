package parcel.delivery.app.delivery.messaging.consumer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.repository.CourierRepository;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static parcel.delivery.app.delivery.domain.CourierStatus.AVAILABLE;
import static parcel.delivery.app.delivery.domain.DeliveryStatus.INITIAL;

@Component
@RequiredArgsConstructor
public class OrderStatusChangedConsumer implements Consumer<OrderStatusChangedEvent> {
    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public void accept(OrderStatusChangedEvent orderStatusChangedEvent) {
        final OrderStatus status = orderStatusChangedEvent.status();
        deliveryRepository.findById(orderStatusChangedEvent.id())
                .ifPresent(d -> handleStatusChangeForExistingDelivery(d, status));
    }

    private void handleStatusChangeForExistingDelivery(@NonNull Delivery delivery, OrderStatus status) {
        final UUID id = delivery.getOrderId();
        if (status == OrderStatus.IN_PROGRESS) {
            deliveryRepository.findById(id)
                    .ifPresent(this::updateInprogressDelivery);
        }
        if (status == OrderStatus.DELIVERED) {
            deliveryRepository.updateStatus(id, DeliveryStatus.COMPLETED);
            makeCourierAvailable(delivery);
        }
        if (status == OrderStatus.CANCELLED) {
            deliveryRepository.updateStatus(id, INITIAL);
            /*
              User can't cancel assigned order, but if it happens somehow
              make sure that courier status is available.
             */
            makeCourierAvailable(delivery);
        }
    }

    private void updateInprogressDelivery(Delivery delivery) {
        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        // Coordinates are hardcoded 
        delivery.setLatitude(new BigDecimal("51.523788"));
        delivery.setLatitude(new BigDecimal("-0.158611"));
        deliveryRepository.save(delivery);
    }

    private void makeCourierAvailable(Delivery delivery) {
        Optional.of(delivery)
                .map(Delivery::getCourier)
                .map(Courier::getUserId)
                .ifPresent(cId -> courierRepository.updateStatus(cId, AVAILABLE));
    }
}
