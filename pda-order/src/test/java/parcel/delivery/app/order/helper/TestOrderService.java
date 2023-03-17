package parcel.delivery.app.order.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TestOrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order.toBuilder()
                .build());
    }


    public void deleteAll() {
        orderRepository.deleteAll();
    }

    public void changeStatus(UUID existingOrderId, OrderStatus status) {
        orderRepository.updateStatus(existingOrderId, status);
    }
}
