package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.mapper.OrderMapper;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourierViewOrdersStrategy implements ViewOrdersStrategy {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthenticationFacade facade;

    @Override
    public UserRole role() {
        return UserRole.COURIER;
    }

    @Override
    @Transactional
    public List<OrderDto> apply(Void unused) {
        List<Order> orders = orderRepository.findAllByAssignedToEqualsIgnoreCase(facade.username());
        return orderMapper.toDto(orders);
    }
}
