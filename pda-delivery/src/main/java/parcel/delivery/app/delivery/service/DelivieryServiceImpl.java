package parcel.delivery.app.delivery.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parcel.delivery.app.common.messaging.EventsEmitter;
import parcel.delivery.app.common.messaging.events.DeliveryAssignedEvent;
import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.dto.DeliveryDto;
import parcel.delivery.app.delivery.dto.LongLat;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DelivieryServiceImpl implements DelvieryService {
    private final ViewDelvieryAggregateStrategy viewDelvieryAggregateStrategy;
    private final AssignCourierStrategy assignCourierStrategy;

    private final TrackDeliveryStrategy trackDeliveryStrategy;

    @Autowired
    private EventsEmitter<DeliveryAssignedEvent> eventsEmitter;

    @Override
    public DeliveryDto get(UUID orderId) {
        return viewDelvieryAggregateStrategy.apply(orderId);
    }

    @Override
    public void assign(UUID orderId, AssignCourierRequest request) {
        assignCourierStrategy.apply(new AssignCourier(orderId, request.courierId()));
        emitOrderAssignedEvent(orderId, request.courierId());
    }

    private void emitOrderAssignedEvent(UUID orderId, @NotNull @Email String courierId) {
        DeliveryAssignedEvent event = new DeliveryAssignedEvent(orderId, courierId);
        eventsEmitter.emit(event);
    }

    @Override
    public LongLat track(UUID orderId) {
        return trackDeliveryStrategy.apply(orderId);
    }
}
