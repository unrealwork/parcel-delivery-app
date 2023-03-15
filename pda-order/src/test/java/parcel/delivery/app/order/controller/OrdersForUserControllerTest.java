package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.controller.client.ApiRestClient;
import parcel.delivery.app.order.domain.OrderDto;
import parcel.delivery.app.order.repository.OrderRepository;
import parcel.delivery.app.order.service.OrderService;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class OrdersForUserControllerTest {
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final String URL = "/orders";
    private static final String CLIENT_ID = "test@mail.com";
    @Autowired
    private ApiRestClient client;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @WithMockUser(username = CLIENT_ID, authorities = {"ROLE_USER", "VIEW_ORDERS"})
    @DisplayName("User should retrieve list of own orders")
    void testViewOrdersRetrieve() throws Exception {
        OrderDto order = new OrderDto(ORDER_ID, INITIAL, CLIENT_ID, Instant.EPOCH, Instant.EPOCH);
        orderService.create(order);
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].createdBy")
                        .value(CLIENT_ID))
                .andExpect(jsonPath("$[0].status").value(INITIAL.name()));
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
