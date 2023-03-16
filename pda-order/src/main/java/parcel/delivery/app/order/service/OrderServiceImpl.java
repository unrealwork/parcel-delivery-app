package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.dto.OrderDto;
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
    public OrderDto create(CreateOrderRequest order) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Order entity = Order.builder()
                .status(OrderStatus.INITIAL)
                .description(order.description())
                .createdBy(authentication.getName())
                .weight(order.weight())
                .build();
        Order saved = orderRepository.save(entity);
        return orderMapper.toDto(saved);
    }
}
