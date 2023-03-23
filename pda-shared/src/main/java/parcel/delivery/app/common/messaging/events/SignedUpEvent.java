package parcel.delivery.app.common.messaging.events;

import parcel.delivery.app.common.security.core.UserRole;

public record SignedUpEvent(UserRole userRole, String userId) implements StreamEvent {
    @Override
    public String destination() {
        return EventsOutputChannels.USER_CREATED;
    }
}
