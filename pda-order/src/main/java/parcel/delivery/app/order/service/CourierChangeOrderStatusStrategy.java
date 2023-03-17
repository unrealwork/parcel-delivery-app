package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;
import parcel.delivery.app.order.error.exception.OrderNotAllowedStatusException;
import parcel.delivery.app.order.error.exception.OrderNotAssignedToCoureierException;
import parcel.delivery.app.order.repository.OrderRepository;

import static parcel.delivery.app.order.domain.OrderStatus.ACCEPTED;
import static parcel.delivery.app.order.domain.OrderStatus.DELIVERED;
import static parcel.delivery.app.order.domain.OrderStatus.IN_PROGRESS;
import static parcel.delivery.app.order.domain.OrderStatus.PENDING;

@Component
@RequiredArgsConstructor
public class CourierChangeOrderStatusStrategy implements ChangeOrderStatusStrategy {
    private final FindOrderStrategy findOrderStrategy;
    private final OrderRepository orderRepository;
    private final AuthenticationFacade auth;

    @Override
    public UserType role() {
        return UserType.COURIER;
    }

    @Override
    @Transactional
    public Void apply(OrderRequest<ChangeStatusRequest> request) {
        Order order = findOrderStrategy.apply(request.id());
        if (!auth.username()
                .equals(order.getAssignedTo())) {
            throw new OrderNotAssignedToCoureierException();
        }
        OrderStatus currentStatus = order.getStatus();
        ChangeStatusRequest req = request.req();
        OrderStatus newStatus = req.status();
        if (!isAllowedStatusChange(currentStatus, newStatus)) {
            throw new OrderNotAllowedStatusException(currentStatus, newStatus);
        }
        orderRepository.updateStatus(request.id(), newStatus);
        return null;
    }

    private boolean isAllowedStatusChange(OrderStatus currentStatus, OrderStatus newStatus) {
        return currentStatus == IN_PROGRESS && newStatus == DELIVERED
                || currentStatus == PENDING && newStatus == ACCEPTED;
    }
}
