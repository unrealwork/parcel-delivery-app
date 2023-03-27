package parcel.delivery.app.delivery.controller.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.messaging.events.DeliveryAssignedEvent;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.messaging.Sink;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.helper.CourierTestService;
import parcel.delivery.app.delivery.helper.DeliveryTestService;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.equalTo;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.notNullValue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.COURER;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.DELIVERY;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;


@SpringBootTest(properties = {
        "spring.cloud.function.definition=deliveryAssignedSink",
        "spring.cloud.stream.bindings.deliveryAssignedSink-in-0.destination=delivery-assigned",
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class DeliveryAssignControllerTest extends BaseControllerTest {
    private static final String URL = "/deliveries/{orderId}/assign";
    @Autowired
    private DeliveryTestService deliveryTestService;
    @Autowired
    private CourierTestService courierTestService;
    private Courier savedCourier;
    private Delivery savedDelivery;
    @SpyBean
    private Sink<DeliveryAssignedEvent> deliveryAssignedSink;

    @BeforeEach
    public void setup() {
        savedCourier = courierTestService.save(COURER);
        savedDelivery = deliveryTestService.save(DELIVERY);
    }

    @Test
    @WithUserRole
    @DisplayName("Should deny access for user")
    void testAccessForbiddenForUser() throws Exception {
        client.put(new AssignCourierRequest(WithCourierRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCourierRole
    @DisplayName("Should deny access for courier")
    void testAccessForbiddenForCourier() throws Exception {
        client.put(new AssignCourierRequest(WithCourierRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAdminRole
    @DisplayName("Should provide access for admin")
    void testValidRequest() throws Exception {
        client.put(new AssignCourierRequest(WithCourierRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isNoContent());
        Courier courier = courierTestService.findById(WithCourierRole.USERNAME)
                .orElse(null);
        assertThat(courier, notNullValue());
        assertThat(courier.getStatus(), equalTo(CourierStatus.UNAVAILABLE));
        Delivery delivery = deliveryTestService.findById(ORDER_ID)
                .orElse(null);
        assertThat(delivery, notNullValue());
        assertThat(delivery.getCourier()
                        .getUserId(),
                equalTo(WithCourierRole.USERNAME));
    }

    @Test
    @WithAdminRole
    @DisplayName("Should emit delivery-assigned event")
    void testDeliveryAssignedEvent() throws Exception {
        client.put(new AssignCourierRequest(WithCourierRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isNoContent());
        DeliveryAssignedEvent event = new DeliveryAssignedEvent(ORDER_ID, WithCourierRole.USERNAME);
        verify(deliveryAssignedSink, timeout(5000))
                .accept(event);
    }


    @Test
    @WithAdminRole
    @DisplayName("Should return not found for non existing courier")
    void testNonExistingCourier() throws Exception {
        client.put(new AssignCourierRequest(WithUserRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isNotFound());
    }


    @Test
    @WithAdminRole
    @DisplayName("Should return bad request for already assigned deliver")
    void testAlreadyAssignedDelivery() throws Exception {
        savedDelivery.setCourier(savedCourier);
        deliveryTestService.save(savedDelivery);
        client.put(new AssignCourierRequest(WithCourierRole.USERNAME), URL, ORDER_ID)
                .andExpect(status().isBadRequest());
    }


    @AfterEach
    public void cleanup() {
        deliveryTestService.deleteAll();
        courierTestService.deleteAll();
    }
}
