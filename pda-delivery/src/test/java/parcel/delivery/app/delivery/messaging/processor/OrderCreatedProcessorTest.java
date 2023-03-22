package parcel.delivery.app.delivery.messaging.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.messaging.events.OrderCreatedEvent;
import parcel.delivery.app.common.test.messaging.BaseMessagingTest;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class OrderCreatedProcessorTest extends BaseMessagingTest {
    @Autowired
    private OrderCreatedProcessor orderCreatedProcessor;
    @MockBean
    private DeliveryRepository deliveryRepository;


    @Test
    @DisplayName("Should create delivery on OrderCreatedEvent ")
    void testApply() {
        orderCreatedProcessor.apply(new OrderCreatedEvent(ORDER_ID, WithUserRole.USERNAME));
        Delivery deliveryToCreate = Delivery.builder()
                .orderId(ORDER_ID)
                .status(DeliveryStatus.INITIAL)
                .build();
        Mockito.verify(deliveryRepository)
                .save(deliveryToCreate);
    }
}
