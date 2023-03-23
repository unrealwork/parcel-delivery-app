package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.order.controller.api.request.CreateOrderRequest;
import parcel.delivery.app.order.helper.TestOrderService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.common.domain.OrderStatus.INITIAL;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.DESCRIPTION;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.DESTINATION;

class OrderCreateControllerTest extends BaseControllerTest {
    private static final String URL = "/orders";
    @Autowired
    private TestOrderService orderService;

    @Test
    @WithMockUser
    @DisplayName("Should not be allowed to access endpoint without CREATE_ORDER privilege")
    void testOrderCreationWithoutPrivilege() throws Exception {
        client.post(null, URL)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should be allowed to access endpoint with CREATE_ORDER privilege")
    @WithUserRole
    void testOrderCreation() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(DESCRIPTION, DESTINATION, BigDecimal.ONE);
        client.post(createOrderRequest, URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.description").value(createOrderRequest.description()))
                .andExpect(jsonPath("$.status").value(INITIAL.name()))
                .andExpect(jsonPath("$.weight").isNumber());


    }

    @ParameterizedTest
    @CsvSource(textBlock = """
                {},description,must not be blank
                {},weight,must not be null,
                {"destination": ""},destination,must not be blank,
                {"weight": 1.00001},weight,numeric value out of bounds (<3 digits>.<3 digits> expected)
                {"weight": "-1.99"},weight,must be greater than or equal to 0.0
                {"weight": "101.00"},weight,must be less than or equal to 100.0
            """)
    @DisplayName("Should validate fields correctly")
    @WithUserRole
    void testRequestBodyValidation(String jsonReq, String field, String expectedFieldError) throws Exception {
        String fieldJsonPath = "$.fieldErrors." + field;
        client.postJson(jsonReq, URL)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath(fieldJsonPath).exists())
                .andExpect(jsonPath(fieldJsonPath).value(expectedFieldError));
    }

    @AfterEach
    @Transactional
    public void cleanUp() {
        orderService.deleteAll();
    }
}
