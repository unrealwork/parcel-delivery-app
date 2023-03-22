package parcel.delivery.app.order.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.order.domain.Order;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static parcel.delivery.app.common.domain.OrderStatus.INITIAL;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {
    private static final Order TEST_ORDER = Order.builder()
            .createdBy("john@doe.com")
            .status(INITIAL)
            .description("Test")
            .weight(BigDecimal.ONE)
            .destination("20 W 34th St., New York, NY 10001, USA")
            .build();
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @DisplayName("Repository able to save order")
    void testSaving() {
        final Order savedOrder = orderRepository.saveAndFlush(TEST_ORDER);
        assertThat(savedOrder.getId(), notNullValue());
        assertThat(savedOrder.getStatus(), equalTo(INITIAL));
    }

    @Test
    @DisplayName("Repository should update the status")
    void testUpdatesStatus() {
        Order order = orderRepository.save(TEST_ORDER);
        orderRepository.updateStatus(order.getId(), OrderStatus.IN_PROGRESS);
        OrderStatus updatedStatus = orderRepository.findById(order.getId())
                .map(Order::getStatus)
                .orElse(null);
        assertThat(updatedStatus, equalTo(OrderStatus.IN_PROGRESS));
    }

    @Test
    @DisplayName("Repository should update the destination")
    void testUpdatesDestination() {
        Order order = orderRepository.save(TEST_ORDER);
        String newDest = "new_dest";
        orderRepository.updateDestination(order.getId(), newDest);
        String updatedDestination = orderRepository.findById(order.getId())
                .map(Order::getDestination)
                .orElse(null);
        assertThat(updatedDestination, equalTo(newDest));
    }

    @AfterEach
    @Transactional
    public void clean() {
        orderRepository.deleteAll();
    }
}
