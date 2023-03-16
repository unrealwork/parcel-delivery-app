package parcel.delivery.app.order.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;

class OrderCreateControllerTest extends BaseControllerTest {
    private static final String CLIENT_ID = "john@doe.com";
    private static final String URL = "/orders";
    @Autowired
    private OrderRepository orderRepository;

    public static Stream<Arguments> fieldValidationTestCases() {
        return Stream.of(
                arguments(new CreateOrderRequest("", (BigDecimal) null), "description", "must not be blank"),
                arguments(new CreateOrderRequest("", (BigDecimal) null), "weight", "must not be null"),
                arguments(new CreateOrderRequest("", "1.00001"), "weight", "numeric value out of bounds (<3 digits>.<3 digits> expected)"), arguments(new CreateOrderRequest("", "-1.99"), "weight", "must be greater than or equal to 0.0"), arguments(new CreateOrderRequest("", "101.00"), "weight", "must be less than or equal to 100.0"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should not be allowed to access endpoint without CREATE_ORDER privilege")
    void testOrderCreationWithoutPrivilege() throws Exception {
        client.post(URL, null)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should be allowed to access endpoint with CREATE_ORDER privilege")
    @WithMockUser(username = CLIENT_ID, authorities = {"ROLE_USER", "CREATE_ORDER"})
    void testOrderCreation() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest("Test", BigDecimal.ONE);
        client.post(URL, createOrderRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.description").value(createOrderRequest.description()))
                .andExpect(jsonPath("$.status").value(INITIAL.name()))
                .andExpect(jsonPath("$.weight").isNumber());


    }

    @ParameterizedTest
    @MethodSource("fieldValidationTestCases")
    @DisplayName("Should validate fields correctly")
    @WithMockUser(username = CLIENT_ID, authorities = {"ROLE_USER", "CREATE_ORDER"})
    void testRequestBodyValidation(CreateOrderRequest orderDto, String field, String expectedFieldError) throws Exception {
        String fieldJsonPath = "$.fieldErrors." + field;
        client.post(URL, orderDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath(fieldJsonPath).exists())
                .andExpect(jsonPath(fieldJsonPath).value(expectedFieldError));
    }

    @AfterEach
    @Transactional
    public void cleanUp() {
        orderRepository.deleteAll();
    }
}
