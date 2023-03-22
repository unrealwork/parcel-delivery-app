package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.EventsChannels;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
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
        emitOrderAcceptedEvent(orderId);

    }

    private void emitOrderAcceptedEvent(UUID orderId) {
        streamBridge.send(EventsChannels.ORDER_STATUS_CHANGED, new OrderStatusChangedEvent(orderId, OrderStatus.ACCEPTED));
    }

    @Override
    public LongLat track(UUID orderId) {
        return trackDeliveryStrategy.apply(orderId);
    }
}
