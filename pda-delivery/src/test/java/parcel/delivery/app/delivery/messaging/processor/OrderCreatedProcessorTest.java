package parcel.delivery.app.delivery.messaging.processor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.messaging.EventsOutputChannels;
import parcel.delivery.app.common.messaging.events.OrderCreatedEvent;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;
import parcel.delivery.app.common.test.messaging.Sink;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.helper.DeliveryTestService;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;


@SpringBootTest(properties = {
        "spring.cloud.function.definition=orderCreatedProcessor;deliveryAcceptedSink",
        "spring.cloud.stream.bindings.orderCreatedProcessor-in-0.destination=order-created",
        "spring.cloud.stream.bindings.orderCreatedProcessor-out-0.destination=order-status-changed",
        "spring.cloud.stream.bindings.deliveryAcceptedSink-in-0.destination=order-status-changed"
})
@ExtendWith(SpringExtension.class)
class OrderCreatedProcessorTest extends BaseIntegreationTest {
    @Autowired
    private StreamBridge streamBridge;
    @SpyBean
    private OrderCreatedProcessor processor;
    @SpyBean
    private Sink<UUID> deliveryAcceptedSink;

    @Autowired
    private DeliveryTestService testService;

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("Should consume and produce event")
    void testApply() {
        OrderCreatedEvent event = new OrderCreatedEvent(ORDER_ID, WithUserRole.USERNAME);
        GenericMessage<OrderCreatedEvent> message = new GenericMessage<>(event);
        streamBridge.send(EventsOutputChannels.ORDER_CREATED, message);
        // In
        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(processor, Mockito.timeout(5000))
                .apply(captor.capture());
        assertThat(captor.getValue(), equalTo(event));
        Delivery createdDelivery = testService.findById(ORDER_ID)
                .orElse(null);
        assertThat(createdDelivery, notNullValue());
        assertThat(createdDelivery.getStatus(), equalTo(DeliveryStatus.INITIAL));
        assertThat(createdDelivery.getOrderedBy(), equalTo(WithUserRole.USERNAME));
        // Out
        verify(deliveryAcceptedSink, timeout(5000)).accept(ORDER_ID);
    }

    @AfterEach
    void cleanup() {
        testService.deleteAll();
    }

}
