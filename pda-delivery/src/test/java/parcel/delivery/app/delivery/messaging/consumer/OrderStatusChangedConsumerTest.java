package parcel.delivery.app.delivery.messaging.consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
import parcel.delivery.app.common.test.messaging.BaseMessagingTest;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.helper.CourierTestService;
import parcel.delivery.app.delivery.helper.DeliveryTestService;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.timeout;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.equalTo;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.notNullValue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static parcel.delivery.app.common.domain.OrderStatus.CANCELLED;
import static parcel.delivery.app.delivery.domain.CourierStatus.AVAILABLE;
import static parcel.delivery.app.delivery.domain.CourierStatus.UNAVAILABLE;
import static parcel.delivery.app.delivery.domain.DeliveryStatus.COMPLETED;
import static parcel.delivery.app.delivery.domain.DeliveryStatus.INITIAL;
import static parcel.delivery.app.delivery.domain.DeliveryStatus.IN_PROGRESS;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.COURER;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.DELIVERY;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;

@SpringBootTest(properties = {
        "spring.cloud.stream.output-bindings=test",
        "spring.cloud.stream.bindings.test.destination=order-status-changed"
})
@ExtendWith(SpringExtension.class)
class OrderStatusChangedConsumerTest extends BaseMessagingTest {
    public static final String OUTPUT_BINDING = "test";
    @Autowired
    private StreamBridge streamBridge;
    @SpyBean
    private OrderStatusChangedConsumer statusChangedConsumer;
    @Autowired
    private DeliveryTestService deliveryTestService;
    @Autowired
    private CourierTestService courierTestService;

    public static Stream<Arguments> testCases() {
        return Stream.of(
                arguments(OrderStatus.CANCELLED, IN_PROGRESS, UNAVAILABLE, INITIAL, AVAILABLE),
                arguments(OrderStatus.IN_PROGRESS, INITIAL, UNAVAILABLE, IN_PROGRESS, UNAVAILABLE),
                arguments(OrderStatus.DELIVERED, IN_PROGRESS, UNAVAILABLE, COMPLETED, AVAILABLE),
                arguments(OrderStatus.ACCEPTED, INITIAL, AVAILABLE, INITIAL, AVAILABLE)
        );
    }

    @Test
    @DisplayName("Should consumer be triggered correctly")
    void testTrigger() {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(ORDER_ID, CANCELLED);
        streamBridge.send(OUTPUT_BINDING, event);
        Mockito.verify(statusChangedConsumer, timeout(5000))
                .accept(event);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Should set correct statuses for courier and delivery depends on event order status")
    void testInStatusChanging(OrderStatus orderStatus,
                              DeliveryStatus beforeDeliveryStatus, CourierStatus beforeCourierStatus,
                              DeliveryStatus afterDeliveryStatus, CourierStatus afterCourierStatus) {
        Courier savedCourier = courierTestService.save(COURER.toBuilder()
                .status(beforeCourierStatus)
                .build());
        Delivery delivery = deliveryTestService.save(DELIVERY.toBuilder()
                .courier(savedCourier)
                .status(beforeDeliveryStatus)
                .build());

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(delivery.getOrderId(), orderStatus);
        statusChangedConsumer.accept(event);
        Delivery updated = deliveryTestService.findById(delivery.getOrderId())
                .orElse(null);
        assertThat(updated, notNullValue());
        assertThat(updated.getStatus(), equalTo(afterDeliveryStatus));
        Courier updatedCourier = courierTestService.findById(updated.getCourier()
                        .getUserId())
                .orElse(null);
        assertThat(updatedCourier, notNullValue());
        assertThat(updatedCourier.getStatus(), equalTo(afterCourierStatus));
    }

    @AfterEach
    void cleanup() {
        deliveryTestService.deleteAll();
        courierTestService.deleteAll();
    }
}
