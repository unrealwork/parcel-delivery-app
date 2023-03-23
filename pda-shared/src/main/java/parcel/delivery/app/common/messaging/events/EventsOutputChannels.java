package parcel.delivery.app.common.messaging.events;

import lombok.experimental.UtilityClass;

@UtilityClass
final class EventsOutputChannels {
    public static final String USER_CREATED = "user-created";
    public static final String ORDER_STATUS_CHANGED = "order-status-changed";
    public static final String ORDER_CREATED = "order-created";
    public static final String DELIVERY_ACCEPTED = "delivery-accepted";
    public static final String DELIVERY_ASSIGNED = "delivery-assigned";
}
