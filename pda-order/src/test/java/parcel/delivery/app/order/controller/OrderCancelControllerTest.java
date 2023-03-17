package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.helper.TestOrderService;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.CREATED_BY;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.CREATED_BY_ALT;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;

class OrderCancelControllerTest extends BaseControllerTest {

    private static final String URL_TEMPLATE = "/orders/{id}/cancel";
    public static final String REQ_AUTHORITY = "CANCEL_ORDER";

    @SpyBean
    private OrderRepository orderRepository;
    @Autowired
    private TestOrderService testOrderService;

    private UUID existingOrderId;

    @BeforeEach
    public void setup() {
        this.existingOrderId = testOrderService.save(ORDER)
                .getId();
    }


    @Test
    @WithMockUser
    @DisplayName(URL_TEMPLATE + " should not be accessible without authority CANCEL_ORDER")
    void testAccessibilityWithoutAuthority() throws Exception {
        client.put(URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "john@doe.com", authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return no content for valid request")
    void testValidReq() throws Exception {
        client.put(URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = CREATED_BY_ALT, authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should be forbidden to cancel ")
    void testAccessDeniedToSpecificOrder() throws Exception {
        client.put(URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").isString());
    }


    @WithMockUser(username = "john@doe.com", authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return not found for non-existing UUID")
    @Test
    void testNotFoundOrder() throws Exception {
        Mockito.when(orderRepository.findById(existingOrderId))
                .thenReturn(Optional.empty());

        client.put(URL_TEMPLATE, existingOrderId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isString());
    }

    @WithMockUser(username = CREATED_BY, authorities = {"ROLE_USER", REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return bad request when order status is unable to change")
    @Test
    void testOrderUnableToCancel() throws Exception {
        testOrderService.changeStatus(existingOrderId, OrderStatus.IN_PROGRESS);
        client.put(URL_TEMPLATE, existingOrderId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }


    @AfterEach
    public void cleanup() {
        testOrderService.deleteAll();
    }
}
