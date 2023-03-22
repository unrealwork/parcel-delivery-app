package parcel.delivery.app.delivery.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import parcel.delivery.app.common.messaging.EventsOutputChannels;
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
    private final StreamBridge streamBridge;

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
        streamBridge.send(EventsOutputChannels.DELIVERY_ASSIGNED, new DeliveryAssignedEvent(orderId, courierId));
    }

    @Override
    public LongLat track(UUID orderId) {
        return trackDeliveryStrategy.apply(orderId);
    }
}
