package parcel.delivery.app.order.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.common.messaging.events.OrderCreatedEvent;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;
import parcel.delivery.app.common.test.messaging.Sink;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.order.controller.api.request.CreateOrderRequest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.helper.OrderDomainTestConstants;
import parcel.delivery.app.order.helper.TestOrderService;

import java.math.BigDecimal;

import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.function.definition=orderStatusChangedSink;orderCreatedSink",
                "spring.cloud.stream.bindings.orderStatusChangedSink-in-0.destination=order-status-changed",
                "spring.cloud.stream.bindings.orderCreatedSink-in-0.destination=order-created"
        })
class OrderServiceTest implements BaseIntegreationTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TestOrderService testOrderService;
    @SpyBean
    private Sink<OrderStatusChangedEvent> statusChangedEventSink;
    @SpyBean
    private Sink<OrderCreatedEvent> orderCreatedEventSink;
    private Order savedOrder;


    @BeforeEach
    void setup() {
        savedOrder = testOrderService.save(ORDER);
    }


    @Test
    @DisplayName("Should emit order-status-changed event")
    @WithUserRole
    void testEmitOrderStatusChangedEvent() {
        orderService.cancel(savedOrder.getId());
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(savedOrder.getId(), OrderStatus.CANCELLED);
        Mockito.verify(statusChangedEventSink, Mockito.timeout(5000))
                .accept(event);
    }

    @Test
    @DisplayName("Should emit order-status-changed event")
    @WithUserRole
    void testOrderCreatedEvent() {
        OrderDto order = orderService.create(CreateOrderRequest.builder()
                .description(OrderDomainTestConstants.DESCRIPTION)
                .weight(BigDecimal.ONE)
                .destination(OrderDomainTestConstants.DESTINATION)
                .build());
        OrderCreatedEvent event = new OrderCreatedEvent(order.id(), WithUserRole.USERNAME);
        Mockito.verify(orderCreatedEventSink, Mockito.timeout(5000))
                .accept(event);

    }

    @AfterEach
    void cleanup() {
        testOrderService.deleteAll();
    }
}
