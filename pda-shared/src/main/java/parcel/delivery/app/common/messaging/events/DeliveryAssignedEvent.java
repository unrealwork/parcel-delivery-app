package parcel.delivery.app.common.messaging.events;

import java.util.UUID;

public record DeliveryAssignedEvent(UUID orderId, String courier) {
}
