package parcel.delivery.app.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;
import parcel.delivery.app.order.repository.OrderRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminChangeOrderStrategy implements ChangeOrderStatusStrategy {
    private final OrderRepository orderRepository;

    @Override
    public UserRole role() {
        return UserRole.ADMIN;
    }


    @Override
    public Void apply(OrderRequest<ChangeStatusRequest> request) throws OrderNotFoundException {
        UUID id = request.id();
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        ChangeStatusRequest changeStatusRequest = request.req();
        orderRepository.updateStatus(id, changeStatusRequest.status());
        return null;
    }
}
