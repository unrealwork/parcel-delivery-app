package parcel.delivery.app.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.repository.OrderRepository;

import static parcel.delivery.app.order.domain.OrderStatus.INITIAL;

@SpringBootApplication
@Slf4j
public class PdaOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(PdaOrderApp.class, args);
    }


    @Bean
    @Profile("dev")
    public InitializingBean testData(OrderRepository orderRepository) {
        return () -> {
            String creator = "test@mail.com";
            Order order = Order.builder()
                    .createdBy(creator)
                    .status(INITIAL)
                    .build();
            final Order savedOrder = orderRepository.save(order);
            log.info("Test data saved {}", savedOrder);
        };
    }
}
