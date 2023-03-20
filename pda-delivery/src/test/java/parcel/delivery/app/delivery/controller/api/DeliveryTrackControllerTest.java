package parcel.delivery.app.delivery.controller.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.helper.DeliveryDomainConstants;
import parcel.delivery.app.delivery.helper.DeliveryTestService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.NON_EXISTING_ORDER_ID;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;

class DeliveryTrackControllerTest extends BaseControllerTest {
    private static final String URL = "/deliveries/{orderId}/track";

    @Autowired
    private DeliveryTestService deliveryTestService;

    @BeforeEach
    public void setup() {
        deliveryTestService.save(DeliveryDomainConstants.DELIVERY);
    }

    @Test
    @WithUserRole
    @DisplayName("Should deny access without TRACK_DELIVERY permission")
    void testDeniedAccessForUser() throws Exception {
        client.get(URL, ORDER_ID)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAdminRole
    @DisplayName("Should permit access with TRACK_DELIVERY permission")
    void testPermitAccessForUser() throws Exception {
        client.get(URL, ORDER_ID)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.longitude").isNumber())
                .andExpect(jsonPath("$.latitude").isNumber());
    }

    @Test
    @WithAdminRole
    @DisplayName("Should return not found for non-existing delivery")
    void testNotFoundFor() throws Exception {
        client.get(URL, NON_EXISTING_ORDER_ID)
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanup() {
        deliveryTestService.deleteAll();
    }
}
