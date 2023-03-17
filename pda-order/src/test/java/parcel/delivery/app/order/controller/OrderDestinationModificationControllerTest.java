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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.controller.api.request.ChangeOrderDestinationRequest;
import parcel.delivery.app.order.helper.TestOrderService;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.IN_PROGRESS;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.CREATED_BY;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.DESTINATION_ALT;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;

@ExtendWith(MockitoExtension.class)
class OrderDestinationModificationControllerTest extends BaseControllerTest {
    public static final String REQ_AUTHORITY = "CHANGE_ORDER_STATUS";
    private static final String URL_TEMPLATE = "/orders/{id}/destination";
    @SpyBean
    private OrderRepository orderRepository;

    @Autowired
    private TestOrderService testOrderService;
    private UUID existingOrderId;

    @BeforeEach
    @Transactional
    public void setup() {
        this.existingOrderId = testOrderService.save(ORDER)
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
    @WithMockUser(username = CREATED_BY, authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return no content for valid json")
    void testValidReq() throws Exception {
        ChangeOrderDestinationRequest request = new ChangeOrderDestinationRequest(DESTINATION_ALT);
        client.put(request, URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            {}
            aavfv
            {"status":"NONEXIST"}
                                """)
    @WithMockUser(username = CREATED_BY, authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return bad request for non valid json")
    void testBadRequests(String json) throws Exception {
        client.putJson(json, URL_TEMPLATE, existingOrderId)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }


    @WithMockUser(username = CREATED_BY, authorities = {REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should return not found for non-existing UUID")
    @Test
    void testNotFoundOrder() throws Exception {
        Mockito.when(orderRepository.findById(existingOrderId))
                .thenReturn(Optional.empty());

        ChangeOrderDestinationRequest request = new ChangeOrderDestinationRequest(DESTINATION_ALT);
        client.put(request, URL_TEMPLATE, existingOrderId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isString());
    }

    @WithMockUser(username = CREATED_BY, authorities = {"ROLE_USER", REQ_AUTHORITY})
    @DisplayName(URL_TEMPLATE + " should not be allowed for modification if status=IN_PROGRESS")
    @Test
    @Transactional
    void testNotAllowedModification() throws Exception {
        testOrderService.changeStatus(existingOrderId, IN_PROGRESS);
        ChangeOrderDestinationRequest request = new ChangeOrderDestinationRequest(DESTINATION_ALT);
        client.put(request, URL_TEMPLATE, existingOrderId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }


    @AfterEach
    @Transactional
    public void cleanup() {
        orderRepository.deleteAll();
    }
}
