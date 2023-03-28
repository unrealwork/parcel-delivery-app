package parcel.delivery.app.order.messaging.consumer;

import org.junit.jupiter.api.AfterEach;
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
import parcel.delivery.app.common.messaging.events.DeliveryAcceptedEvent;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.helper.TestOrderService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;


@SpringBootTest(properties = {
        "spring.cloud.function.definition=deliveryAcceptedConsumer",
})
@ExtendWith(SpringExtension.class)
class DeliveryAcceptedConsumerTest implements BaseIntegreationTest {
    @SpyBean
    private DeliveryAcceptedConsumer consumer;
    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private TestOrderService testOrderService;
    private Order savedOrder;

    @BeforeEach
    void setup() {
        savedOrder = testOrderService.save(ORDER);
    }


    @Test
    @DisplayName("Should handle delivery accepted event")
    void testTrigger() {
        DeliveryAcceptedEvent event = new DeliveryAcceptedEvent(savedOrder.getId());
        inputDestination.send(new GenericMessage<>(event));
        verify(consumer, Mockito.timeout(5000)).accept(event);
    }

    @Test
    @DisplayName("Should change status when accept method called")
    void testAccept() {
        DeliveryAcceptedEvent event = new DeliveryAcceptedEvent(savedOrder.getId());
        consumer.accept(event);
        Order updatedOrder = testOrderService.findById(savedOrder.getId())
                .orElse(null);
        assertThat(updatedOrder, notNullValue());
        assertThat(updatedOrder.getStatus(), equalTo(OrderStatus.ACCEPTED));
    }

    @AfterEach
    public void cleanup() {
        testOrderService.deleteAll();
    }
}
