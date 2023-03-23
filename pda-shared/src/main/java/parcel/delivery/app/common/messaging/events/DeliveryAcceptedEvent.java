package parcel.delivery.app.common.messaging.events;

import java.util.UUID;

public record DeliveryAcceptedEvent(UUID orderId) implements StreamEvent {

    @Override
    public String destination() {
        return EventsOutputChannels.DELIVERY_ACCEPTED;
    }
}
