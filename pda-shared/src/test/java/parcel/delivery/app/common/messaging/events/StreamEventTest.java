package parcel.delivery.app.common.messaging.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static parcel.delivery.app.common.messaging.events.EventsOutputChannels.DELIVERY_ACCEPTED;
import static parcel.delivery.app.common.messaging.events.EventsOutputChannels.DELIVERY_ASSIGNED;
import static parcel.delivery.app.common.messaging.events.EventsOutputChannels.ORDER_CREATED;
import static parcel.delivery.app.common.messaging.events.EventsOutputChannels.ORDER_STATUS_CHANGED;
import static parcel.delivery.app.common.messaging.events.EventsOutputChannels.USER_CREATED;

class StreamEventTest {
    private static final UUID ORDER_ID = UUID.fromString("4843a6c6-28e7-4a85-8f8d-f7e343389350");

    public static Stream<Arguments> testCases() {
        return Stream.of(
                arguments(new SignedUpEvent(UserRole.USER, ""), USER_CREATED),
                arguments(new OrderCreatedEvent(ORDER_ID, ORDER_CREATED), ORDER_CREATED),
                arguments(new OrderStatusChangedEvent(ORDER_ID, OrderStatus.IN_PROGRESS), ORDER_STATUS_CHANGED),
                arguments(new DeliveryAcceptedEvent(ORDER_ID), DELIVERY_ACCEPTED),
                arguments(new DeliveryAssignedEvent(ORDER_ID, ""), DELIVERY_ASSIGNED)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Stream events have valid destinations")
    void destination(StreamEvent streamEvent, String destination) {
        assertThat(streamEvent.destination(), equalTo(destination));
    }
}
