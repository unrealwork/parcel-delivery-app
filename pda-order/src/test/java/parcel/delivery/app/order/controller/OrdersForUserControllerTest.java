package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.repository.OrderRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.CREATED_BY;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;


class OrdersForUserControllerTest extends BaseControllerTest {
    private static final String URL = "/orders";
    public static final String REQ_AUTHORITY = "VIEW_ORDERS";
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @WithMockUser(username = CREATED_BY, authorities = {REQ_AUTHORITY})
    @DisplayName("User should retrieve list of own orders")
    void testViewOrdersRetrieve() throws Exception {
        orderRepository.save(ORDER);
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].createdBy")
                        .value(CREATED_BY))
                .andExpect(jsonPath("$[0].status").value(INITIAL.name()))
                .andExpect(jsonPath("$[0].description").value(ORDER.getDescription()))
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
