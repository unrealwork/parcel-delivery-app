package parcel.delivery.app.common.messaging.events;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, @NonNull String createdBy) {
}
