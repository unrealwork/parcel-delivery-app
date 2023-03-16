package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.order.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.api.request.CreateOrderRequest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.error.exception.OrderCancellationException;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;
import parcel.delivery.app.order.mapper.OrderMapper;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Override
    public void changeStatus(UUID id, ChangeStatusRequest changeStatusRequest) throws OrderNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.updateStatus(id, changeStatusRequest.status());
    }

    @Override
    public void cancel(UUID id) throws OrderNotFoundException, AccessDeniedException, OrderCancellationException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!Objects.equals(order.getCreatedBy(), authentication.getName())) {
            throw new AccessDeniedException("Unable to get access to order created not by authenticated user");
        }
        if (order.getStatus()
                .getOrder() > OrderStatus.PENDING.getOrder()) {
            throw new OrderCancellationException();
        }
        orderRepository.updateStatus(id, OrderStatus.CANCELLED);
    }
}
