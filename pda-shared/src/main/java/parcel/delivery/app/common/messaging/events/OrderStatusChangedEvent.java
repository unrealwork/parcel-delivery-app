package parcel.delivery.app.common.messaging.events;

import org.springframework.lang.NonNull;
import parcel.delivery.app.common.domain.OrderStatus;

import java.util.UUID;


public record OrderStatusChangedEvent(UUID id, @NonNull OrderStatus status) implements StreamEvent {
    @Override
    public String destination() {
        return EventsOutputChannels.ORDER_STATUS_CHANGED;
    }
}
