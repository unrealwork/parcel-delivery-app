package parcel.delivery.app.common.messaging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventsOutputChannels {
    public static final String USER_CREATED = "user-created";
    public static final String ORDER_STATUS_CHANGED = "order-status-changed";
    public static final String ORDER_CREATED = "order-created";
    public static final String DELIVERY_ACCEPTED = "delivery-accepted";
    public static final String DELIVERY_ASSIGNED = "delivery-assigned";
}
