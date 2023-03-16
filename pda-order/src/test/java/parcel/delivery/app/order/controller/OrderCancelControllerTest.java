package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.repository.OrderRepository;
import parcel.delivery.app.order.service.OrderService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderCancelControllerTest extends BaseControllerTest {

    private static final String URL_TEMPLATE = "/orders/{id}/cancel";

    @SpyBean
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    private UUID existingOrderId;

    @BeforeEach
    @Transactional
    public void setup() {
        Order order = Order.builder()
                .createdBy("john@doe.com")
                .weight(BigDecimal.ONE)
                .description("Test")
                .status(OrderStatus.INITIAL)
                .build();
        this.existingOrderId = orderRepository.save(order)
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
    @WithMockUser(username = "john@doe.com", authorities = {"CANCEL_ORDER"})
    @DisplayName(URL_TEMPLATE + " should return no content for valid request")
    void testValidReq() throws Exception {
        client.put(URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "jane@doe.com", authorities = {"CANCEL_ORDER"})
    @DisplayName(URL_TEMPLATE + " should be forbidden to cancel ")
    void testAccessDeniedToSpecificOrder() throws Exception {
        client.put(URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").isString());
    }


    @WithMockUser(username = "john@doe.com", authorities = {"CANCEL_ORDER"})
    @DisplayName(URL_TEMPLATE + " should return not found for non-existing UUID")
    @Test
    void testNotFoundOrder() throws Exception {
        Mockito.when(orderRepository.findById(existingOrderId))
                .thenReturn(Optional.empty());

        client.put(URL_TEMPLATE, existingOrderId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isString());
    }

    @WithMockUser(username = "john@doe.com", authorities = {"CANCEL_ORDER"})
    @DisplayName(URL_TEMPLATE + " should return bad request when order status is unable to change")
    @Test
    void testOrderUnableToCancel() throws Exception {
        orderService.changeStatus(existingOrderId, new ChangeStatusRequest(OrderStatus.IN_PROGRESS));
        client.put(URL_TEMPLATE, existingOrderId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }


    @AfterEach
    @Transactional
    public void cleanup() {
        orderRepository.deleteAll();
    }
}
