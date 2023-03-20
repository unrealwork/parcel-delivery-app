package parcel.delivery.app.delivery.controller.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.helper.CourierTestService;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.COURER;

class CouriersControllerTest extends BaseControllerTest {

    @Autowired
    private CourierTestService courierTestService;

    @BeforeEach
    public void setup() {
        courierTestService.save(COURER);
    }

    @Test
    @WithUserRole
    @DisplayName("Should deny access without VIEW_COURIERS role")
    void testAccessDeniedForNonAdmin() throws Exception {
        client.get("/couriers")
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAdminRole
    @DisplayName("Should permit access with VIEW_COURIERS role")
    void testAccessForAdmin() throws Exception {
        client.get("/couriers")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(1)));
    }

    @AfterEach
    public void cleanup() {
        courierTestService.deleteAll();
    }
}
