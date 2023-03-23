package parcel.delivery.app.order.messaging.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.events.DeliveryAssignedEvent;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.helper.TestOrderService;

import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.equalTo;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.notNullValue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;

@SpringBootTest(properties = {
        "spring.cloud.function.definition=deliveryAssignedConsumer",
})
@ExtendWith(SpringExtension.class)
class DeliveryAssignedConsumerTest implements BaseIntegreationTest {
    @SpyBean
    private DeliveryAssignedConsumer consumer;
    @Autowired
    private InputDestination inputDestination;
    @Autowired
    private TestOrderService testOrderService;
    private Order savedUser;

    @BeforeEach
    void setup() {
        savedUser = testOrderService.save(ORDER);
    }

    @Test
    @DisplayName("Should consume event sent to output channel")
    void testConsume() {
        DeliveryAssignedEvent event = new DeliveryAssignedEvent(savedUser.getId(), WithCourierRole.USERNAME);
        inputDestination.send(new GenericMessage<>(event));
        Mockito.verify(consumer, Mockito.timeout(5000))
                .accept(event);
    }


    @Test
    @DisplayName("Should consume event sent to output channel")
    void testAction() {
        DeliveryAssignedEvent event = new DeliveryAssignedEvent(savedUser.getId(), WithCourierRole.USERNAME);
        consumer.accept(event);
        Order order = testOrderService.findById(savedUser.getId())
                .orElse(null);
        assertThat(order, notNullValue());
        assertThat(order.getAssignedTo(), equalTo(WithCourierRole.USERNAME));
        assertThat(order.getStatus(), equalTo(OrderStatus.PENDING));
    }
}
