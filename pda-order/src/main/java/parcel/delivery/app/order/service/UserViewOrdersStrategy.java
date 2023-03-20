package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.mapper.OrderMapper;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserViewOrdersStrategy implements ViewOrdersStrategy {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthenticationFacade auth;

    @Override
    public UserRole role() {
        return UserRole.USER;
    }

    @Override
    public List<OrderDto> apply(Void request) {
        List<Order> orders = orderRepository.findAllByCreatedByEqualsIgnoreCase(auth.username());
        return orderMapper.toDto(orders);
    }
}
