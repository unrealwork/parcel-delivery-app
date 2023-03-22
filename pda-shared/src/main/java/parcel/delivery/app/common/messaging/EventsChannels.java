package parcel.delivery.app.common.messaging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventsChannels {
    public static final String USER_CREATED = "userCreatedEvent";
    public static final String ORDER_STATUS_CHANGED = "orderStatusChanged";
    public static final String ORDER_CREATED = "orderCreated";
}
