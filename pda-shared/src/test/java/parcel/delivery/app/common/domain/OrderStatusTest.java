package parcel.delivery.app.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class OrderStatusTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            INITIAL,0
            ACCEPTED,1
            PENDING,2,
            IN_PROGRESS,3
            DELIVERED,4
            CANCELLED,5
            """)
    @DisplayName("Should have valid order")
    void testOrder(OrderStatus status, int order) {
        assertThat(status.getOrder(), equalTo(order));
    }
}
