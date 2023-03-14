package parcel.delivery.app.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.order.controller.client.ApiRestClient;
import parcel.delivery.app.order.domain.OrderDto;
import parcel.delivery.app.order.service.OrderService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
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
    @MockBean
    private OrderService orderService;
    @Autowired
    private JsonMatchers jsonMatchers;


    @Test
    @WithMockUser(username = CLIENT_ID, authorities = {"ROLE_USER", "VIEW_ORDERS"})
    @DisplayName("User should retrieve list of own orders")
    void testViewOrdersRetrieve() throws Exception {
        OrderDto order = new OrderDto(ORDER_ID, INITIAL, CLIENT_ID, Instant.EPOCH, Instant.EPOCH);
        List<OrderDto> orders = List.of(order);
        Mockito.when(orderService.ordersForUser(CLIENT_ID))
                .thenReturn(orders);
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonMatchers.isJsonArray(orders, new TypeReference<>() {
                }));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Should request authentication for user")
    void testUnavailabilityForAnonymous() throws Exception {
        client.get(URL)
                .andExpect(status().isUnauthorized());
    }
}
