package parcel.delivery.app.order.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.domain.Order;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    

    @Test
    @DisplayName("Repository able to save order")
    void test() {
        String creator = "test@mail.com";
        Order order = Order.builder()
                .createdBy(creator)
                .status(INITIAL)
                .build();
        final Order savedOrder = orderRepository.saveAndFlush(order);
        assertThat(savedOrder.getId(), notNullValue());
        assertThat(savedOrder.getStatus(), equalTo(INITIAL));
    }

    @AfterEach
    @Transactional
    public void clean() {
        orderRepository.deleteAll();
    }
}
