package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.repository.OrderRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ASSIGNED_TO;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.CREATED_BY;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER;
import static parcel.delivery.app.order.helper.OrderDomainTestConstants.ORDER_ALT;


class OrdersForUserControllerTest extends BaseControllerTest {
    private static final String URL = "/orders";
    public static final String REQ_AUTHORITY = "VIEW_ORDERS";
    @Autowired
    private OrderRepository orderRepository;


    @BeforeEach
    @Transactional
    public void init() {
        orderRepository.save(ORDER);
        orderRepository.save(ORDER_ALT);
    }

    @Test
    @WithMockUser(username = CREATED_BY, authorities = {"ROLE_USER", REQ_AUTHORITY})
    @DisplayName("User should retrieve list of own orders")
    void testViewOrdersRetrieve() throws Exception {
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

    @Test
    @WithMockUser(username = CREATED_BY, authorities = {"ROLE_ADMIN", REQ_AUTHORITY})
    @DisplayName("Should correctly return orders created by user")
    void testAdminReceiveAllOrders() throws Exception {
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @WithMockUser(username = ASSIGNED_TO, authorities = {"ROLE_COURIER", REQ_AUTHORITY})
    @DisplayName("Should correctly return orders assigned to courier")
    void testCourierReceiveOrderAssignedToHim() throws Exception {
        Order order = ORDER.toBuilder()
                .id(null)
                .assignedTo(ASSIGNED_TO)
                .build();
        orderRepository.saveAndFlush(order);
        client.get(URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].assignedTo").value(ASSIGNED_TO));
    }


    @AfterEach
    @Transactional
    public void cleanup() {
        orderRepository.deleteAll();
    }
}
