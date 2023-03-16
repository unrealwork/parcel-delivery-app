package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.repository.OrderRepository;
import parcel.delivery.app.order.service.OrderService;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;


class OrdersForUserControllerTest extends BaseControllerTest {
    private static final String URL = "/orders";
    private static final String CLIENT_ID = "test@mail.com";
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @WithMockUser(username = CLIENT_ID, authorities = {"ROLE_USER", "VIEW_ORDERS"})
    @DisplayName("User should retrieve list of own orders")
    void testViewOrdersRetrieve() throws Exception {
        CreateOrderRequest order = new CreateOrderRequest("Parcel Description", BigDecimal.ONE);
        orderService.create(order);
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].createdBy")
                        .value(CLIENT_ID))
                .andExpect(jsonPath("$[0].status").value(INITIAL.name()))
                .andExpect(jsonPath("$[0].description").value(order.description()))
                .andExpect(jsonPath("$[0].weight").isNumber());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Should request authentication for user")
    void testUnavailabilityForAnonymous() throws Exception {
        client.get(URL)
                .andExpect(status().isUnauthorized());
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        orderRepository.deleteAll();
    }
}
