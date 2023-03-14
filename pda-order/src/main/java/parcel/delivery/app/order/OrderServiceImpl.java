package parcel.delivery.app.order;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.repository.OrderRepository;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order save(Order order) {
        return null;
    }
}
