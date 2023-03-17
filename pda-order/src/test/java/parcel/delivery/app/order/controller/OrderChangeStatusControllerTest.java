package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.helper.OrderDomainTestConstants;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderChangeStatusControllerTest extends BaseControllerTest {
    private static final String URL_TEMPLATE = "/orders/{id}/status";
    private static final String REQ_AUTHORITY = "CHANGE_ORDER_STATUS";

    @SpyBean
    private OrderRepository orderRepository;
    private UUID existingOrderId;

    @BeforeEach
    @Transactional
    public void setup() {
        this.existingOrderId = orderRepository.save(OrderDomainTestConstants.ORDER)
                .getId();
    }


    @Test
    @WithMockUser
    @DisplayName(URL_TEMPLATE + " should not be accessible without authority CHANGE_ORDER_STATUS")
    void testAccessibilityWithoutAuthority() throws Exception {
        client.putJson(null, URL_TEMPLATE, "testid")
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return no content for valid json")
    void testValidReq() throws Exception {
        ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest(OrderStatus.ACCEPTED);
        client.put(changeStatusRequest, URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            {}
            aavfv
            {"status":"NONEXIST"}
                                """)
    @WithMockUser(authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return bad request for non valid json")
    void testBadRequests(String json) throws Exception {
        client.putJson(json, URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }


    @WithMockUser(authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return not found for non-existing UUID")
    @Test
    void testNotFoundOrder() throws Exception {
        Mockito.when(orderRepository.existsById(existingOrderId))
                .thenReturn(false);

        ChangeStatusRequest request = new ChangeStatusRequest(OrderStatus.ACCEPTED);
        client.put(request, URL_TEMPLATE, existingOrderId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isString());
    }


    @AfterEach
    @Transactional
    public void cleanup() {
        orderRepository.deleteAll();
    }
}
