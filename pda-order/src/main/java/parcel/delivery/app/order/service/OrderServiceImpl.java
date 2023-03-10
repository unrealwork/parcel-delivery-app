package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderDto;
import parcel.delivery.app.order.mapper.OrderMapper;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> ordersForUser(@NonNull String username) {
        return orderRepository.findAllByCreatedByEqualsIgnoreCase(username)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto create(OrderDto order) {
        Order entity = orderMapper.toEntity(order);
        Order saved = orderRepository.save(entity);
        return orderMapper.toDto(saved);
    }
}
