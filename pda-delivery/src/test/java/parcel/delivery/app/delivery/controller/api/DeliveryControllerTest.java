package parcel.delivery.app.delivery.controller.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.helper.CourierTestService;
import parcel.delivery.app.delivery.helper.DeliveryTestService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.COURER;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.DELIVERY;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.NON_EXISTING_ORDER_ID;

class DeliveryControllerTest extends BaseControllerTest {
    public static final String URL = "/deliveries/{orderId}";
    @Autowired
    private DeliveryTestService deliveryTestService;
    @Autowired
    private CourierTestService courierTestService;
    private Delivery savedDelivery;

    @BeforeEach
    public void setup() {
        Courier savedCourier = this.courierTestService.save(COURER);
        this.savedDelivery = deliveryTestService.save(DELIVERY.toBuilder()
                .courier(savedCourier)
                .build());
    }


    @Test
    @WithUserRole
    @DisplayName("Should provide access to own delivery")
    void testDeliveryAccessForUser() throws Exception {
        client.get(URL, DELIVERY.getOrderId())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserRole
    @DisplayName("Should deny access to not own delivery")
    void testAccessToNotOwnOrder() throws Exception {
        deliveryTestService.save(savedDelivery.toBuilder()
                .orderedBy(WithAdminRole.USERNAME)
                .build());
        client.get(URL, DELIVERY.getOrderId())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    @WithUserRole
    @DisplayName("Should return not found status for non existing_order")
    void testAccessToNotFoundOrder() throws Exception {
        client.get(URL, NON_EXISTING_ORDER_ID)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    @WithCourierRole
    @DisplayName("Should provide access to delivery if it is assigned to the user")
    void testAccessToAssignedOrder() throws Exception {
        client.get(URL, savedDelivery.getOrderId())
                .andExpect(status().isOk());
    }


    @Test
    @WithCourierRole
    @DisplayName("Should deny access to delivery if it is not assigned to the user")
    void testAccessDeniedToAssignedOrder() throws Exception {
        Courier courier = courierTestService.save(COURER
                .toBuilder()
                .userId(WithAdminRole.USERNAME)
                .build());
        deliveryTestService.save(savedDelivery.toBuilder()
                .courier(courier)
                .build());
        client.get(URL, savedDelivery.getOrderId())
                .andExpect(status().isForbidden());
    }

    @AfterEach
    public void cleanup() {
        deliveryTestService.deleteAll();
        courierTestService.deleteAll();
    }
}
