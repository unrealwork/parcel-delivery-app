package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.domain.DeliveryDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DelvieryServiceImpl implements DelvieryService {
    private final ViewDelvieryAggregateStrategy viewDelvieryAggregateStrategy;
    private final AssignCourierStrategy assignCourierStrategy;

    @Override
    public DeliveryDto get(UUID orderId) {
        return viewDelvieryAggregateStrategy.apply(orderId);
    }

    @Override
    public void assign(UUID orderId, AssignCourierRequest request) {
        assignCourierStrategy.apply(new AssignCourier(orderId, request.courierId()));
    }
}
