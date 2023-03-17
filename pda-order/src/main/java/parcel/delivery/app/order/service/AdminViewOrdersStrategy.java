package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.mapper.OrderMapper;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminViewOrdersStrategy implements ViewOrdersStrategy {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public UserType role() {
        return UserType.ADMIN;
    }

    @Override
    @Transactional
    public List<OrderDto> apply(Void request) {
        return orderMapper.toDto(orderRepository.findAll());
    }
}
